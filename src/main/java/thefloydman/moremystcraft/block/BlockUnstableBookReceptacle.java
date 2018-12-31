/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.xcompwiz.mystcraft.api.item.IItemPortalActivator;
import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;
import com.xcompwiz.mystcraft.data.ModBlocks;
import com.xcompwiz.mystcraft.tileentity.IOInventory;

import thefloydman.moremystcraft.portal.MoreMystcraftPortalUtils;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;

public class BlockUnstableBookReceptacle extends BlockContainer {
	public static final PropertyEnum<EnumFacing> ROTATION;

	public BlockUnstableBookReceptacle() {
		super(Material.GLASS);
		this.setTickRandomly(false);
		this.useNeighborBrightness = true;
		this.setHardness(1.0f);
		this.setSoundType(SoundType.GLASS);
		this.setUnlocalizedName("moremystcraft.unstable_receptacle");
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);
	}

	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty((IProperty) BlockUnstableBookReceptacle.ROTATION,
				(Comparable) EnumFacing.values()[meta]);
	}

	public int getMetaFromState(final IBlockState state) {
		return ((EnumFacing) state.getValue((IProperty) BlockUnstableBookReceptacle.ROTATION)).ordinal();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer((Block) this, new IProperty[] { BlockUnstableBookReceptacle.ROTATION });
	}

	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	public EnumBlockRenderType getRenderType(final IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public int quantityDropped(final Random par1Random) {
		return 1;
	}

	public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
		return side != EnumFacing.DOWN
				&& worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().equals(ModBlocks.crystal)
				&& this.canPlaceBlockAt(worldIn, pos);
	}

	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		switch ((EnumFacing) state.getValue((IProperty) BlockUnstableBookReceptacle.ROTATION)) {
		case UP: {
			return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
		}
		case NORTH: {
			return new AxisAlignedBB(0.0, 0.0, 0.75, 1.0, 1.0, 1.0);
		}
		case SOUTH: {
			return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.25);
		}
		case WEST: {
			return new AxisAlignedBB(0.75, 0.0, 0.0, 1.0, 1.0, 1.0);
		}
		case EAST: {
			return new AxisAlignedBB(0.0, 0.0, 0.0, 0.25, 1.0, 1.0);
		}
		default: {
			return BlockUnstableBookReceptacle.FULL_BLOCK_AABB;
		}
		}
	}

	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing,
			final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer,
			final EnumHand hand) {
		return this.getDefaultState().withProperty((IProperty) BlockUnstableBookReceptacle.ROTATION,
				(Comparable) facing);
	}

	public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn,
			final BlockPos fromPos) {
		final BlockPos receptable = MoreMystcraftPortalUtils.getReceptacleBase(pos,
				(EnumFacing) state.getValue((IProperty) BlockUnstableBookReceptacle.ROTATION));
		if (!worldIn.getBlockState(receptable).getBlock().equals(ModBlocks.crystal)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
		if (!worldIn.isRemote) {
			final TileEntityUnstableBookReceptacle book = (TileEntityUnstableBookReceptacle) worldIn.getTileEntity(pos);
			if (book != null) {
				final ItemStack in = book.getBook();
				if (!in.isEmpty()) {
					final EntityItem i = new EntityItem(worldIn, (double) pos.getX(), (double) pos.getY(),
							(double) pos.getZ(), in);
					worldIn.spawnEntity((Entity) i);
				}
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state,
			final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX,
			final float hitY, final float hitZ) {
		final TileEntityUnstableBookReceptacle book = (TileEntityUnstableBookReceptacle) worldIn.getTileEntity(pos);
		if (book == null) {
			return false;
		}
		ItemStack in = book.getBook();
		if (!in.isEmpty()) {
			if (!worldIn.isRemote && playerIn.getHeldItem(hand).isEmpty()) {
				playerIn.setHeldItem(hand, in);
				book.setBook(ItemStack.EMPTY);
			}
			return true;
		}
		in = playerIn.getHeldItem(hand);
		if (!in.isEmpty() && in.getItem() instanceof IItemPortalActivator) {
			if (!worldIn.isRemote) {
				playerIn.setHeldItem(hand, ItemStack.EMPTY);
				book.setBook(in);
			}
			return true;
		}
		return false;
	}

	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Nullable
	public TileEntity createTileEntity(final World world, final IBlockState state) {
		return new TileEntityUnstableBookReceptacle();
	}

	public TileEntity createNewTileEntity(final World world, final int metadata) {
		return new TileEntityUnstableBookReceptacle();
	}

	public boolean hasComparatorInputOverride(final IBlockState state) {
		return true;
	}

	public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
		final TileEntity te = worldIn.getTileEntity(pos);
		if (te != null) {
			final IItemHandler cap = (IItemHandler) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.DOWN);
			if (cap != null && cap instanceof IOInventory) {
				return ((IOInventory) cap).calcRedstoneFromInventory();
			}
		}
		return 0;
	}

	static {
		ROTATION = PropertyEnum.create("rotation", (Class) EnumFacing.class);
	}
}
