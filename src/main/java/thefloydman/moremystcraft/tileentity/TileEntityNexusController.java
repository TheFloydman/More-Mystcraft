package thefloydman.moremystcraft.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.xcompwiz.mystcraft.item.ItemLinking;

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
import scala.actors.threadpool.Arrays;
import thefloydman.moremystcraft.block.BlockNexusController;

public class TileEntityNexusController extends TileEntity implements ISidedInventory {

	protected NonNullList<ItemStack> bookList;
	protected List<ItemStack> shortList;
	private List<Integer> filteredList;
	protected int inventorySize;
	protected String query = null;
	List<BlockPos> storageBlocks;

	public TileEntityNexusController() {
		this.inventorySize = 2 + 4;
		this.bookList = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		this.shortList = new ArrayList<ItemStack>();
		this.filteredList = new ArrayList<Integer>();
		this.storageBlocks = new ArrayList<BlockPos>();
	}

	public static int distanceWalked(final BlockPos startBlock, final BlockPos endBlock) {
		return Math.abs(startBlock.getX() - endBlock.getX()) + Math.abs(startBlock.getY() - endBlock.getY())
				+ Math.abs(startBlock.getZ() - endBlock.getZ());
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		Map<BlockPos, Integer> map = BlockNexusController.getConnectedNexusBlocks(this.getWorld(), this.getPos(), 1);
		int totalSize = (Collections.frequency(map.values(), 0) * 8) + this.getSizeInventory();
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(totalSize, ItemStack.EMPTY);
			System.out.println("READ:" + this.getBookList().size());
			ItemStackHelper.loadAllItems(books, this.bookList);
			this.shortList = new ArrayList<ItemStack>();
			for (int i = 2; i < this.bookList.size(); i++) {
				if (!this.bookList.get(i).isEmpty()) {
					this.shortList.add(this.bookList.get(i));
				} else {
					break;
				}
			}
		} else {
			this.bookList = null;
		}
		for (Entry<BlockPos, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 0) {
				TileEntityNexusStorage tileEntity = (TileEntityNexusStorage) this.getWorld()
						.getTileEntity(entry.getKey());
				if (tileEntity == null) {
					continue;
				}
				List<ItemStack> list = tileEntity.getBookList();
				for (ItemStack stack : list) {
					if (!stack.isEmpty()) {
						this.addBook(stack);
					}
				}
			}
		}
		if (nbt.hasKey("query")) {
			this.query = nbt.getString("query");
		} else {
			this.query = null;
		}
		this.filterBooks(this.query);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		Map<BlockPos, Integer> map = BlockNexusController.getConnectedNexusBlocks(this.getWorld(), this.getPos(), 1);
		int totalSize = (Collections.frequency(map.values(), 0) * 8) + this.getSizeInventory();
		NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < this.getSizeInventory(); i++) {
			list.set(i, this.bookList.get(i));
		}
		if (this.bookList != null) {
			NBTTagCompound books = new NBTTagCompound();
			ItemStackHelper.saveAllItems(books, list);
			nbt.setTag("books", books);
		}
		map.remove(this.getPos());
		int count = 0;
		for (int i = this.getSizeInventory(); i < this.getBookList().size(); i++) {
			BlockPos blockPos = map.entrySet().stream().findFirst().get().getKey();
			TileEntity tileEntity = this.getWorld().getTileEntity(blockPos);
			if (tileEntity instanceof TileEntityNexusStorage) {
				TileEntityNexusStorage tileEntityStorage = (TileEntityNexusStorage) this.getWorld().getTileEntity(blockPos);
				tileEntityStorage.clearBookList();
				tileEntityStorage.addBook(this.getBookList().get(i));
			}
			count++;
			if (count > 7) {
				map.remove(blockPos);
				count = 0;
			}
		}
		if (this.query != null) {
			nbt.setString("query", this.query);
		}
		return nbt;
	}

	public boolean addBook(ItemStack stack) {
		for (int i = 2; i < this.getBookList().size(); i++) {
			if (this.bookList.get(i).isEmpty()) {
				this.bookList.set(i, stack);
				this.shortList.add(stack);
				this.sortBookList();
				this.removeStackFromSlot(0);
				this.filterBooks(this.query);
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
		this.shortList.remove(id - 2);
		this.filterBooks(this.query);
		this.markDirty();
	}

	public int getBookCount() {
		return this.getShortList().size();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
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
		if (index == 0 && stack.getItem() instanceof ItemLinking) {
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
		if (Arrays.asList(EnumFacing.HORIZONTALS).contains(side)) {
			return new int[] { 0 };
		}
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (index == 0) {
			if (Arrays.asList(EnumFacing.HORIZONTALS).contains(direction)) {
				if (itemStackIn.getItem() instanceof ItemLinking) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (oldState.getBlock().equals(newState.getBlock())) {
			return false;
		}
		return true;
	}

	@Nonnull
	public String getQuery() {
		return (this.query == null) ? "" : this.query;
	}

	public void setQuery(String str) {
		this.query = str;
		this.markDirty();
	}

	public NonNullList<ItemStack> getBookList() {
		return this.bookList;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(super.getUpdateTag());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.readFromNBT(tag);
	}

	protected void sortBookList() {
		Comparator<ItemStack> compareByDisplayName = (ItemStack o1, ItemStack o2) -> ((ItemLinking) o1.getItem())
				.getLinkInfo(o1).getDisplayName().toLowerCase()
				.compareTo(((ItemLinking) o2.getItem()).getLinkInfo(o2).getDisplayName().toLowerCase());
		shortList.sort(compareByDisplayName);
		for (int i = 0; i < shortList.size(); i++) {
			this.bookList.set(i + 2, shortList.get(i));
		}
		this.markDirty();
	}

	public List<ItemStack> getShortList() {
		return this.shortList;
	}

	public void filterBooks(String text) {
		this.filteredList = new ArrayList<Integer>();
		for (int i = 0; i < this.getShortList().size(); i++) {
			this.filteredList.add(i + 2);
		}
		if (text == null || text.trim().isEmpty()) {
			return;
		}
		List<String> displayNames = new ArrayList<String>();
		for (ItemStack stack : this.shortList) {
			displayNames.add(((ItemLinking) stack.getItem()).getLinkInfo(stack).getDisplayName());
		}
		this.filteredList = new ArrayList<Integer>();
		for (int i = 0; i < this.getShortList().size(); i++) {
			this.filteredList.add(i + 2);
		}
		for (int i = 0; i < displayNames.size(); i++) {
			if (!displayNames.get(i).toLowerCase().contains(text.toLowerCase())) {
				int index = filteredList.indexOf(i + 2);
				filteredList.remove(index);
			}
		}
		this.markDirty();
	}

	public List<Integer> getFilteredBookList() {
		return this.filteredList;
	}

	@Override
	public void onLoad() {
		if (!this.getWorld().isRemote) {
			Map<BlockPos, Integer> map = BlockNexusController.getConnectedNexusBlocks(this.getWorld(), this.getPos(),
					1);
			int totalSize = (Collections.frequency(map.values(), 0) * 8) + this.getSizeInventory();
			this.bookList = NonNullList.<ItemStack>withSize(totalSize, ItemStack.EMPTY);
			System.out.println("LOAD: " + this.getBookList().size());
			this.getWorld().notifyBlockUpdate(this.getPos(), this.getWorld().getBlockState(this.getPos()),
					this.getWorld().getBlockState(this.getPos()), 7);
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