package thefloydman.moremystcraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.MathHelper;
import thefloydman.moremystcraft.capability.ICapabilityHub;
import thefloydman.moremystcraft.capability.ProviderCapabilityHub;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;

public class ContainerJourneyHub extends Container {

	public TileEntitySingleItem tileEntity;

	public ContainerJourneyHub(TileEntitySingleItem controller) {

		this.tileEntity = controller;

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean enchantItem(EntityPlayer player, int id) {
		ICapabilityHub cap = this.tileEntity.getItem().getCapability(ProviderCapabilityHub.HUB, null);
		if (id == -1) {
			cap.setPerPlayer(!cap.getPerPlayer());
		} else if (id < -1) {
			cap.removeCloth(cap.getUUID(MathHelper.abs(id + 2)));
		}
		return true;
	}

}
