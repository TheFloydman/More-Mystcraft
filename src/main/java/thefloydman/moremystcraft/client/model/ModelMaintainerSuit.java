package thefloydman.moremystcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelMaintainerSuit - Floydman Created using Tabula 7.0.0
 */
public class ModelMaintainerSuit extends ModelBase {

	public ModelRenderer head;
	public ModelRenderer torso;
	public ModelRenderer left_leg;
	public ModelRenderer right_leg;
	public ModelRenderer left_arm;
	public ModelRenderer right_arm;

	public ModelMaintainerSuit() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.right_leg = new ModelRenderer(this, 40, 0);
		this.right_leg.setRotationPoint(-2.4F, 9.0F, 0.0F);
		this.right_leg.addBox(-2.5F, 0.0F, -2.5F, 5, 15, 5, 0.0F);
		this.left_leg = new ModelRenderer(this, 40, 0);
		this.left_leg.setRotationPoint(2.4F, 9.0F, 0.0F);
		this.left_leg.addBox(-2.5F, 0.0F, -2.5F, 5, 15, 5, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.head.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, 0.0F);
		this.left_arm = new ModelRenderer(this, 0, 40);
		this.left_arm.setRotationPoint(6.25F, -3.5F, 0.0F);
		this.left_arm.addBox(-1.25F, -2.5F, -2.5F, 5, 15, 5, 0.0F);
		this.right_arm = new ModelRenderer(this, 0, 40);
		this.right_arm.setRotationPoint(-6.25F, -3.5F, 0.0F);
		this.right_arm.addBox(-3.75F, -2.5F, -2.5F, 5, 15, 5, 0.0F);
		this.torso = new ModelRenderer(this, 0, 20);
		this.torso.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.torso.addBox(-5.0F, 0.0F, -2.5F, 10, 15, 5, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		java.util.Collection<Entity> passengers = entity.getPassengers();
		boolean playerIsPassenger = false;
		for (Entity pas : passengers) {
			if (pas.equals(Minecraft.getMinecraft().player)) {
				playerIsPassenger = true;
				break;
			}
		}
		if (!playerIsPassenger || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
			this.right_leg.render(f5);
			this.left_leg.render(f5);
			this.head.render(f5);
			this.left_arm.render(f5);
			this.right_arm.render(f5);
			this.torso.render(f5);
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
