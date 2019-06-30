package thefloydman.moremystcraft.client.render;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.client.model.ModelMaintainerSuit;
import thefloydman.moremystcraft.client.model.ModelPotionDummy;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityPotionDummy;

public class RenderPotionDummy extends RenderLiving<EntityPotionDummy> {

	public RenderPotionDummy(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelPotionDummy(), 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPotionDummy entity) {
		return Reference.forMoreMystcraft("textures/entity/maintainer_suit.png");
	}

}
