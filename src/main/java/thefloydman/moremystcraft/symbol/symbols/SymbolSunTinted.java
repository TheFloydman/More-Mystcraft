package thefloydman.moremystcraft.symbol.symbols;

import net.minecraft.util.*;
import com.xcompwiz.mystcraft.api.world.*;
import com.xcompwiz.mystcraft.api.world.logic.*;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.util.*;
import java.util.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.*;
import com.xcompwiz.mystcraft.data.*;
import com.xcompwiz.mystcraft.symbol.SunsetRenderer;

import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraftforge.fml.relauncher.*;
import thefloydman.moremystcraft.symbol.SymbolBase;

public class SymbolSunTinted extends SymbolBase {
	public SymbolSunTinted(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final Number period = controller.popModifier("wavelength").asNumber();
		final Number angle = controller.popModifier("angle").asNumber();
		final Number offset = controller.popModifier("phase").asNumber();
		final ColorGradient sunset = controller.popModifier("sunset").asGradient();
		final Color color = controller.popModifier("color").asColor();
		final Number size = controller.popModifier("size").asNumber();
		controller.registerInterface(new CelestialObject(controller, seed, period, angle, offset, sunset, color, size));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private static class CelestialObject extends SunsetRenderer implements ICelestial {
		private Random rand;
		private long period;
		private float angle;
		private float offset;
		private Color color;
		private double size;

		CelestialObject(final AgeDirector controller, final long seed, Number period, Number angle, Number offset,
				final ColorGradient gradient, Color color, Number size) {
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
			if (offset != null) {
				offset = offset.floatValue() / 360.0f;
			}
			if (offset == null) {
				offset = this.rand.nextFloat();
				if (this.period == 0L) {
					offset = offset.floatValue() / 2.0f + 0.25f;
				}
			}
			this.offset = offset.floatValue() - 0.5f;
			this.color = color;
			if (size != null) {
				this.size = size.doubleValue();
			} else
				this.size = 1.0d;
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
			final float f16 = 1.0f - world.getRainStrength(partial);
			GlStateManager.color(this.color.r, this.color.g, this.color.b, f16);
			GlStateManager.rotate(this.angle, 0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(celestial_period * 360.0f, 1.0f, 0.0f, 0.0f);
			final float size;
			if (this.size < 0.375) {
				size = 10.0f;
			} else if (this.size >= 0.375 && this.size < 0.75) {
				size = 20.0f;
			} else if (this.size >= 0.75 && this.size < 1.5) {
				size = 30.0f;
			} else if (this.size >= 1.5 && this.size < 2.5) {
				size = 75.0f;
			} else if (this.size >= 2.5) {
				size = 120.0f;
			} else {
				size = 30.0f;
			}
			eng.bindTexture(new ResourceLocation("moremystcraft", "textures/environment/sun_neutral.png"));
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);
			vb.pos((double) (-size), 100.0, (double) (-size)).tex(0.0, 0.0).endVertex();
			vb.pos((double) size, 100.0, (double) (-size)).tex(1.0, 0.0).endVertex();
			vb.pos((double) size, 100.0, (double) size).tex(1.0, 1.0).endVertex();
			vb.pos((double) (-size), 100.0, (double) size).tex(0.0, 1.0).endVertex();
			tes.draw();
			GlStateManager.popMatrix();
			this.renderHorizon(eng, world, celestial_period, this.angle, partial, 1.0f);
		}

		@Override
		public float getAltitudeAngle(final long time, final float partialTime) {
			if (this.period == 0L) {
				return this.offset;
			}
			final int i = (int) (time % this.period);
			float f = (i + partialTime) / this.period + this.offset;
			if (f < 0.0f) {
				++f;
			}
			if (f > 1.0f) {
				--f;
			}
			final float f2 = f;
			f = 1.0f - (float) ((Math.cos(f * 3.141592653589793) + 1.0) / 2.0);
			f = f2 + (f - f2) / 3.0f;
			return f;
		}

		@Override
		public Long getTimeToDawn(final long time) {
			/*
			 * if (this.period == 0L) { return null; } final long current = time %
			 * this.period; long next = (long) (this.period * Math.abs(0.75f -
			 * this.offset)); if (current > next) { next += this.period; } return next -
			 * current;
			 */

			return 1000L;
		}
	}
}
