package thefloydman.moremystcraft.client.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.data.Assets;
import com.xcompwiz.mystcraft.data.ModItems;

import thefloydman.moremystcraft.block.BlockUnstableBookReceptacle;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;

public class RenderUnstableBookReceptacle extends TileEntitySpecialRenderer<TileEntityUnstableBookReceptacle> {
	private ModelBook bookmodel;

	public RenderUnstableBookReceptacle() {
		this.bookmodel = new ModelBook();
	}

	public void render(final TileEntityUnstableBookReceptacle te, double x, final double y, double z,
			final float partialTicks, final int destroyStage, final float alpha) {
		x += 0.5;
		z += 0.5;
		final ItemStack display = te.getDisplayItem();
		if (display.isEmpty()) {
			return;
		}
		if (display.getItem() == ModItems.agebook) {
			this.bindTexture(Assets.Entities.agebook);
		} else if (display.getItem() == ModItems.linkbook) {
			this.bindTexture(Assets.Entities.linkbook);
		} else {
			this.bindTexture(Assets.Entities.linkbook);
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 0.5, z);
		final IBlockState state = MoreMystcraftBlocks.UNSTABLE_RECEPTACLE.getStateFromMeta(te.getBlockMetadata());
		final EnumFacing rotation = (EnumFacing) state.getValue((IProperty) BlockUnstableBookReceptacle.ROTATION);
		switch (rotation) {
		case UP: {
			GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
			GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
			break;
		}
		case NORTH: {
			GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
			break;
		}
		case SOUTH: {
			GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
		}
		case EAST: {
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
			break;
		}
		default:
			break;
		}
		GlStateManager.rotate((float) te.getPitch(), 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate((float) te.getYaw(), 0.0f, 1.0f, 0.0f);
		GlStateManager.scale(0.8f, 0.8f, 0.8f);
		this.bookmodel.render((Entity) null, 0.1f, 0.1f, 0.1f, 0.005f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
		if (Mystcraft.renderlabels && Mystcraft.serverLabels) {
			this.renderLabel(te, te.getBookTitle(), x, y + 1.25, z, 25);
		}
	}

	protected void renderLabel(final TileEntity entity, final String s, final double x, final double y, final double z,
			final int maxDst) {
		if (s == null || s.isEmpty()) {
			return;
		}
		double f = entity.getDistanceSq(this.rendererDispatcher.entityX, this.rendererDispatcher.entityY,
				this.rendererDispatcher.entityZ);
		f = MathHelper.sqrt(f);
		if (f > maxDst) {
			return;
		}
		final FontRenderer fontrenderer = this.getFontRenderer();
		final float f2 = 1.6f;
		final float f3 = 0.01666667f * f2;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-this.rendererDispatcher.entityYaw, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(this.rendererDispatcher.entityPitch, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(-f3, -f3, f3);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		final Tessellator tes = Tessellator.getInstance();
		final BufferBuilder vb = tes.getBuffer();
		final byte byte0 = 0;
		final int j = fontrenderer.getStringWidth(s) / 2;
		vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vb.pos((double) (-j - 1), (double) (-1 + byte0), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
		vb.pos((double) (-j - 1), (double) (8 + byte0), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
		vb.pos((double) (j + 1), (double) (8 + byte0), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
		vb.pos((double) (j + 1), (double) (-1 + byte0), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
		tes.draw();
		GlStateManager.enableTexture2D();
		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, (int) byte0, 553648127);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, (int) byte0, -1);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}
}