package thefloydman.moremystcraft.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.util.handlers.PotionListHandler;

public class GuiMaintainerSuit extends Gui {

	protected int viewWidth = 192;
	protected int viewHeight = 128;
	protected int viewTop;
	protected int viewBottom;
	protected int viewLeft;
	protected int viewRight;
	protected int width;
	protected int height;

	protected double zLevel = -1000.0D;

	protected String[] effectsCode = { "effect.blindness", "effect.digSlowDown", "effect.hunger", "effect.confusion",
			"effect.poison", "effect.moveSlowdown", "effect.weakness", "effect.wither" };
	protected String[] effectsName = { "Blindness", "Fatigue", "Hunger", "Nausea", "Poison", "Slowness", "Weakness",
			"Wither" };

	@SubscribeEvent
	public void renderHPOverlay(RenderGameOverlayEvent.Pre event) {
		boolean ridingMaintainerSuit = false;
		EntityPlayer player = Minecraft.getMinecraft().player;
		Entity entity = player.getRidingEntity();
		if (player.isRiding() && entity instanceof EntityMaintainerSuit) {
			ridingMaintainerSuit = true;
		}
		if (ridingMaintainerSuit) {
			GlStateManager.pushMatrix();
			width = event.getResolution().getScaledWidth();
			height = event.getResolution().getScaledHeight();
			viewTop = (height - viewHeight) / 2;
			viewBottom = viewTop + viewHeight;
			viewLeft = (width - viewWidth) / 2;
			viewRight = viewLeft + viewWidth;

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			int color = Color.BLACK.getRGB();
			drawRect(0, 0, width, viewTop, color, zLevel);
			drawRect(0, viewTop, viewLeft, viewBottom, color, zLevel);
			drawRect(viewRight, viewTop, width, viewBottom, color, zLevel);
			drawRect(0, viewBottom, width, height, color, zLevel);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.11F);
			int textureX = 0;
			Minecraft.getMinecraft().renderEngine
					.bindTexture(Reference.forMoreMystcraft("textures/gui/maintainer_suit.png"));
			drawTexturedRect(viewLeft, viewTop, textureX, 0, 192, 128, zLevel);
			int dim = Minecraft.getMinecraft().player.dimension;
			int topDelta = 0;
			for (int i = 0; i < this.effectsCode.length; i++) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				textureX = 0;
				color = Color.DARK_GRAY.getRGB();
				if (PotionListHandler.getPotionsList().containsKey(dim)) {
					if (PotionListHandler.getPotionsList().get(dim).contains(this.effectsCode[i])) {
						textureX = 16;
						color = Color.WHITE.getRGB();
					}
				} else {
					textureX = 0;
					color = Color.DARK_GRAY.getRGB();
				}
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				Minecraft.getMinecraft().renderEngine
						.bindTexture(Reference.forMoreMystcraft("textures/gui/maintainer_suit.png"));
				drawTexturedRect(viewRight, viewTop + topDelta, textureX, 128, 16, 16, zLevel);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				super.drawString(Minecraft.getMinecraft().fontRenderer, this.effectsName[i], viewRight + 16,
						viewTop + topDelta + 4, color);
				topDelta += 16;
			}
			GlStateManager.popMatrix();
		}
		// Re-binds original texture so the HUD renders correctly.
		Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);

	}

	/**
	 * Draws a solid-color rectangle on the screen at the specified z-level. Copied
	 * from Minecraft's Gui class to allow for z-level manipulation.
	 * 
	 * @param left   The origin x-value.
	 * @param top    The origin y-value.
	 * @param right  The destination x-value.
	 * @param bottom The destination y-value.
	 * @param color  The color.
	 * @param zLevel The z-level at which the rectangle will be drawn.
	 */
	public void drawRect(int left, int top, int right, int bottom, int color, double zLevel) {
		if (left < right) {
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			int j = top;
			top = bottom;
			bottom = j;
		}

		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.color(red, green, blue, alpha);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) left, (double) bottom, zLevel).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, zLevel).endVertex();
		bufferbuilder.pos((double) right, (double) top, zLevel).endVertex();
		bufferbuilder.pos((double) left, (double) top, zLevel).endVertex();
		tessellator.draw();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
	}

	/**
	 * Draws a textured rectangle on the screen at the specified z-level. Copied
	 * from Minecraft's Gui class to allow for z-level manipulation.
	 * 
	 * @param x        The origin x-value on-screen.
	 * @param y        The origin y-value on-screen.
	 * @param textureX The origin x-value in the texture.
	 * @param textureY The origin y-value in the texture.
	 * @param width    The width of the final texture.
	 * @param height   The height of the final texture.
	 * @param zLevel   The z-level at which the rectangle will be drawn.
	 */
	public void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, double z) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), z)
				.tex((double) ((float) (textureX + 0) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), z)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), z)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), z)
				.tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		tessellator.draw();
	}

}