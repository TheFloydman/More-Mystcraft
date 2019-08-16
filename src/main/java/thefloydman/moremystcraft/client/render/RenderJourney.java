package thefloydman.moremystcraft.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import thefloydman.moremystcraft.block.BlockJourneyBase;
import thefloydman.moremystcraft.tileentity.TileEntityJourney;
import thefloydman.moremystcraft.util.JourneyUtils;
import thefloydman.moremystcraft.util.Reference;

public class RenderJourney extends TileEntitySpecialRenderer<TileEntityJourney> {

	@Override
	public void render(TileEntityJourney te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {

		EnumFacing facing = te.getFacing();
		double[] pos = new double[] { x, y, z };
		float[] rot = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
		switch (facing.getIndex()) {
		case 2:
			pos[2] += 0.99;
			break;
		case 3:
			rot[0] = 180.0F;
			rot[2] = 1.0F;
			pos[2] += 0.01F;
			pos[0] += 1.0F;
			break;
		case 4:
			rot[0] = 90.0F;
			rot[2] = 1.0F;
			pos[0] += 0.99F;
			pos[2] += 1.0F;
			break;
		case 5:
			rot[0] = -90.0F;
			rot[2] = 1.0F;
			pos[0] += 0.01;
			break;
		}

		float opacity = Float.valueOf(te.getTimer()) < 120.0F
				? 1.0F - (MathHelper.abs(te.getTimer() - Float.valueOf(60)) / 60.0F)
				: 0.0F;

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.translate(pos[0], pos[1], pos[2]);
		GlStateManager.rotate(rot[0], rot[1], rot[2], rot[3]);
		GlStateManager.color(1.0F, 1.0F, 1.0F, opacity);
		BlockJourneyBase block = (BlockJourneyBase) te.getBlockType();
		this.bindTexture(
				Reference.forMoreMystcraft("textures/blocks/journey_" + JourneyUtils.blockTypeToString(te.getType())
						+ "_activated_" + block.PATTERN_TYPE.name().toLowerCase() + ".png"));
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(0, 0, 0).tex(1.0, 1.0).endVertex();
		buf.pos(0, 1, 0).tex(1.0, 0.0).endVertex();
		buf.pos(1, 1, 0).tex(0.0, 0.0).endVertex();
		buf.pos(1, 0, 0).tex(0.0, 1.0).endVertex();
		tes.draw();

		GlStateManager.popMatrix();
	}

}
