/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

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
			final float celestial_period = this.getAltitudeAngle(world.getWorldTime(), partial);
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
				size = 75.0;
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
					getHorizonAngle(world.getWorldTime(), (double) this.tilt, (double) this.period), partial, 1.0f,
					size);
		}

		@Override
		public float getAltitudeAngle(final long time, final float partialTime) {
			Vector3f sunPos = getSunPos(time, (double) this.angle, (double) this.tilt, (double) this.period);
			Vector3f noon = new Vector3f(0, 1, 0);
			double angCurrent = Vector3f.angle(sunPos, noon);
			if (this.period == 0) {
			} else if ((((time % this.period) / this.period) + this.phase) % 1 > 0.5) {
				angCurrent += Math.PI;
			}
			angCurrent /= Math.PI * 2;
			return (float) angCurrent;
		}

		private float getHorizonAngle(final long time, double tilt, double period) {
			Vector3f sunPos = getSunPos(time, this.angle, tilt, period);
			Vector3f sunPosFlat = new Vector3f(sunPos.getX(), 0, sunPos.getZ()).normalise(null);
			Vector3f horZ = new Vector3f(0, 0, 1);
			double angCurrent = Math.PI - Vector3f.angle(sunPosFlat, horZ);
			Vector3f horX = new Vector3f(1, 0, 0);
			double angCheck = Vector3f.angle(sunPosFlat, horX);
			if (angCheck < Math.PI / 2) {
				angCurrent *= -1;
			}
			// System.out.println(sunPos.getZ());
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

		private Vector3f getSunPos(final long time, double angle, double tilt, double period) {

			tilt = Math.toRadians(tilt);
			double angY = -(double) angle;
			angY = Math.toRadians(angY);
			double angZ;
			if (period == 0) {
				angZ = 0;
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
	}
}
