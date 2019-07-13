package thefloydman.moremystcraft.inventory;

import com.xcompwiz.mystcraft.data.ModItems;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDye extends SlotBannerInscriberInput {

	public SlotDye(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem().equals(Items.DYE);
	}

}
