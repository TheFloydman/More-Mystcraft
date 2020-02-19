package thefloydman.moremystcraft.block;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.capability.journeyclothscollected.CapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.capability.journeyclothscollected.ProviderCapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.capability.journeyhub.ICapabilityHub;
import thefloydman.moremystcraft.capability.journeyhub.ProviderCapabilityHub;
import thefloydman.moremystcraft.capability.uuid.ICapabilityUUID;
import thefloydman.moremystcraft.capability.uuid.ProviderCapabilityUUID;
import thefloydman.moremystcraft.data.worldsaveddata.MoreMystcraftSavedDataPerSave;
import thefloydman.moremystcraft.item.ItemJourneyHub;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.tileentity.TileEntityJourney;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;
import thefloydman.moremystcraft.util.JourneyUtils;
import thefloydman.moremystcraft.util.MoreMystcraftCreativeTabs;
import thefloydman.moremystcraft.util.Reference;

public class BlockJourneyBase extends BlockHorizontal implements ITileEntityProvider {

	public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
	public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(1.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public final JourneyUtils.PatternType PATTERN_TYPE;

	public BlockJourneyBase(JourneyUtils.PatternType type, Material material) {

		super(material);
		this.PATTERN_TYPE = type;
		this.setHardness(0.5f);
		this.setCreativeTab(MoreMystcraftCreativeTabs.MORE_MYSTCRAFT);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));

	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (((EnumFacing) state.getProperties().get(FACING)).getHorizontalIndex()) {
		case 0:
			return AABB_NORTH;
		case 1:
			return AABB_EAST;
		case 2:
			return AABB_SOUTH;
		case 3:
			return AABB_WEST;
		}
		return AABB_NORTH;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		boolean mod = !world.getBlockState(pos.offset(side.getOpposite())).getBlockFaceShape(world, pos, null)
				.equals(BlockFaceShape.UNDEFINED) && !side.equals(EnumFacing.UP) && !side.equals(EnumFacing.DOWN);
		boolean vanilla = super.canPlaceBlockOnSide(world, pos, side);
		return mod && vanilla;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		boolean mod = world.isAirBlock(pos) || world.getBlockState(pos).getMaterial().equals(Material.SNOW) || world
				.getBlockState(pos).getMaterial().equals(Material.GRASS)
				&& !world.getBlockState(pos).getBlockFaceShape(world, pos, null).equals(BlockFaceShape.UNDEFINED);
		boolean vanilla = super.canPlaceBlockAt(world, pos);
		return mod && vanilla;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (world.getBlockState(fromPos).getMaterial().isLiquid()) {
			this.breakBlock(world, pos, state);
			world.setBlockToAir(pos);
		} else if (state.getProperties().get(FACING).equals(EnumFacing.NORTH)) {
			if (!world.getBlockState(pos.south()).getMaterial().isSolid()) {
				this.breakBlock(world, pos, state);
				world.setBlockToAir(pos);
			}
		} else if (state.getProperties().get(FACING).equals(EnumFacing.EAST)) {
			if (!world.getBlockState(pos.west()).getMaterial().isSolid()) {
				this.breakBlock(world, pos, state);
				world.setBlockToAir(pos);
			}
		} else if (state.getProperties().get(FACING).equals(EnumFacing.SOUTH)) {
			if (!world.getBlockState(pos.north()).getMaterial().isSolid()) {
				this.breakBlock(world, pos, state);
				world.setBlockToAir(pos);
			}
		} else if (state.getProperties().get(FACING).equals(EnumFacing.WEST)) {
			if (!world.getBlockState(pos.east()).getMaterial().isSolid()) {
				this.breakBlock(world, pos, state);
				world.setBlockToAir(pos);
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof IInventory) {
			InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
		}
		this.notifyNeighbors(world, pos, state);
		super.breakBlock(world, pos.west(), state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySingleItem();
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing facing = (EnumFacing) state.getProperties().get(FACING);
		worldIn.notifyNeighborsOfStateChange(pos, this, false);
		worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this, false);
	}

}