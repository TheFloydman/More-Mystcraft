package thefloydman.moremystcraft.client.render;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.client.model.ModelMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;

public class RenderMaintainerSuit extends RenderLiving<EntityMaintainerSuit> {

	public RenderMaintainerSuit(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelMaintainerSuit(), 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMaintainerSuit entity) {
		return Reference.forMoreMystcraft("textures/entity/maintainer_suit/maintainer_suit.png");
	}

}
