package thefloydman.moremystcraft.inventory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.xcompwiz.mystcraft.api.item.IItemPageProvider;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.inventory.ContainerBase;
import com.xcompwiz.mystcraft.inventory.IBookContainer;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.item.LinkItemUtils;
import com.xcompwiz.mystcraft.linking.DimensionUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;

public class ContainerNexusController extends ContainerBase implements IBookContainer {

	public TileEntityNexusController tileEntity;
	InventoryPlayer inventory;
	NBTTagCompound nbt;
	private String itemName;
	private ItemStack itemStack;
	private NBTTagCompound bookCompound;
	// public List<ItemStack> bookArray = new ArrayList<ItemStack>();
	private ItemStack currentpage;
	private int currentpageIndex;
	private int pagecount;
	private EntityPlayer player;
	private ILinkInfo cached_linkinfo;
	private boolean cached_permitted;
	public boolean bookSelected = false;
	private final IInventory nexusInventory = new InventoryBasic("nexus_controller", false, 2) {
	};

	public ContainerNexusController(InventoryPlayer playerInv, TileEntityNexusController controller) {

		this.tileEntity = controller;
		this.inventory = playerInv;
		// Add player inventory slots.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 168));
		}

		this.addSlotToContainer(new SlotNexusInput(this.nexusInventory, 0, 17, 72));
		this.addSlotToContainer(new Slot(this.nexusInventory, 1, 143, 72) {

			public boolean isItemValid(ItemStack stack) {
				return false;

			}
		});

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
		if (this.tileEntity.bookList.isEmpty() || !bookSelected) {
			return ItemStack.EMPTY;
		}
		return this.getSlot(1).getStack();
	}

	@Override
	public Collection<String> getBookAuthors() {
		final ItemStack book = this.getBook();
		if (book.isEmpty()) {
			return (Collection<String>) Collections.EMPTY_SET;
		}
		return ((ItemLinking) book.getItem()).getAuthors(book);
	}

	@Override
	public String getBookTitle() {
		final ItemStack book = this.getBook();
		if (book.isEmpty() || !(book.getItem() instanceof ItemLinking)) {
			return "";
		}
		return ((ItemLinking) book.getItem()).getTitle(book);
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
			this.cached_linkinfo = ((ItemLinking) book.getItem()).getLinkInfo(book);
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
		} else {
			this.pagecount = pagelist.size();
			if (index >= this.pagecount) {
				index = this.pagecount;
			} else {
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
			return ((IItemPageProvider) book.getItem()).getPageList(this.player, book);
		}
		return null;
	}

	protected void acceptBook() {
		if (this.getSlotFromInventory(this.nexusInventory, 0).getHasStack()) {
			if (this.getSlotFromInventory(this.nexusInventory, 0).getStack().getItem() instanceof ItemLinking) {
				if (this.tileEntity.getBookCount() < this.tileEntity.inventorySize) {
					this.tileEntity.addBook(this.getSlotFromInventory(this.nexusInventory, 0).getStack());
				}
			}
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		if (!this.tileEntity.getWorld().isRemote) {
			this.clearContainer(playerIn, playerIn.world, this.nexusInventory);
		}
	}

	public class SlotNexusInput extends Slot {

		public SlotNexusInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return stack.getItem() instanceof ItemLinking;

		}

		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
			if (!ContainerNexusController.this.tileEntity.getWorld().isRemote) {
				acceptBook();
			} else {
				acceptBook();
				ContainerNexusController.this.getSlotFromInventory(ContainerNexusController.this.nexusInventory, 0).decrStackSize(1);
			}
		}

	}

}
