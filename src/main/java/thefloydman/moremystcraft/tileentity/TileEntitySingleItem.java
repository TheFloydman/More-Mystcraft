package thefloydman.moremystcraft.tileentity;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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

public class TileEntitySingleItem extends TileEntity implements IInventory {
	
	public TileEntitySingleItem() {
	}

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
		NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, list);
		this.item = list.get(0);
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
		list.set(0, this.getItem());
		nbt = ItemStackHelper.saveAllItems(nbt, list);
		return super.writeToNBT(nbt);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		SPacketUpdateTileEntity pkt = super.getUpdatePacket();
		NBTTagCompound nbt = new NBTTagCompound();
		if (pkt == null) {
			pkt = new SPacketUpdateTileEntity();
		} else {
			nbt = pkt.getNbtCompound();
		}
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt = this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
		this.markDirty();
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

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return !oldState.getBlock().equals(newState.getBlock());
	}

}