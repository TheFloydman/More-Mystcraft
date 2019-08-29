package thefloydman.moremystcraft.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.item.IItemRenameable;
import com.xcompwiz.mystcraft.inventory.ContainerBase;
import com.xcompwiz.mystcraft.inventory.SlotCollection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.tileentity.TileEntitySymbolRecordingDesk;

public class ContainerSymbolRecordingDesk extends ContainerBase {

	private TileEntitySymbolRecordingDesk tileEntity;
	private InventoryPlayer playerinv;

	public ContainerSymbolRecordingDesk(InventoryPlayer inventoryplayer, TileEntitySymbolRecordingDesk te) {
		this.tileEntity = te;
		this.playerinv = inventoryplayer;

		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 135 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 193));
		}

		SlotCollection maininv = new SlotCollection(this, 0, 27);
		SlotCollection hotbar = new SlotCollection(this, 27, 36);

		maininv.pushTargetFront(hotbar);
		hotbar.pushTargetFront(maininv);

		this.collections.add(maininv);
		this.collections.add(hotbar);
	}

	@Nonnull
	public ItemStack getPageCollection() {
		return getInventoryItem();
	}

	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Nullable
	public String getTabItemName() {
		ItemStack itemstack = getInventoryItem();
		if (itemstack.isEmpty())
			return null;
		if (itemstack.getItem() instanceof IItemRenameable)
			return ((IItemRenameable) itemstack.getItem()).getDisplayName(this.playerinv.player, itemstack);
		return null;
	}

	@Nonnull
	public ItemStack getInventoryItem() {
		return ItemStack.EMPTY;
		// return this.playerinv.getStackInSlot(this.slot);
	}

	public void processMessage(@Nonnull EntityPlayer player, @Nonnull NBTTagCompound data) {
		if (data.hasKey("WriteSymbol")) {
			this.tileEntity.writeSymbol(player, new ResourceLocation(data.getString("WriteSymbol")));
		}
	}

}