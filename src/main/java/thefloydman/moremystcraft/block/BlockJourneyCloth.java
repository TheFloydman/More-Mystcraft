package thefloydman.moremystcraft.block;

import java.util.UUID;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import thefloydman.moremystcraft.entity.capability.CapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.entity.capability.ProviderJourneyClothsCollectedCapability;
import thefloydman.moremystcraft.tileentity.TileEntityJourneyCloth;
import thefloydman.moremystcraft.tileentity.capability.CapabilityUUID;
import thefloydman.moremystcraft.tileentity.capability.ProviderUUIDCapability;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.Reference;

public class BlockJourneyCloth extends BlockHorizontal implements ITileEntityProvider {

	public static final AxisAlignedBB JOURNEY_CLOTH_AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
	public static final AxisAlignedBB JOURNEY_CLOTH_AABB_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB JOURNEY_CLOTH_AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB JOURNEY_CLOTH_AABB_EAST = new AxisAlignedBB(1.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public final JourneyClothUtils.ClothType TYPE;

	public BlockJourneyCloth(JourneyClothUtils.ClothType type) {

		super(Material.CLOTH);
		this.TYPE = type;
		this.setHardness(0.5f);
		this.setSoundType(SoundType.CLOTH);
		this.setUnlocalizedName(Reference.MOD_ID + ".journey_cloth_" + type.name().toLowerCase());
		this.setRegistryName(Reference.forMoreMystcraft("journey_cloth_" + type.name().toLowerCase()));
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);
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
			return JOURNEY_CLOTH_AABB_NORTH;
		case 1:
			return JOURNEY_CLOTH_AABB_EAST;
		case 2:
			return JOURNEY_CLOTH_AABB_SOUTH;
		case 3:
			return JOURNEY_CLOTH_AABB_WEST;
		}
		return JOURNEY_CLOTH_AABB_NORTH;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		boolean mod = (!side.equals(EnumFacing.UP) && !side.equals(EnumFacing.DOWN));
		boolean vanilla = super.canPlaceBlockOnSide(world, pos, side);
		return mod && vanilla;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		boolean mod = world.isAirBlock(pos);
		boolean vanilla = super.canPlaceBlockAt(world, pos);
		return mod && vanilla;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityJourneyCloth) {
				CapabilityUUID capStack = ((TileEntityJourneyCloth) tileEntity).getCloth()
						.getCapability(ProviderUUIDCapability.UUID, facing);
				UUID uuid = capStack.getUUID();
				if (uuid != null) {
					CapabilityJourneyClothsCollected capPlayer = player
							.getCapability(ProviderJourneyClothsCollectedCapability.JOURNEY_CLOTH, facing);
					capPlayer.addCloth(uuid);
				}
			}
		}

		return true;

	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!world.isRemote) {
			if (world.getBlockState(fromPos).getMaterial().isLiquid()) {
				this.dropBlockAsItem(world, pos, state, 0);
				world.setBlockToAir(pos);
			} else if (state.getProperties().get(FACING).equals(EnumFacing.NORTH)) {
				if (!world.getBlockState(pos.south()).getMaterial().isSolid()) {
					this.dropBlockAsItem(world, pos, state, 0);
					world.setBlockToAir(pos);
				}
			} else if (state.getProperties().get(FACING).equals(EnumFacing.EAST)) {
				if (!world.getBlockState(pos.west()).getMaterial().isSolid()) {
					this.dropBlockAsItem(world, pos, state, 0);
					world.setBlockToAir(pos);
				}
			} else if (state.getProperties().get(FACING).equals(EnumFacing.SOUTH)) {
				if (!world.getBlockState(pos.north()).getMaterial().isSolid()) {
					this.dropBlockAsItem(world, pos, state, 0);
					world.setBlockToAir(pos);
				}
			} else if (state.getProperties().get(FACING).equals(EnumFacing.WEST)) {
				if (!world.getBlockState(pos.east()).getMaterial().isSolid()) {
					this.dropBlockAsItem(world, pos, state, 0);
					world.setBlockToAir(pos);
				}
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityJourneyCloth) {
				CapabilityUUID capStack = stack.getCapability(ProviderUUIDCapability.UUID, null);
				if (capStack.getUUID() == null) {
					capStack.setUUID(UUID.randomUUID());
				}
				TileEntityJourneyCloth clothEntity = (TileEntityJourneyCloth) tileEntity;
				clothEntity.insertItem(0, stack, false);
			}
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityJourneyCloth();
	}
}