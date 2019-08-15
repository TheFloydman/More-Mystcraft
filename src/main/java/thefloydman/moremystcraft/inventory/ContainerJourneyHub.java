package thefloydman.moremystcraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
		if (cap != null) {
			if (id < 0) {
				cap.setPerPlayer(!cap.getPerPlayer());
			} else if (id < 16) {
				cap.removeCloth(cap.getUUID(id));
			}
			player.getEntityWorld().notifyBlockUpdate(this.tileEntity.getPos(),
					this.tileEntity.getBlockType().getDefaultState(), this.tileEntity.getBlockType().getDefaultState(),
					3);
			return true;
		}
		return false;
	}

}