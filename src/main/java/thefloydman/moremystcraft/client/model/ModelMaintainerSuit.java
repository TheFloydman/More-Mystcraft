package thefloydman.moremystcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;

/**
 * ModelMaintainerSuit - Floydman Created using Tabula 7.0.0
 */
public class ModelMaintainerSuit extends ModelBase {
	public ModelRenderer cube;

	public ModelMaintainerSuit() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.cube = new ModelRenderer(this, 0, 0);
		this.cube.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.cube.addBox(-8.0F, 0.0F, -8.0F, 16, 16, 16, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
		java.util.Collection<Entity> passengers = entity.getPassengers();
		boolean playerIsPassenger = false;
		for (Entity pas : passengers) {
			if (pas.equals(Minecraft.getMinecraft().player)) {
				playerIsPassenger = true;
				break;
			}
		}
		if (!playerIsPassenger || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
			this.cube.render(scale);
		}
	}
}
