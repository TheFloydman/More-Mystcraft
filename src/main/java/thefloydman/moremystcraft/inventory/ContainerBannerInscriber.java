package thefloydman.moremystcraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thefloydman.moremystcraft.tileentity.TileEntityBannerInscriber;

public class ContainerBannerInscriber extends Container {

	public TileEntityBannerInscriber tileEntity;
	InventoryPlayer inventory;
	NBTTagCompound nbt;

	public ContainerBannerInscriber(InventoryPlayer playerInv, TileEntityBannerInscriber controller) {

		this.tileEntity = controller;
		this.inventory = playerInv;

		// Add player inventory slots.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 150));
		}

		addSlotToContainer(new SlotDye(controller, Slots.DYE.ordinal(), 8, 33));
		addSlotToContainer(new SlotInkVial(controller, Slots.INK.ordinal(), 36, 33));
		addSlotToContainer(new SlotGlassBottle(controller, Slots.BOTTLE.ordinal(), 36, 61));
		addSlotToContainer(new SlotPage(controller, Slots.PAGE.ordinal(), 64, 33));
		addSlotToContainer(new SlotBanner(controller, Slots.BANNER.ordinal(), 92, 33));
		addSlotToContainer(new SlotBannerInscriberOutput(controller, Slots.OUTPUT_0.ordinal(), 151, 0));
		addSlotToContainer(new SlotBannerInscriberOutput(controller, Slots.OUTPUT_1.ordinal(), 151, 22));
		addSlotToContainer(new SlotBannerInscriberOutput(controller, Slots.OUTPUT_2.ordinal(), 151, 44));
		addSlotToContainer(new SlotBannerInscriberOutput(controller, Slots.OUTPUT_3.ordinal(), 151, 66));

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	public enum Slots {
		DYE, INK, BOTTLE, PAGE, BANNER, OUTPUT_0, OUTPUT_1, OUTPUT_2, OUTPUT_3
	}

}
