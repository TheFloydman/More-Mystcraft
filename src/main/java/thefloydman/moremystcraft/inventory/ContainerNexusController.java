package thefloydman.moremystcraft.inventory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.xcompwiz.mystcraft.api.item.IItemPageProvider;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.inventory.ContainerBase;
import com.xcompwiz.mystcraft.inventory.IBookContainer;
import com.xcompwiz.mystcraft.item.ItemAgebook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.item.LinkItemUtils;
import com.xcompwiz.mystcraft.linking.DimensionUtils;
import com.xcompwiz.mystcraft.network.IGuiMessageHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thefloydman.moremystcraft.block.BlockNexusController;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;

public class ContainerNexusController extends ContainerBase implements IBookContainer, IGuiMessageHandler {

	public TileEntityNexusController tileEntity;
	InventoryPlayer inventory;
	NBTTagCompound nbt;
	private String itemName;
	private ItemStack itemStack;
	private NBTTagCompound bookCompound;
	private ItemStack currentpage;
	private int currentpageIndex;
	private int pagecount;
	private EntityPlayer player;
	private ILinkInfo cached_linkinfo;
	private boolean cached_permitted;
	public int selectedBook;
	protected String query;

	public ContainerNexusController(InventoryPlayer playerInv, TileEntityNexusController controller) {

		this.tileEntity = controller;
		this.inventory = playerInv;
		this.query = "";

		this.addSlotToContainer(new SlotNexusInput(controller, 0, 17, 98));
		this.addSlotToContainer(new Slot(controller, 1, 143, 98) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
				ContainerNexusController.this.tileEntity.removeBook(ContainerNexusController.this.selectedBook);
				ContainerNexusController.this.selectedBook = -1;
				return super.onTake(thePlayer, stack);
			}
		});

		// Add player inventory slots.
		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 194));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 136 + i * 18));
			}
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack copyStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack existingStack = slot.getStack();
			copyStack = existingStack.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(existingStack, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(existingStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (existingStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (existingStack.getCount() == copyStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, existingStack);
		}

		return copyStack;
	}

	@Nonnull
	@Override
	public ItemStack getBook() {
		if (this.tileEntity.getBookList() == null || this.tileEntity.getBookList().isEmpty() || this.selectedBook < 2) {
			return ItemStack.EMPTY;
		}
		return this.getSlotFromInventory(this.tileEntity, 1).getStack();
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
		ILinkInfo linkInfo = getLinkInfo();
		if (linkInfo == null) {
			return false;
		}
		if (ItemAgebook.isNewAgebook(getBook())) {
			return true;
		}
		if (linkInfo.getDimensionUID() != null) {
			return this.tileEntity.getWorld().provider.getDimension() != linkInfo.getDimensionUID();
		}
		return false;
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

		if (this.getSlotFromInventory(this.tileEntity, 0).getHasStack()) {
			if (this.getSlotFromInventory(this.tileEntity, 0).getStack().getItem() instanceof ItemLinking) {
				if (this.tileEntity.getBookCount() < this.tileEntity.getBookList().size() - 2) {
					this.tileEntity.addBook(this.getSlotFromInventory(this.tileEntity, 0).getStack());
				}

				if (!this.tileEntity.getWorld().isRemote) {
					this.tileEntity.getWorld().notifyBlockUpdate(this.tileEntity.getPos(),
							this.tileEntity.getWorld().getBlockState(this.tileEntity.getPos()),
							this.tileEntity.getWorld().getBlockState(this.tileEntity.getPos()), 7);
				}
			}
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		World world = this.tileEntity.getWorld();
		if (!world.isRemote) {
			this.clearContainer(playerIn, playerIn.world, this.tileEntity);
			IBlockState state = world.getBlockState(this.tileEntity.getPos());
			world.setBlockState(this.tileEntity.getPos(),
					state.withProperty(BlockNexusController.IN_USE, Boolean.valueOf(false)));
		}

	}

	@Override
	protected void clearContainer(EntityPlayer playerIn, World worldIn, IInventory inventoryIn) {
		inventoryIn.removeStackFromSlot(1);
		if (!playerIn.isEntityAlive()
				|| playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) {
			playerIn.dropItem(inventoryIn.removeStackFromSlot(0), false);
		} else {
			playerIn.inventory.placeItemBackInInventory(worldIn, inventoryIn.removeStackFromSlot(0));
		}
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id) {
		this.selectedBook = id;
		this.getSlotFromInventory(this.tileEntity, 1).putStack(this.tileEntity.getStackInSlot(id));
		return true;
	}

	@Override
	public void processMessage(EntityPlayer player, NBTTagCompound data) {
		if (data.hasKey("Link") && this.tileEntity != null) {
			ItemStack book = this.tileEntity.getStackInSlot(1);
			if (book.isEmpty())
				return;
			if (book.getItem() instanceof ItemLinking) {
				((ItemLinking) book.getItem()).activate(book, this.tileEntity.getWorld(), player);
			}

		}
		if (data.hasKey("query")) {
			this.query = data.getString("query");
			this.tileEntity.setQuery(this.query);
		}
	}

	public String getQuery() {
		return this.query;
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
			acceptBook();
		}

	}

}
