package thefloydman.moremystcraft.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.xcompwiz.mystcraft.api.item.IItemPageProvider;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.inventory.ContainerBase;
import com.xcompwiz.mystcraft.inventory.ContainerWritingDesk;
import com.xcompwiz.mystcraft.inventory.IBookContainer;
import com.xcompwiz.mystcraft.network.IGuiMessageHandler;
import com.xcompwiz.mystcraft.tileentity.IOInventory;
import com.xcompwiz.mystcraft.inventory.PageCollectionPageReceiver;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.item.LinkItemUtils;
import com.xcompwiz.mystcraft.linking.DimensionUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;

public class ContainerNexusController extends ContainerBase implements IBookContainer {

	public TileEntityNexusController tileEntity;
	InventoryPlayer inventory;
	NBTTagCompound nbt;
	private String itemName;
	private ItemStack itemStack;
	private NBTTagCompound bookCompound;
	//public List<ItemStack> bookArray = new ArrayList<ItemStack>();
	private ItemStack currentpage;
    private int currentpageIndex;
    private int pagecount;
    private EntityPlayer player;
    private ILinkInfo cached_linkinfo;
    private boolean cached_permitted;
    public boolean bookSelected = false;

	public ContainerNexusController(InventoryPlayer playerInv, TileEntityNexusController controller) {

		this.tileEntity = controller;
		this.inventory = playerInv;
		// Add player inventory slots.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 168));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	@Nonnull
	@Override
	public ItemStack getBook() {
		//return new ItemStack(Item.getByNameOrId("mystcraft:agebook"));
		if (this.tileEntity.bookArray.isEmpty() || !bookSelected) {
			return ItemStack.EMPTY;
		}
		return this.tileEntity.bookArray.get(0);
		//return inventory.getItemStack();
	}

	@Override
    public Collection<String> getBookAuthors() {
        final ItemStack book = this.getBook();
        if (book.isEmpty()) {
            return (Collection<String>)Collections.EMPTY_SET;
        }
        return ((ItemLinking)book.getItem()).getAuthors(book);
    }

	@Override
    public String getBookTitle() {
        final ItemStack book = this.getBook();
        if (book.isEmpty() || !(book.getItem() instanceof ItemLinking)) {
            return "";
        }
        return ((ItemLinking)book.getItem()).getTitle(book);
    }

	@Nonnull
    @Override
    public ItemStack getCurrentPage() {
        if (this.currentpage.isEmpty()) {
            this.setCurrentPageIndex(this.currentpageIndex);
        }
        return this.currentpage;
    }

	@Override
    public int getCurrentPageIndex() {
        return this.currentpageIndex;
    }

	@Override
    public ILinkInfo getLinkInfo() {
        final ItemStack book = this.getBook();
        if (book.isEmpty() || !(book.getItem() instanceof ItemLinking)) {
            return null;
        }
        if (this.cached_linkinfo == null) {
            this.cached_linkinfo = ((ItemLinking)book.getItem()).getLinkInfo(book);
        }
        return this.cached_linkinfo;
    }

	@Override
    public int getPageCount() {
        return this.pagecount;
    }

	@Override
	public boolean hasBookSlot() {
		return false;
	}

	@Override
    public boolean isLinkPermitted() {
        return true;
    }

	@Override
    public boolean isTargetWorldVisited() {
        final Integer dim = LinkItemUtils.getTargetDimension(this.getBook());
        return dim != null && DimensionUtils.isDimensionVisited(dim);
    }

	@Override
    public void setCurrentPageIndex(int index) {
        this.currentpage = ItemStack.EMPTY;
        this.currentpageIndex = 0;
        if (index < 0) {
            index = 0;
        }
        final List<ItemStack> pagelist = this.getPageList();
        if (pagelist == null) {
            index = 0;
            this.pagecount = 0;
        }
        else {
            this.pagecount = pagelist.size();
            if (index >= this.pagecount) {
                index = this.pagecount;
            }
            else {
                this.currentpage = pagelist.get(index);
            }
        }
        this.currentpageIndex = index;
    }
	
	private List<ItemStack> getPageList() {
        final ItemStack book = this.getBook();
        if (book.isEmpty()) {
            return null;
        }
        if (book.getItem() instanceof IItemPageProvider) {
            return ((IItemPageProvider)book.getItem()).getPageList(this.player, book);
        }
        return null;
    }

}
