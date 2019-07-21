package thefloydman.moremystcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;

public class TileEntityNexusController extends TileEntity implements ISidedInventory {

	public NonNullList<ItemStack> bookList;
	protected int bookCount;
	public int inventorySize;

	public TileEntityNexusController() {
		this.inventorySize = 2 + 8;
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
		this.bookCount = 0;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.bookList);
		bookCount = 0;
		for (ItemStack stack : this.bookList) {
			if (!stack.isEmpty()) {
				bookCount++;
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		ItemStackHelper.saveAllItems(nbt, this.bookList);
		return nbt;
	}

	public boolean addBook(ItemStack stack) {
		for (int i = 2; i < this.inventorySize; i++) {
			if (this.bookList.get(i).isEmpty()) {
				this.bookList.set(i, stack);
				 this.getWorld().notifyBlockUpdate(this.getPos(),
				 MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(),
				 MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(), 3);
				bookCount++;
				this.removeStackFromSlot(0);
				this.markDirty();
				return true;
			}
		}
		return false;
	}
	
	public void removeBook(int id) {
		for (int i = id; i < this.bookList.size() - 1; i++) {
			this.bookList.set(i, this.bookList.get(i + 1));
		}
		this.bookList.set(this.bookList.size() - 1, ItemStack.EMPTY);
		this.bookCount--;
	}

	public int getBookCount() {
		return this.bookCount;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStackHelper.saveAllItems(nbt, this.bookList);
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.getNbtCompound();
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.bookList);
		bookCount = 0;
		for (ItemStack stack : this.bookList) {
			if (!stack.isEmpty()) {
				bookCount++;
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return this.inventorySize;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.bookList) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.bookList.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.bookList, index, count);
		if (!itemstack.isEmpty()) {
			this.markDirty();
		}
		return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemstack = this.bookList.get(index);
		if (itemstack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.bookList.set(index, ItemStack.EMPTY);
			return itemstack;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.bookList.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
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
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

}