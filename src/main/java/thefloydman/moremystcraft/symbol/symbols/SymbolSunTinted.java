/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thefloydman.moremystcraft.lwjgl.Matrix3f;
import thefloydman.moremystcraft.lwjgl.Vector3f;

import com.xcompwiz.mystcraft.api.util.Color;
import com.xcompwiz.mystcraft.api.util.ColorGradient;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.ICelestial;
import com.xcompwiz.mystcraft.symbol.SunsetRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.symbol.MoreMystcraftSunsetRenderer;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.util.Reference;

public class SymbolSunTinted extends MoreMystcraftSymbolBase {
	public SymbolSunTinted(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final Number period = controller.popModifier("wavelength").asNumber();
		final Number angle = controller.popModifier("angle").asNumber();
		final Number phase = controller.popModifier("phase").asNumber();
		final ColorGradient sunset = controller.popModifier("sunset").asGradient();
		final Color color = controller.popModifier("color").asColor();
		final Number size = controller.popModifier("size").asNumber();
		final Number tilt = controller.popModifier("tilt").asNumber();
		controller.registerInterface(
				new CelestialObject(controller, seed, period, angle, phase, sunset, color, size, tilt));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private static class CelestialObject extends MoreMystcraftSunsetRenderer implements ICelestial {
		private Random rand;
		private long period;
		private float angle;
		private float phase;
		private Color color;
		private double size;
		private float tilt;

		CelestialObject(final AgeDirector controller, final long seed, Number period, Number angle, Number phase,
				final ColorGradient gradient, Color color, Number size, Number tilt) {
			super(controller, gradient);
			this.rand = new Random(seed);
			if (period == null) {
				period = 0.4 * this.rand.nextDouble() + 0.8;
			}
			this.period = (long) (period.doubleValue() * 24000.0);
			if (angle == null) {
				angle = this.rand.nextDouble() * 360.0;
			}
			this.angle = -angle.floatValue();
			if (phase != null) {
				phase = phase.floatValue() / 360.0f;
			}
			if (phase == null) {
				phase = this.rand.nextFloat();
				if (this.period == 0L) {
					phase = phase.floatValue() / 2.0f + 0.25f;
				}
			}
			this.phase = phase.floatValue() - 0.5f;
			this.color = color;
			if (size != null) {
				this.size = size.doubleValue();
			} else {
				this.size = 1.0d;
			}
			if (tilt != null) {
				this.tilt = tilt.floatValue();
			} else {
				this.tilt = 0.0f;
			}
		}

		@Override
		public boolean providesLight() {
			return true;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void render(final TextureManager eng, final World world, final float partial) {

			final Tessellator tes = Tessellator.getInstance();
			final BufferBuilder vb = tes.getBuffer();
			final float celestial_period = this.getAltitudeForSunset(world.getWorldTime(), partial);
			GlStateManager.enableTexture2D();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			final float f16 = 1F - world.getRainStrength(partial);
			GlStateManager.color(this.color.r, this.color.g, this.color.b, f16);

			GlStateManager.rotate(this.angle, 0f, 1f, 0f);
			GlStateManager.rotate(this.tilt, 1f, 0f, 0f);
			float rotZ = (float) (((360 * (world.getWorldTime() / (double) this.period)) + (this.phase * 360)) % 360);
			GlStateManager.rotate(rotZ, 0f, 0f, 1f);

			double size;
			if (this.size < 0.375) {
				size = 10.0;
			} else if (this.size >= 0.375 && this.size < 0.75) {
				size = 20.0;
			} else if (this.size >= 0.75 && this.size < 1.5) {
				size = 30.0;
			} else if (this.size >= 1.5 && this.size < 2.5) {
				size = 70.0;
			} else if (this.size >= 2.5) {
				size = 120.0;
			} else {
				size = 30.0;
			}
			eng.bindTexture(Reference.forMoreMystcraft("textures/environment/sun_neutral.png"));
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);
			vb.pos(-size, 100d, -size).tex(0.0, 0.0).endVertex();
			vb.pos(size, 100d, -size).tex(1.0, 0.0).endVertex();
			vb.pos(size, 100d, size).tex(1.0, 1.0).endVertex();
			vb.pos(-size, 100d, size).tex(0.0, 1.0).endVertex();
			tes.draw();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.popMatrix();
			this.renderHorizon(eng, world, celestial_period,
					getHorizonAngle(world.getWorldTime(), (double) this.tilt, (double) this.period), partial, 1.0f);
		}

		@Override
		public float getAltitudeAngle(final long time, final float partialTime) {

			Vector3f sunPos = getSunPos(time, (double) this.angle, (double) this.tilt, (double) this.period);
			Vector3f noon = new Vector3f(0, 1, 0);
			double angCurrent = Vector3f.angle(sunPos, noon);
			if (this.period == 0) {
			} else if ((((time % (double) this.period) / (double) this.period) + this.phase) % 1 > 0.5) {
				angCurrent = (2 * Math.PI) - angCurrent;
			}
			angCurrent /= 2 * Math.PI;
			angCurrent %= 1;

			float sunsInAQuarter;
			if (this.size < 0.375) {
				sunsInAQuarter = 11.0f;
			} else if (this.size >= 0.375 && this.size < 0.75) {
				sunsInAQuarter = 9.0f;
			} else if (this.size >= 0.75 && this.size < 1.5) {
				sunsInAQuarter = 4.5f;
			} else if (this.size >= 1.5 && this.size < 2.5) {
				sunsInAQuarter = 2.25f;
			} else if (this.size >= 2.5) {
				sunsInAQuarter = 1.5f;
			} else {
				sunsInAQuarter = 4.5f;
			}
			float angAdj = (0.25f / sunsInAQuarter) / 2.0f;

			if (angCurrent <= 0.5) {
				angCurrent -= angAdj;
			} else {
				angCurrent += angAdj;
			}
			
			angCurrent %= 1;

			return (float) angCurrent;
		}

		private float getAltitudeForSunset(final long time, final float partialTime) {

			Vector3f sunPos = getSunPos(time, (double) this.angle, (double) this.tilt, (double) this.period);
			Vector3f noon = new Vector3f(0, 1, 0);
			double angCurrent = Vector3f.angle(sunPos, noon);
			if (this.period == 0) {
			} else if ((((time % (double) this.period) / (double) this.period) + this.phase) % 1 > 0.5) {
				angCurrent = (2 * Math.PI) - angCurrent;
			}
			angCurrent /= 2 * Math.PI;
			angCurrent %= 1;

			/*
			 * This lovely section of code adjusts the motion of the mock sun so that
			 * sunrises and sunsets laster longer or shorter depending on the size of the
			 * rendered sun.
			 */

			float sunsInAQuarter = 10.0f;
			if (this.size < 0.375) {
				sunsInAQuarter = 10.0f;
			} else if (this.size >= 0.375 && this.size < 0.75) {
				sunsInAQuarter = 8.0f;
			} else if (this.size >= 0.75 && this.size < 1.5) {
				sunsInAQuarter = 4.5f;
			} else if (this.size >= 1.5 && this.size < 2.5) {
				sunsInAQuarter = 3.0f;
			} else if (this.size >= 2.5) {
				sunsInAQuarter = 2.0f;
			} else {
				sunsInAQuarter = 4.5f;
			}
			float angAdj = (0.25f / sunsInAQuarter) / 2.0f;

			float[] coords0 = { 0.0f, 0.0f, 0.25f - (2.0f * angAdj), 0.25f - angAdj };
			float[] coords1 = { 0.25f - (2.0f * angAdj), 0.25f - angAdj, 0.25f - angAdj, 0.25f };
			float[] coords2 = { 0.25f - angAdj, 0.25f, 0.25f + angAdj, 0.25f };
			float[] coords3 = { 0.25f + angAdj, 0.25f, 0.25f + (2.0f * angAdj), 0.25f + angAdj };
			float[] coords4 = { 0.25f + (2.0f * angAdj), 0.25f + angAdj, 0.75f - (2.0f * angAdj), 0.75f - angAdj };
			float[] coords5 = { 0.75f - (2.0f * angAdj), 0.75f - angAdj, 0.75f - angAdj, 0.75f };
			float[] coords6 = { 0.75f - angAdj, 0.75f, 0.75f + angAdj, 0.75f };
			float[] coords7 = { 0.75f + angAdj, 0.75f, 0.75f + (2.0f * angAdj), 0.75f + angAdj };
			float[] coords8 = { 0.75f + (2.0f * angAdj), 0.75f + angAdj, 1.0f, 1.0f };
			float slope0 = findSlope(coords0[0], coords0[1], coords0[2], coords0[3]);
			float slope1 = findSlope(coords1[0], coords1[1], coords1[2], coords1[3]);
			float slope2 = findSlope(coords2[0], coords2[1], coords2[2], coords2[3]);
			float slope3 = findSlope(coords3[0], coords3[1], coords3[2], coords3[3]);
			float slope4 = findSlope(coords4[0], coords4[1], coords4[2], coords4[3]);
			float slope5 = findSlope(coords5[0], coords5[1], coords5[2], coords5[3]);
			float slope6 = findSlope(coords6[0], coords6[1], coords6[2], coords6[3]);
			float slope7 = findSlope(coords7[0], coords7[1], coords7[2], coords7[3]);
			float slope8 = findSlope(coords8[0], coords8[1], coords8[2], coords8[3]);
			float intercept0 = findYIntercept(coords0[0], coords0[1], slope0);
			float intercept1 = findYIntercept(coords1[0], coords1[1], slope1);
			float intercept2 = findYIntercept(coords2[0], coords2[1], slope2);
			float intercept3 = findYIntercept(coords3[0], coords3[1], slope3);
			float intercept4 = findYIntercept(coords4[0], coords4[1], slope4);
			float intercept5 = findYIntercept(coords5[0], coords5[1], slope5);
			float intercept6 = findYIntercept(coords6[0], coords6[1], slope6);
			float intercept7 = findYIntercept(coords7[0], coords7[1], slope7);
			float intercept8 = findYIntercept(coords8[0], coords8[1], slope8);
			if (angCurrent < 0.25 - (angAdj * 2)) {
				angCurrent *= slope0;
				angCurrent += intercept0;
			} else if (angCurrent >= 0.25 - (angAdj * 2) && angCurrent < 0.25 - angAdj) {
				angCurrent *= slope1;
				angCurrent += intercept1;
			} else if (angCurrent >= 0.25 - angAdj && angCurrent < 0.25 + angAdj) {
				angCurrent *= slope2;
				angCurrent += intercept2;
			} else if (angCurrent >= 0.25 + angAdj && angCurrent < 0.25 + (angAdj * 2)) {
				angCurrent *= slope3;
				angCurrent += intercept3;
			} else if (angCurrent >= 0.25 + (angAdj * 2) && angCurrent < 0.75 - (angAdj * 2)) {
				angCurrent *= slope4;
				angCurrent += intercept4;
			} else if (angCurrent >= 0.75 - (angAdj * 2) && angCurrent < 0.75 - angAdj) {
				angCurrent *= slope5;
				angCurrent += intercept5;
			} else if (angCurrent >= 0.75 - angAdj && angCurrent < 0.75 + angAdj) {
				angCurrent *= slope6;
				angCurrent += intercept6;
			} else if (angCurrent >= 0.75 + angAdj && angCurrent < 0.75 + (angAdj * 2)) {
				angCurrent *= slope7;
				angCurrent += intercept7;
			} else if (angCurrent >= 0.75 + (angAdj * 2)) {
				angCurrent *= slope8;
				angCurrent += intercept8;
			}

			angCurrent %= 1;
			return (float) angCurrent;
		}

		private float getHorizonAngle(final long time, double tilt, double period) {
			Vector3f sunPos = getSunPos((double) time, this.angle, tilt, period);
			Vector3f sunPosFlat = new Vector3f(sunPos.getX(), 0, sunPos.getZ()).normalise(null);
			Vector3f horZ = new Vector3f(0, 0, 1);
			double angCurrent = Math.PI - Vector3f.angle(sunPosFlat, horZ);
			Vector3f horX = new Vector3f(1, 0, 0);
			double angCheck = Vector3f.angle(sunPosFlat, horX);
			if (angCheck < Math.PI / 2) {
				angCurrent *= -1;
			}
			return (float) Math.toDegrees(angCurrent);

		}

		@Override
		public Long getTimeToDawn(final long time) {

			if (this.period == 0L) {
				return null;
			}
			final long current = time % this.period;
			long next = (long) (this.period * Math.abs(0.75f - this.phase));
			if (current > next) {
				next += this.period;
			}
			return next - current;

		}

		private Vector3f getSunPos(final double time, double angle, double tilt, double period) {

			tilt = Math.toRadians(tilt);
			double angY = -angle;
			angY = Math.toRadians(angY);
			double angZ;
			if (period == 0) {
				angZ = (this.phase * 360) % 360;
			} else {
				angZ = ((360 * (time / period)) + (this.phase * 360)) % 360;
			}
			angZ = Math.toRadians(angZ);

			Vector3f sunPos = new Vector3f(0, 1, 0);

			Matrix3f rotX = matrixRotX(tilt);
			Matrix3f rotY = matrixRotY(angY);
			Matrix3f rotZ = matrixRotZ(angZ);

			Matrix3f.transform(rotZ, sunPos, sunPos);
			Matrix3f.transform(rotX, sunPos, sunPos);
			Matrix3f.transform(rotY, sunPos, sunPos);

			return sunPos;

		}

		private Matrix3f matrixRotX(double ang) {
			Matrix3f rot = new Matrix3f();
			rot.m00 = 1;
			rot.m01 = 0;
			rot.m02 = 0;
			rot.m10 = 0;
			rot.m11 = (float) Math.cos(ang);
			rot.m12 = -(float) Math.sin(ang);
			rot.m20 = 0;
			rot.m21 = (float) Math.sin(ang);
			rot.m22 = (float) Math.cos(ang);
			return rot;
		}

		private Matrix3f matrixRotY(double ang) {
			Matrix3f rot = new Matrix3f();
			rot.m00 = (float) Math.cos(ang);
			rot.m01 = 0;
			rot.m02 = (float) Math.sin(ang);
			rot.m10 = 0;
			rot.m11 = 1;
			rot.m12 = 0;
			rot.m20 = -(float) Math.sin(ang);
			rot.m21 = 0;
			rot.m22 = (float) Math.cos(ang);
			return rot;
		}

		private Matrix3f matrixRotZ(double ang) {
			Matrix3f rot = new Matrix3f();
			rot.m00 = (float) Math.cos(ang);
			rot.m01 = -(float) Math.sin(ang);
			rot.m02 = 0;
			rot.m10 = (float) Math.sin(ang);
			rot.m11 = (float) Math.cos(ang);
			rot.m12 = 0;
			rot.m20 = 0;
			rot.m21 = 0;
			rot.m22 = 1;
			return rot;
		}

		private float findSlope(final float input1, final float output1, final float input2, final float output2) {
			return (output2 - output1) / (input2 - input1);
		}

		private float findYIntercept(float input, float output, float slope) {
			return output - (input * slope);
		}
	}
}
