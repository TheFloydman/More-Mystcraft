package thefloydman.moremystcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPotionDummy extends ModelBase {

	public ModelRenderer cube;

	public ModelPotionDummy() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.cube = new ModelRenderer(this, 0, 0);
		this.cube.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.cube.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 1.0F);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
	}

}