package thefloydman.moremystcraft.tileentity;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityNexusController extends TileEntity implements ISidedInventory {

	protected NonNullList<ItemStack> bookList;
	protected int bookCount;
	protected int inventorySize;
	protected String query = null;

	public TileEntityNexusController() {
		this.inventorySize = 2 + 128;
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
		this.bookCount = 0;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(books, this.bookList);
			bookCount = 0;
			for (ItemStack stack : this.bookList) {
				if (!stack.isEmpty()) {
					bookCount++;
				}
			}
		} else {
			this.bookList = null;
		}
		if (nbt.hasKey("query")) {
			this.query = nbt.getString("query");
		} else {
			this.query = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (this.bookList != null) {
			NBTTagCompound books = new NBTTagCompound();
			ItemStackHelper.saveAllItems(books, this.bookList);
			nbt.setTag("books", books);
		}
		if (this.query != null) {
			nbt.setString("query", this.query);
		}
		return nbt;
	}

	public boolean addBook(ItemStack stack) {
		for (int i = 2; i < this.inventorySize; i++) {
			if (this.bookList.get(i).isEmpty()) {
				this.bookList.set(i, stack);
				bookCount++;
				this.removeStackFromSlot(0);
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
		this.markDirty();
	}

	public int getBookCount() {
		return this.bookCount;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		super.getUpdatePacket();
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.bookList != null) {
			NBTTagCompound books = new NBTTagCompound();
			ItemStackHelper.saveAllItems(books, this.bookList);
			nbt.setTag("books", books);
		}
		if (this.query != null) {
			nbt.setString("query", this.query);
		}
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		NBTTagCompound nbt = pkt.getNbtCompound();
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(books, this.bookList);
			bookCount = 0;
			for (ItemStack stack : this.bookList) {
				if (!stack.isEmpty()) {
					bookCount++;
				}
			}
		} else {
			this.bookList = null;
		}
		if (nbt.hasKey("query")) {
			this.query = nbt.getString("query");
		} else {
			this.query = null;
		}
		this.markDirty();
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
			this.markDirty();
			return itemstack;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.bookList.set(index, stack);
		this.markDirty();
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
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		this.markDirty();
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

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return false;
	}

	@Nonnull
	public String getQuery() {
		return (this.query == null) ? "" : this.query;
	}

	public void setQuery(String str) {
		this.query = str;
		this.markDirty();
	}

	public List<ItemStack> getBookList() {
		return this.bookList;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		super.getUpdateTag();
		NBTTagCompound nbt = this.getTileData();
		if (this.bookList != null) {
			NBTTagCompound books = new NBTTagCompound();
			ItemStackHelper.saveAllItems(books, this.bookList);
			nbt.setTag("books", books);
		}
		if (this.query != null) {
			nbt.setString("query", this.query);
		}
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		NBTTagCompound nbt = new NBTTagCompound();
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(books, this.bookList);
			bookCount = 0;
			for (ItemStack stack : this.bookList) {
				if (!stack.isEmpty()) {
					bookCount++;
				}
			}
		} else {
			this.bookList = null;
		}
		if (nbt.hasKey("query")) {
			this.query = nbt.getString("query");
		} else {
			this.query = null;
		}
	}

}