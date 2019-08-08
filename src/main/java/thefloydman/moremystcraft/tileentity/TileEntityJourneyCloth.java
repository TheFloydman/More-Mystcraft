package thefloydman.moremystcraft.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public class TileEntityJourneyCloth extends TileEntity implements IItemHandler {

	protected ItemStack cloth = ItemStack.EMPTY;

	public void setCloth(ItemStack stack) {
		this.cloth = stack;
		this.markDirty();
	}

	public ItemStack getCloth() {
		return this.cloth;
	}

	public void clearCloth() {
		this.setCloth(ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return super.writeToNBT(nbt);
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.cloth;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		this.setCloth(stack);
		return this.getCloth();
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack stack = this.getCloth();
		this.setCloth(ItemStack.EMPTY);
		return stack;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

}