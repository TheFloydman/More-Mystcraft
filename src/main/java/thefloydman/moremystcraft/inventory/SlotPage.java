package thefloydman.moremystcraft.inventory;

import java.util.Arrays;
import java.util.List;

import com.xcompwiz.mystcraft.api.word.WordData;
import com.xcompwiz.mystcraft.data.ModItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.tileentity.TileEntityBannerInscriber;

public class SlotPage extends SlotBannerInscriberInput {

	public SlotPage(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack.getItem().equals(ModItems.page)) {
			if (!MoreMystcraft.proxy.pageApi.hasLinkPanel(stack)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
