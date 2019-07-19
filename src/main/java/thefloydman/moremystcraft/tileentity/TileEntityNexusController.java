package thefloydman.moremystcraft.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.xcompwiz.mystcraft.item.ItemAgebook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityNexusController extends TileEntity implements ISidedInventory {

	public List<ItemStack> bookArray;

	public TileEntityNexusController() {
		bookArray = new ArrayList<ItemStack>();
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		NBTTagCompound books = nbt.getCompoundTag("books");
		for (int i = 0; i < nbt.getCompoundTag("books").getSize(); i++) {
			ItemStack stack = new ItemStack(nbt.getCompoundTag("books").getCompoundTag(Integer.toString(i)));
			bookArray.add(stack);
		}
		System.out.println("readFromNBT Successful");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound books = new NBTTagCompound();
		System.out.println("bookArray size: " + Integer.toString(bookArray.size()));
		for (int i = 0; i < bookArray.size(); i++) {
			NBTTagCompound stack = new NBTTagCompound();
			bookArray.get(i).writeToNBT(stack);
			books.setTag(Integer.toString(i), stack);
			System.out.println("bookArray added to NBT");
		}
		nbt.setTag("books", books);
		System.out.println("writeToNBT Successful");
		return super.writeToNBT(nbt);
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.getItem() instanceof ItemAgebook) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

}