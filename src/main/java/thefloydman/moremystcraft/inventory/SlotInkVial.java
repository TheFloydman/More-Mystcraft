package thefloydman.moremystcraft.inventory;

import com.xcompwiz.mystcraft.data.ModItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInkVial extends SlotBannerInscriberInput {

	public SlotInkVial(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem().equals(ModItems.inkvial);
	}

}
