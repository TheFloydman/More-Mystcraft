package thefloydman.moremystcraft.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityNexusController extends TileEntity {
	
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

}