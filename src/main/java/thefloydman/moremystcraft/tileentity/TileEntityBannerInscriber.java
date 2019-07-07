package thefloydman.moremystcraft.tileentity;

import com.xcompwiz.mystcraft.data.ModItems;

import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityBannerInscriber extends TileEntity {

	public NonNullList<ItemStack> itemStacks;

	public TileEntityBannerInscriber() {
		itemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.itemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.itemStacks);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		ItemStackHelper.saveAllItems(nbt, this.itemStacks);
		return nbt;
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		switch (index) {
		case 0:
			if (stack.getItem().equals(ModItems.inkvial)) {
				return true;
			}
			break;
		case 1:
			break;
		case 2:
			if (stack.getItem().equals(ModItems.page)) {
				return true;
			}
			break;
		case 3:
			if (stack.getItem().equals(Items.BANNER)) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
}