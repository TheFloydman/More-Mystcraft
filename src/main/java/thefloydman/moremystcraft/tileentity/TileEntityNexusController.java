package thefloydman.moremystcraft.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.xcompwiz.mystcraft.item.ItemLinking;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import thefloydman.moremystcraft.block.BlockNexusController;

public class TileEntityNexusController extends TileEntity implements ISidedInventory {

	protected NonNullList<ItemStack> bookList;
	protected List<ItemStack> shortList;
	private List<Integer> filteredList;
	protected int inventorySize;
	protected String query = null;

	public TileEntityNexusController(World world) {
		this.updateInventorySize(world, this.getPos());
		this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
		this.shortList = new ArrayList<ItemStack>();
		this.filteredList = new ArrayList<Integer>();
	}

	public void updateInventorySize(World world, BlockPos pos) {
		Map<BlockPos, Integer> nexusBlocks = BlockNexusController.getConnectedNexusBlocks(world,
				pos, 1);
		int newSize = 2 + 4 + (8 * Collections.frequency(nexusBlocks.values(), 0));
		int sizeDifference = newSize - this.inventorySize;
		if (sizeDifference < 0) {
			int abs = MathHelper.abs(sizeDifference);
			InventoryBasic tempInv = new InventoryBasic(new TextComponentString("temp_inv"), abs);
			for (int i = 0; i < abs; i++) {
				tempInv.addItem(this.getBookList().get(this.inventorySize - 1 - i));
			}
			InventoryHelper.dropInventoryItems(this.getWorld(), this.getPos(), tempInv);
		}
		this.inventorySize = 2 + 4 + (8 * Collections.frequency(nexusBlocks.values(), 0));
		this.markDirty();
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.updateInventorySize(this.getWorld(), this.getPos());
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
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
		this.updateInventorySize(this.getWorld(), this.getPos());
		for (int i = 2; i < this.inventorySize; i++) {
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
		this.updateInventorySize(this.getWorld(), this.getPos());
		NBTTagCompound nbt = pkt.getNbtCompound();
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
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
		if (nbt.hasKey("query")) {
			this.query = nbt.getString("query");
		} else {
			this.query = null;
		}
		this.filterBooks(this.query);
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
		this.updateInventorySize(this.getWorld(), this.getPos());
		NBTTagCompound nbt = new NBTTagCompound();
		if (nbt.hasKey("books")) {
			NBTTagCompound books = nbt.getCompoundTag("books");
			this.bookList = NonNullList.<ItemStack>withSize(this.inventorySize, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(books, this.bookList);
			this.shortList = new ArrayList<ItemStack>();
			for (int i = 2; i < this.bookList.size(); i++) {
				if (!this.bookList.get(i).isEmpty()) {
					shortList.add(this.bookList.get(i));
				} else {
					break;
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
		this.filterBooks(this.query);
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

	class TestInventory implements IInventory {
		
		protected int size;
		
		public TestInventory(int size) {
			this.size = size;
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
		public ITextComponent getDisplayName() {
			return null;
		}

		@Override
		public int getSizeInventory() {
			return this.size;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			return null;
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
			return 0;
		}

		@Override
		public void markDirty() {
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

	}

}