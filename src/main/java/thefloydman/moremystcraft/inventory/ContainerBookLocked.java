/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 */

package thefloydman.moremystcraft.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.item.IItemPageProvider;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.entity.EntityLinkbook;
import com.xcompwiz.mystcraft.inventory.ContainerBase;
import com.xcompwiz.mystcraft.inventory.ContainerBook;
import com.xcompwiz.mystcraft.inventory.IBookContainer;
import com.xcompwiz.mystcraft.inventory.SlotCollection;
import com.xcompwiz.mystcraft.inventory.SlotFiltered;
import com.xcompwiz.mystcraft.inventory.TargetInventory;
import com.xcompwiz.mystcraft.item.ItemAgebook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.item.LinkItemUtils;
import com.xcompwiz.mystcraft.linking.DimensionUtils;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;
import com.xcompwiz.mystcraft.network.IGuiMessageHandler;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;
import com.xcompwiz.mystcraft.tileentity.IOInventory;
import com.xcompwiz.mystcraft.tileentity.InventoryFilter;
import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ContainerBookLocked extends ContainerBase implements IGuiMessageHandler, IBookContainer {

	private EntityLinkbook linkbook;
	private TileEntityBookRotateable bookTile;
	private Integer slot;
	private InventoryPlayer inventoryplayer;
	@Nonnull
	private ItemStack currentpage;
	private int pagecount;
	private int currentpageIndex;
	private ILinkInfo cached_linkinfo;
	private Boolean cached_permitted;

	public ContainerBookLocked(final InventoryPlayer inventoryplayer, final TileEntityBookRotateable tile) {
		this.currentpage = ItemStack.EMPTY;
		this.pagecount = 0;
		this.currentpageIndex = 0;
		this.bookTile = tile;
		this.inventoryplayer = inventoryplayer;
		this.updateSlots();
	}

	@Nonnull
	@Override
	public ItemStack getBook() {
		if (this.linkbook != null) {
			final ItemStack itemstack = this.linkbook.getBook();
			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemLinking) {
				return itemstack;
			}
		} else if (this.bookTile != null) {
			final ItemStack book = this.bookTile.getBook();
			if (!book.isEmpty() && book.getItem() instanceof ItemLinking) {
				return book;
			}
		} else if (this.slot != null) {
			final ItemStack itemstack = this.inventoryplayer.getStackInSlot((int) this.slot);
			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemLinking) {
				return itemstack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setCurrentPageIndex(int index) {
		this.currentpage = ItemStack.EMPTY;
		this.currentpageIndex = 0;
		if (index < 0) {
			index = 0;
		}
		final List<ItemStack> pagelist = this.getPageList();
		if (pagelist != null && index >= pagelist.size()) {
			index = pagelist.size();
		}
		if (pagelist != null && index < pagelist.size()) {
			this.pagecount = pagelist.size();
			this.currentpage = pagelist.get(index);
		}
		this.currentpageIndex = index;
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

	@Nullable
	private List<ItemStack> getPageList() {
		final ItemStack book = this.getBook();
		if (book.isEmpty()) {
			return null;
		}
		if (book.getItem() instanceof IItemPageProvider) {
			return ((IItemPageProvider) book.getItem()).getPageList(this.inventoryplayer.player, book);
		}
		return null;
	}

	@Override
	public int getPageCount() {
		return this.pagecount;
	}

	@Override
	public boolean isTargetWorldVisited() {
		return DimensionUtils.isDimensionVisited(LinkItemUtils.getTargetDimension(this.getBook()));
	}

	@Override
	public boolean hasBookSlot() {
		return false;
	}

	public int getInventorySize() {
		if (this.linkbook != null && !this.linkbook.isDead) {
			return 1;
		}
		if (this.bookTile != null) {
			return 1;
		}
		return 0;
	}

	@Nonnull
	public ItemStack slotClick(final int slotId, final int dragType, final ClickType clickTypeIn,
			final EntityPlayer player) {
		if (slotId >= this.inventorySlots.size()) {
			return ItemStack.EMPTY;
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public void updateSlots() {
		if (this.linkbook != null || this.bookTile != null) {
			final ItemStack book = this.getBook();
			if (this.currentpageIndex > 0) {
				this.inventorySlots.clear();
				this.inventoryItemStacks.clear();
			} else if (!book.isEmpty() && this.inventorySlots.size() != 1) {
				this.inventorySlots.clear();
				this.inventoryItemStacks.clear();
				final IItemHandlerModifiable other = this.getCurrentOtherInventory();
			} else if (book.isEmpty() && this.getCurrentOtherInventorySize() == 1 && this.inventorySlots.size() != 37) {
				this.inventorySlots.clear();
				this.inventoryItemStacks.clear();
				this.addInventorySlots();
				final IItemHandlerModifiable other = this.getCurrentOtherInventory();
			} else if (book.isEmpty() && this.getCurrentOtherInventorySize() == 0 && this.inventorySlots.size() != 36) {
				this.inventorySlots.clear();
				this.inventoryItemStacks.clear();
				this.addInventorySlots();
			}
		}
	}

	private void addInventorySlots() {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(
						new Slot((IInventory) this.inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot((IInventory) this.inventoryplayer, i, 8 + i * 18, 142));
		}
	}

	public void detectAndSendChanges() {
		final List<IMessage> packets = new ArrayList<IMessage>();
		for (int slotId = 0; slotId < this.inventorySlots.size(); ++slotId) {
			final ItemStack actual = this.inventorySlots.get(slotId).getStack();
			ItemStack stored = (ItemStack) this.inventoryItemStacks.get(slotId);
			if (!ItemStack.areItemStacksEqual(stored, actual)) {
				if (slotId == 0) {
					this.cached_linkinfo = null;
					this.cached_permitted = null;
					final NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setInteger("SetCurrentPage", this.currentpageIndex);
					packets.add((IMessage) new MPacketGuiMessage(this.windowId, nbttagcompound));
				}
				stored = (actual.isEmpty() ? ItemStack.EMPTY : actual.copy());
				this.inventoryItemStacks.set(slotId, (ItemStack) stored);
				for (final IContainerListener listener : this.listeners) {
					listener.sendSlotContents((Container) this, slotId, stored);
				}
			}
		}
		if (this.cached_permitted == null) {
			this.cached_permitted = this.checkLinkPermitted();
			if (this.cached_permitted != null) {
				final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
				nbttagcompound2.setBoolean("LinkPermitted", (boolean) this.cached_permitted);
				packets.add((IMessage) new MPacketGuiMessage(this.windowId, nbttagcompound2));
			}
		}
		if (packets.size() > 0) {
			for (final IContainerListener listener2 : this.listeners) {
				if (listener2 instanceof EntityPlayerMP) {
					for (final IMessage message : packets) {
						MystcraftPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) listener2);
					}
				}
			}
		}
	}

	public void updateProgressBar(final int i, final int j) {
	}

	public boolean canInteractWith(@Nonnull final EntityPlayer entityplayer) {
		this.updateSlots();
		return this.linkbook == null || this.linkbook.isDead || !this.getBook().isEmpty();
	}

	public Slot getSlotFromInventory(@Nonnull final IInventory par1IInventory, final int par2) {
		for (final Slot slot : this.inventorySlots) {
			if (slot.isHere(par1IInventory, par2)) {
				return slot;
			}
		}
		return new Slot((IInventory) this.inventoryplayer, this.inventoryplayer.currentItem, 0, 0);
	}

	public Slot getSlot(int par1) {
		if (par1 >= this.inventorySlots.size()) {
			par1 = 0;
		}
		if (par1 < this.inventorySlots.size()) {
			return this.inventorySlots.get(par1);
		}
		return new Slot((IInventory) this.inventoryplayer, this.inventoryplayer.currentItem, 0, 0);
	}

	@Override
	public void processMessage(@Nonnull final EntityPlayer player, @Nonnull final NBTTagCompound data) {
		if (data.hasKey("LinkPermitted")) {
			this.cached_permitted = data.getBoolean("LinkPermitted");
		}
		if (data.hasKey("SetCurrentPage")) {
			this.setCurrentPageIndex(data.getInteger("SetCurrentPage"));
		}
		if (data.hasKey("Link")) {
			if (this.bookTile != null) {
				this.bookTile.link((Entity) player);
			} else if (this.linkbook != null) {
				this.linkbook.linkEntity((Entity) player);
			} else if (this.slot != null) {
				final ItemStack itemstack = this.inventoryplayer.getStackInSlot((int) this.slot);
				if (itemstack.getItem() instanceof ItemLinking) {
					((ItemLinking) itemstack.getItem()).activate(itemstack, player.world, (Entity) player);
				}
			}
		}
	}

	@Override
	public ILinkInfo getLinkInfo() {
		final ItemStack book = this.getBook();
		if (book.isEmpty() || !(book.getItem() instanceof ItemLinking)) {
			return this.cached_linkinfo = null;
		}
		if (this.cached_linkinfo == null) {
			this.cached_linkinfo = ((ItemLinking) book.getItem()).getLinkInfo(book);
		}
		return this.cached_linkinfo;
	}

	@Override
	public boolean isLinkPermitted() {
		final ILinkInfo linkinfo = this.getLinkInfo();
		if (linkinfo == null) {
			this.cached_permitted = null;
		}
		return this.cached_permitted != null && this.cached_permitted;
	}

	private boolean checkLinkPermitted() {
		final ILinkInfo linkinfo = this.getLinkInfo();
		return linkinfo != null && (ItemAgebook.isNewAgebook(this.getBook()) || LinkListenerManager
				.isLinkPermitted(this.inventoryplayer.player.world, (Entity) this.inventoryplayer.player, linkinfo));
	}

	@Nullable
	private IItemHandlerModifiable getCurrentOtherInventory() {
		if (this.linkbook != null && !this.linkbook.isDead) {
			return (IItemHandlerModifiable) this.linkbook.createBookWrapper();
		}
		if (this.bookTile != null) {
			return (IItemHandlerModifiable) this.bookTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.DOWN);
		}
		return null;
	}

	private int getCurrentOtherInventorySize() {
		if (this.linkbook != null && !this.linkbook.isDead) {
			return 1;
		}
		if (this.bookTile != null) {
			final IOInventory inv = (IOInventory) this.bookTile.getCapability(
					(net.minecraftforge.common.capabilities.Capability) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.DOWN);
			if (inv != null && inv.getSlots() > 0) {
				return 1;
			}
		}
		return 0;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
		ItemStack clone = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			final ItemStack original = slot.getStack();
			clone = original.copy();
			final List<SlotCollection> collections = new ArrayList<SlotCollection>();
			SlotCollection internal = null;
			final ItemStack book = this.getBook();
			if (!book.isEmpty()) {
				if (this.linkbook != null && !this.linkbook.isDead) {
					internal = new SlotCollection(this, 0, 1);
					internal.pushTargetFront(new TargetInventory(this.inventoryplayer));
					collections.add(internal);
				} else if (this.bookTile != null) {
					final IOInventory inv = (IOInventory) this.bookTile
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
					if (inv != null && inv.getSlots() > 0) {
						internal = new SlotCollection(this, inv.getSlots() - 1, inv.getSlots());
						internal.pushTargetFront(new TargetInventory(this.inventoryplayer));
						collections.add(internal);
					}
				}
			}
			if (book.isEmpty()) {
				final SlotCollection maininv = new SlotCollection(this, 0, 27);
				final SlotCollection hotbar = new SlotCollection(this, 27, 36);
				maininv.pushTargetFront(hotbar);
				hotbar.pushTargetFront(maininv);
				if (internal != null) {
					internal.pushTargetFront(maininv);
					internal.pushTargetFront(hotbar);
					maininv.pushTargetFront(internal);
					hotbar.pushTargetFront(internal);
				}
				collections.add(maininv);
				collections.add(hotbar);
			}
			for (final SlotCollection collection : collections) {
				if (!collection.contains(i)) {
					continue;
				}
				collection.onShiftClick(original);
				break;
			}
			if (original.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (original.getCount() == clone.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, original);
		}
		return clone;
	}

	@Override
	public String getBookTitle() {
		final ItemStack book = this.getBook();
		if (book.isEmpty() || !(book.getItem() instanceof ItemLinking)) {
			return "";
		}
		return ((ItemLinking) book.getItem()).getTitle(book);
	}

	@Override
	public Collection<String> getBookAuthors() {
		final ItemStack book = this.getBook();
		if (book.isEmpty() || !(book.getItem() instanceof ItemLinking)) {
			return (Collection<String>) Collections.EMPTY_SET;
		}
		return ((ItemLinking) book.getItem()).getAuthors(book);
	}

	public static class Messages {
		public static final String LinkPermitted = "LinkPermitted";
		public static final String SetCurrentPage = "SetCurrentPage";
		public static final String Link = "Link";
	}

}