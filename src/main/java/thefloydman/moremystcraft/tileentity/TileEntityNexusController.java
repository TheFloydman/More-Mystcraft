package thefloydman.moremystcraft.tileentity;

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
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.util.Reference;

public class TileEntityNexusController extends TileEntity {

	public NonNullList<ItemStack> bookList;
	protected int bookCount;
	public int inventorySize;

	public TileEntityNexusController() {
		this.inventorySize = 2;
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
		System.out.println("Before: " + bookList);
		for (int i = 0; i < this.inventorySize; i++) {
			if (this.bookList.get(i).isEmpty()) {
				this.bookList.set(i, stack);
				this.getWorld().notifyBlockUpdate(this.getPos(), MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(), MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(), 3);
				bookCount++;
				this.markDirty();
				System.out.println("After: " + bookList);
				return true;
			}
		}
		return false;
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

}