package thefloydman.moremystcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import thefloydman.moremystcraft.item.ItemJourneyCloth;

public class TileEntitySingleItem extends TileEntity implements IInventory {

	protected ItemStack item = ItemStack.EMPTY;

	public void setItem(ItemStack stack) {
		this.item = stack;
		this.markDirty();
	}

	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, list);
		this.item = list.get(0);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
		list.set(0, this.getItem());
		ItemStackHelper.saveAllItems(nbt, list);
		return super.writeToNBT(nbt);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.getItem();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return !this.item.equals(ItemStack.EMPTY);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return this.removeStackFromSlot(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.getItem();
		this.setItem(ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.item = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.getItem() instanceof ItemJourneyCloth) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.item = ItemStack.EMPTY;
	}

}