package thefloydman.moremystcraft.inventory;

import com.xcompwiz.mystcraft.data.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thefloydman.moremystcraft.inventory.ContainerBannerInscriber.Slots;
import thefloydman.moremystcraft.tileentity.TileEntityBannerInscriber;

public class SlotBannerInscriberOutput extends Slot {

	public SlotBannerInscriberOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		super.onTake(thePlayer, stack);
		
		this.inventory.setInventorySlotContents(Slots.OUTPUT_0.ordinal(), ItemStack.EMPTY);
		this.inventory.setInventorySlotContents(Slots.OUTPUT_1.ordinal(), ItemStack.EMPTY);
		this.inventory.setInventorySlotContents(Slots.OUTPUT_2.ordinal(), ItemStack.EMPTY);
		this.inventory.setInventorySlotContents(Slots.OUTPUT_3.ordinal(), ItemStack.EMPTY);
		
		int dyeSize = this.inventory.getStackInSlot(Slots.DYE.ordinal()).getCount();
		ItemStack dyeStack = this.inventory.getStackInSlot(Slots.DYE.ordinal());
		dyeStack.setCount(dyeSize - 1);
		this.inventory.setInventorySlotContents(Slots.DYE.ordinal(), dyeStack);
		
		int inkSize = this.inventory.getStackInSlot(Slots.INK.ordinal()).getCount();
		ItemStack inkStack = this.inventory.getStackInSlot(Slots.INK.ordinal());
		inkStack.setCount(inkSize - 1);
		this.inventory.setInventorySlotContents(Slots.INK.ordinal(), inkStack);
		
		int bottleSize = this.inventory.getStackInSlot(Slots.BOTTLE.ordinal()).getCount();
		ItemStack bottleStack = this.inventory.getStackInSlot(Slots.BOTTLE.ordinal());
		if (bottleStack.isEmpty()) {
			bottleStack = new ItemStack(Items.GLASS_BOTTLE);
		} else {
			bottleStack.setCount(bottleSize + 1);
		}
		this.inventory.setInventorySlotContents(Slots.BOTTLE.ordinal(), bottleStack);
		
		int bannerSize = this.inventory.getStackInSlot(Slots.BANNER.ordinal()).getCount();
		ItemStack bannerStack = this.inventory.getStackInSlot(Slots.BANNER.ordinal());
		bannerStack.setCount(bannerSize - 1);
		this.inventory.setInventorySlotContents(Slots.BANNER.ordinal(), bannerStack);
		
		if (this.inventory instanceof TileEntityBannerInscriber) {
			((TileEntityBannerInscriber) this.inventory).updateSlots();
		}
		
		return stack;
	}

}
