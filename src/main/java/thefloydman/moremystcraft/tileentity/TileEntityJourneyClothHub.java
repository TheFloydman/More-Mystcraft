package thefloydman.moremystcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thefloydman.moremystcraft.item.ItemJourneyCloth;
import thefloydman.moremystcraft.item.ItemJourneyClothHub;

public class TileEntityJourneyClothHub extends TileEntity implements IInventory {

	protected ItemStack hub = ItemStack.EMPTY;

	public void setHub(ItemStack stack) {
		this.hub = stack;
		this.markDirty();
	}

	public ItemStack getHub() {
		return this.hub;
	}

	public void clearHub() {
		this.setHub(ItemStack.EMPTY);
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
	public ItemStack getStackInSlot(int slot) {
		return this.getHub();
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
		return !this.hub.equals(ItemStack.EMPTY);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return this.removeStackFromSlot(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.getHub();
		this.setHub(ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.hub = stack;
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
		if (stack.getItem() instanceof ItemJourneyClothHub) {
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
		this.hub = ItemStack.EMPTY;
	}

}