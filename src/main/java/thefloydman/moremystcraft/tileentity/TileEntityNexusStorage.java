package thefloydman.moremystcraft.tileentity;

import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.block.BlockNexusController;

public class TileEntityNexusStorage extends TileEntity implements IInventory {

	protected NonNullList<ItemStack> bookList;
	protected int inventorySize;

	public TileEntityNexusStorage() {
		this.inventorySize = 8;
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
	}

	public void clearBookList() {
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
		this.markDirty();
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(books, this.bookList);
		} else {
			this.bookList = null;
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
		return nbt;
	}

	public boolean addBook(ItemStack stack) {
		for (int i = 0; i < this.inventorySize; i++) {
			if (this.bookList.get(i).isEmpty()) {
				this.bookList.set(i, stack);
				this.markDirty();
				if (!this.getWorld().isRemote) {
					this.getWorld().notifyBlockUpdate(this.getPos(), this.getWorld().getBlockState(this.getPos()),
							this.getWorld().getBlockState(this.getPos()), 3);
				}
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
		this.markDirty();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		super.getUpdatePacket();
		NBTTagCompound nbt = new NBTTagCompound();
		nbt = this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.getNbtCompound();
		this.readFromNBT(nbt);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (oldState.getBlock().equals(newState.getBlock())) {
			return false;
		}
		return true;
	}

	public NonNullList<ItemStack> getBookList() {
		if (this.bookList != null) {
			return this.bookList;
		}
		return NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(super.getUpdateTag());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		NBTTagCompound nbt = new NBTTagCompound();
		this.readFromNBT(nbt);
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
		return this.getBookList().get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
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
	public void onLoad() {
		if (this.getWorld().isRemote) {
			return;
		}
		Map<BlockPos, Integer> nexusBlocks = BlockNexusController.getConnectedNexusBlocks(this.getWorld(),
				this.getPos(), 0);
		BlockPos controllerPos = this.getKey(nexusBlocks, 1);
		if (controllerPos != null) {
			this.getWorld().notifyBlockUpdate(controllerPos, this.getWorld().getBlockState(controllerPos),
					this.getWorld().getBlockState(controllerPos), 3);
		}
	}

	/*
	 * Source as of 26 July 2019: https://www.baeldung.com/java-map-key-from-value
	 */
	protected <K, V> K getKey(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

}