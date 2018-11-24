/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.symbol;

import com.xcompwiz.mystcraft.api.util.Color;
import com.xcompwiz.mystcraft.api.util.ColorGradient;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.symbol.SunsetRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoreMystcraftSunsetRenderer {

	private float[] colorsSunriseSunset;
	protected AgeDirector controller;
	protected ColorGradient gradient;

	public MoreMystcraftSunsetRenderer(final AgeDirector controller, final ColorGradient gradient) {
		this.colorsSunriseSunset = new float[4];
		this.controller = controller;
		this.gradient = gradient;
	}

	@SideOnly(Side.CLIENT)
	protected float[] getSunriseSunsetColors(final float celestial_angle, final float partialtick, final double size) {
		final float f2;
		final float f4;
		if (size == 10) {
			f2 = 0.15f;
		} else if (size == 20) {
			f2 = 0.25f;
		} else if (size == 75) {
			f2 = 0.6f;
		} else if (size == 120) {
			f2 = 0.8f;
		} else {
			f2 = 0.4f;
		}
		f4 = -0.0f;
		final float f3 = (float) Math.cos(celestial_angle * Math.PI * 2.0f);
		if (f3 >= f4 - f2 && f3 <= f4 + f2) {
			final float f5 = (f3 - f4) / f2 * 0.5f + 0.5f;
			float f6 = (float) (1 - (1 - Math.sin(f5 * Math.PI)) * 0.99);
			f6 *= f6;
			ColorGradient gradient = this.gradient;
			Color color = null;
			if (gradient == null) {
				gradient = this.controller.getSunriseSunsetColor();
			}
			if (gradient != null && gradient.getColorCount() > 0) {
				color = gradient.getColor(this.controller.getTime() / 12000.0f);
			}
			if (color == null) {
				this.colorsSunriseSunset[0] = f5 * 0.3f + 0.7f;
				this.colorsSunriseSunset[1] = f5 * f5 * 0.7f + 0.2f;
				this.colorsSunriseSunset[2] = f5 * f5 * 0.0f + 0.2f;
			} else {
				this.colorsSunriseSunset[0] = color.r;
				this.colorsSunriseSunset[1] = color.g;
				this.colorsSunriseSunset[2] = color.b;
			}
			this.colorsSunriseSunset[3] = f6;
			return this.colorsSunriseSunset;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	protected void renderHorizon(final TextureManager eng, final World worldObj, final float celestial_period,
			final float angle, final float partial, final float alpha, final double size) {
		final float celestial_radians = celestial_period * 3.1415927f * 2.0f;
		final Tessellator tes = Tessellator.getInstance();
		final BufferBuilder vb = tes.getBuffer();
		RenderHelper.disableStandardItemLighting();
		final float[] horizoncolors = this.getSunriseSunsetColors(celestial_period, partial, size);
		if (horizoncolors != null) {
			float horizonRed = horizoncolors[0];
			float horizonGreen = horizoncolors[1];
			float horizonBlue = horizoncolors[2];
			final float[] array = horizoncolors;
			final int n = 3;
			array[n] *= alpha;
			if (Minecraft.getMinecraft().gameSettings.anaglyph) {
				final float red = (horizonRed * 30.0f + horizonGreen * 59.0f + horizonBlue * 11.0f) / 100.0f;
				final float green = (horizonRed * 30.0f + horizonGreen * 70.0f) / 100.0f;
				final float blue = (horizonRed * 30.0f + horizonBlue * 70.0f) / 100.0f;
				horizonRed = red;
				horizonGreen = green;
				horizonBlue = blue;
			}
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
			GlStateManager.rotate((Math.sin(celestial_radians) < 0.0f) ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
			GlStateManager.rotate(-angle, 0.0f, 0.0f, 1.0f);
			vb.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vb.pos(0.0, 100.0, 0.0).color(horizonRed, horizonGreen, horizonBlue, horizoncolors[3]).endVertex();
			for (int l = 0; l <= 16; ++l) {
				final float f21 = l * 6.2831855f / 16.0f;
				final float f22 = (float) Math.sin(f21);
				final float f23 = (float) Math.cos(f21);
				vb.pos((double) (f22 * 120.0f), (double) (f23 * 120.0f), (double) (-f23 * 40.0f * horizoncolors[3]))
						.color(horizonRed, horizonGreen, horizonBlue, 0.0f).endVertex();
			}
			tes.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(7424);
		}
	}

}
