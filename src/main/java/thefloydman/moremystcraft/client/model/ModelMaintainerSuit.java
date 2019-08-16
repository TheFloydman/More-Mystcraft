package thefloydman.moremystcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelMaintainerSuit extends ModelBiped {

	public ModelMaintainerSuit() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.bipedRightLeg = new ModelRenderer(this, 40, 0);
		this.bipedRightLeg.setRotationPoint(-2.4F, 9.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.5F, 0.0F, -2.5F, 5, 15, 5, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 40, 0);
		this.bipedLeftLeg.setRotationPoint(2.4F, 9.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.5F, 0.0F, -2.5F, 5, 15, 5, 0.0F);
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.bipedHead.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 0, 40);
		this.bipedLeftArm.setRotationPoint(6.25F, -3.5F, 0.0F);
		this.bipedLeftArm.addBox(-1.25F, -2.5F, -2.5F, 5, 15, 5, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 0, 40);
		this.bipedRightArm.setRotationPoint(-6.25F, -3.5F, 0.0F);
		this.bipedRightArm.addBox(-3.75F, -2.5F, -2.5F, 5, 15, 5, 0.0F);
		this.bipedBody = new ModelRenderer(this, 0, 20);
		this.bipedBody.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.bipedBody.addBox(-5.0F, 0.0F, -2.5F, 10, 15, 5, 0.0F);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		java.util.Collection<Entity> passengers = entityIn.getPassengers();
		boolean playerIsPassenger = false;
		for (Entity pas : passengers) {
			if (pas.equals(Minecraft.getMinecraft().player)) {
				playerIsPassenger = true;
				break;
			}
		}
		if (!playerIsPassenger || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
			this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
			GlStateManager.pushMatrix();

			this.bipedHead.render(scale);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			GlStateManager.popMatrix();
		}

	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;

		this.bipedBody.rotateAngleY = 0.0F;
		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedRightArm.rotationPointX = -6.25F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointX = 6.25F;
		float f = 1.0F;

		if (f < 1.0F) {
			f = 1.0F;
		}

		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount
				* 0.5F / f;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount
				/ f;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		this.bipedRightLeg.rotateAngleZ = 0.0F;
		this.bipedLeftLeg.rotateAngleZ = 0.0F;

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;

		this.bipedLeftArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleY = 0.0F;

		if (this.swingProgress > 0.0F) {
			EnumHandSide enumhandside = this.getMainHand(entityIn);
			ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
			float f1 = this.swingProgress;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 6.25F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 6.25F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 6.25F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 6.25F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f1 = 1.0F - this.swingProgress;
			f1 = f1 * f1;
			f1 = f1 * f1;
			f1 = 1.0F - f1;
			float f2 = MathHelper.sin(f1 * (float) Math.PI);
			float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F)
					* 0.75F;
			modelrenderer.rotateAngleX = (float) ((double) modelrenderer.rotateAngleX
					- ((double) f2 * 1.2D + (double) f3));
			modelrenderer.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
		}

		this.bipedBody.rotateAngleX = 0.0F;
		this.bipedRightLeg.rotationPointZ = 0.1F;
		this.bipedLeftLeg.rotationPointZ = 0.1F;
		this.bipedRightLeg.rotationPointY = 9.0F;
		this.bipedLeftLeg.rotationPointY = 9.0F;
		this.bipedHead.rotationPointY = -6.0F;

	}

}
