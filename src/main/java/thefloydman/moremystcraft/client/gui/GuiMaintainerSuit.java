package thefloydman.moremystcraft.client.gui;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.init.MoreMystcraftEntityEntries;
import thefloydman.moremystcraft.util.Reference;

@EventBusSubscriber
public class GuiMaintainerSuit extends Gui {

	protected static int viewWidth = 192;
	protected static int viewHeight = 128;
	protected static int viewTop;
	protected static int viewBottom;
	protected static int viewLeft;
	protected static int viewRight;
	protected static int width;
	protected static int height;

	protected static double zLevel = -1000.0D;

	@SubscribeEvent
	public static void renderHPOverlay(RenderGameOverlayEvent.Pre event) {
		boolean ridingMaintainerSuit = false;
		EntityPlayer player = Minecraft.getMinecraft().player;
		Entity entity = player.getRidingEntity();
		if (player.isRiding() && entity instanceof EntityMaintainerSuit) {
			ridingMaintainerSuit = true;
		}
		if (ridingMaintainerSuit) {
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
			color = Color.HSBtoRGB(0.59444444444444444444444444444444F, 0.27F, 1.0F);
			float alpha = 0.105F;
			drawGlass(viewLeft, viewTop, viewLeft + 4, viewBottom);
			drawGlass(viewLeft + 4, viewTop, viewRight - 4, viewTop + 4);
			drawGlass(viewLeft + 4, viewBottom - 4, viewRight - 4, viewBottom);
			drawGlass(viewRight - 4, viewTop, viewRight, viewBottom);
			drawPixel(2, 4);
			drawPixel(3, 3);
			drawPixel(4, 2);
			drawPixel(18, 4);
			drawPixel(19, 3);
			drawPixel(20, 2);
			drawPixel(34, 4);
			drawPixel(35, 3);
			drawPixel(36, 2);
			drawPixel(2, 20);
			drawPixel(3, 19);
			drawPixel(4, 18);
			drawPixel(18, 20);
			drawPixel(19, 19);
			drawPixel(20, 18);
			drawPixel(34, 20);
			drawPixel(35, 19);
			drawPixel(36, 18);
			drawPixel(12, 13);
			drawPixel(13, 12);
			drawPixel(28, 13);
			drawPixel(29, 12);
			drawPixel(44, 13);
			drawPixel(45, 12);
			drawPixel(12, 29);
			drawPixel(13, 28);
			drawPixel(28, 29);
			drawPixel(29, 28);
			drawPixel(44, 29);
			drawPixel(45, 28);
		}
	}
	
	public static void drawPixel(int x, int y) {
		int left = viewLeft + (x * 4);
		int top = viewTop + (y * 4);
		int right = left + 4;
		int bottom = top + 4;
		drawGlass(left, top, right, bottom);
	}

	public static void drawGlass(int left, int top, int right, int bottom) {
		int color = Color.HSBtoRGB(0.59444444444444444444444444444444F, 0.27F, 1.0F);
		float alpha = 0.105F;
		drawRect(left, top, right, bottom, color, zLevel, alpha);
	}

	public static void drawRect(int left, int top, int right, int bottom, int color, double zLevel) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		drawRect(left, top, right, bottom, color, zLevel, alpha);
	}

	/**
	 * Draws a solid-color rectangle on the screen at the specified z-level. Copied
	 * from Minecraft's Gui class to allow for z-level manipulation.
	 * 
	 * @param left
	 *            The origin x-value.
	 * @param top
	 *            The origin y-value.
	 * @param right
	 *            The destination x-value.
	 * @param bottom
	 *            The destination y-value.
	 * @param color
	 *            The color.
	 * @param zLevel
	 *            The z-level at which the rectangle will be drawn.
	 */
	public static void drawRect(int left, int top, int right, int bottom, int color, double zLevel, float alpha) {
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
		GlStateManager.disableBlend();
	}

	public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), zLevel)
				.tex((double) ((float) (textureX + 0) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), zLevel)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), zLevel)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), zLevel)
				.tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		tessellator.draw();
	}

}