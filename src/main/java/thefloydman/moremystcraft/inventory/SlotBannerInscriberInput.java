package thefloydman.moremystcraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thefloydman.moremystcraft.tileentity.TileEntityBannerInscriber;

public class SlotBannerInscriberInput extends Slot {

	public SlotBannerInscriberInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (this.inventory instanceof TileEntityBannerInscriber) {
			((TileEntityBannerInscriber) this.inventory).updateSlots();
		}
	}
	
}
