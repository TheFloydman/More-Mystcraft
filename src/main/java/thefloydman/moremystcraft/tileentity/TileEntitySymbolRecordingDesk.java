package thefloydman.moremystcraft.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.api.item.IItemWritable;
import com.xcompwiz.mystcraft.data.ModAchievements;
import com.xcompwiz.mystcraft.item.ItemPage;
import com.xcompwiz.mystcraft.tileentity.FluidTankFiltered;
import com.xcompwiz.mystcraft.tileentity.IOInventory;
import com.xcompwiz.mystcraft.tileentity.InventoryFilter;
import com.xcompwiz.mystcraft.tileentity.TileEntityBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySymbolRecordingDesk extends TileEntityBase implements InventoryFilter {

	private IOInventory inventoryStacks;
	private FluidTankFiltered inkwell;

	private static final int SLOT_PAPER = 0;
	private static final int SLOT_SYMBOL = 1;

	public TileEntitySymbolRecordingDesk() {
		this.inventoryStacks = buildWorkInventory();
		this.inkwell = new FluidTankFiltered(800);
		this.inkwell.setPermittedFluids(Mystcraft.validInks);
	}

	public void writeSymbol(EntityPlayer player, ResourceLocation symbol) {
		if (this.getWorld().isRemote)
			return;
		if (!hasEnoughInk()) {
			return;
		}

		if (this.inventoryStacks.getStackInSlot(SLOT_SYMBOL).isEmpty()
				&& !this.inventoryStacks.getStackInSlot(SLOT_PAPER).isEmpty()) {
			this.inventoryStacks.setStackInSlot(SLOT_SYMBOL,
					ItemPage.createItemstack(this.inventoryStacks.getStackInSlot(SLOT_PAPER)));
			if (this.inventoryStacks.getStackInSlot(SLOT_PAPER).getCount() <= 0) {
				this.inventoryStacks.setStackInSlot(SLOT_PAPER, ItemStack.EMPTY);
			}
		}

		if (this.inventoryStacks.getStackInSlot(SLOT_SYMBOL).isEmpty()) {
			return;
		}

		ItemStack target = this.inventoryStacks.getStackInSlot(SLOT_SYMBOL);
		if (target.isEmpty())
			return;
		if (target.getItem() instanceof IItemWritable
				&& ((IItemWritable) target.getItem()).writeSymbol(player, target, symbol)) {
			useink();
			if (player instanceof EntityPlayerMP) {
				ModAchievements.TRIGGER_WRITE.trigger((EntityPlayerMP) player);
			}
			return;
		}
	}

	private boolean hasEnoughInk() {
		FluidStack fluid = this.inkwell.getFluid();
		return (fluid != null && fluid.amount >= Mystcraft.inkcost);
	}

	@Override
	public boolean canAcceptItem(int arg0, ItemStack arg1) {
		return false;
	}

	protected IOInventory buildWorkInventory() {
		return (new IOInventory(this, new int[] { SLOT_PAPER }, new int[0], EnumFacing.VALUES))
				.setMiscSlots(new int[] { SLOT_SYMBOL }).setListener(() -> onChange(true))
				.applyFilter(this, new int[] { SLOT_PAPER });
	}

	private void useink() {
		this.inkwell.drain(Mystcraft.inkcost, true);
	}

	public void onChange(boolean isWorkInv) {
		if (isWorkInv) {
			for (int i = 0; i < this.inventoryStacks.getSlots(); i++) {
				handleItemChange(this.inventoryStacks.getStackInSlot(i));
			}
		}
	}

	public void handleItemChange(@Nonnull ItemStack itemstack) {
	}

	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.inventoryStacks.getCapability(facing);
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.inkwell;
		}
		return null;
	}

}
