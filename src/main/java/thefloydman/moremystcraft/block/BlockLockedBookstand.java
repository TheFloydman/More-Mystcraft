package thefloydman.moremystcraft.block;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.block.BlockBookstand;
import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.util.MoreMystcraftCreativeTabs;

public class BlockLockedBookstand extends BlockBookstand {
	
	public BlockLockedBookstand() {
		this.setUnlocalizedName("moremystcraft.locked_bookstand");
		this.setCreativeTab(MoreMystcraftCreativeTabs.MORE_MYSTCRAFT);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state,
			final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX,
			final float hitY, final float hitZ) {
		if (worldIn.isRemote) {
			return true;
		}
		final TileEntityBookRotateable tileentity = (TileEntityBookRotateable) worldIn.getTileEntity(pos);
		if (tileentity == null) {
			return true;
		}
		if (tileentity.getBook().isEmpty()) {
			final ItemStack stack = playerIn.getHeldItem(hand);
			if (!stack.isEmpty() && tileentity.canAcceptItem(0, stack)) {
				final ItemStack copy = stack.copy();
				copy.setCount(1);
				stack.shrink(1);
				NBTTagCompound compoundUUID = copy.getTagCompound();
				compoundUUID.setString("moremystcraft_uuid", playerIn.getUniqueID().toString());
				copy.setTagCompound(compoundUUID);
				tileentity.setBook(copy);
				playerIn.setHeldItem(hand, stack);
			}
			return true;
		} else {
			if (playerIn.isSneaking() && playerIn.getHeldItem(hand).isEmpty()
					&& playerIn.getUniqueID().toString().equals(tileentity.getBook().getTagCompound().getString("moremystcraft_uuid"))) {
				playerIn.setHeldItem(hand, tileentity.getBook());
				tileentity.setBook(ItemStack.EMPTY);
				return true;
			}
			if (playerIn.getUniqueID().toString()
					.equals(tileentity.getBook().getTagCompound().getString("moremystcraft_uuid"))) {
				playerIn.openGui((Object) Mystcraft.instance,
						com.xcompwiz.mystcraft.data.ModGUIs.BOOK_DISPLAY.ordinal(), worldIn, pos.getX(), pos.getY(),
						pos.getZ());
				return true;
			}
			playerIn.openGui((Object) MoreMystcraft.instance, MoreMystcraftGUIs.BOOK_DISPLAY_LOCKED.ordinal(), worldIn,
					pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

}
