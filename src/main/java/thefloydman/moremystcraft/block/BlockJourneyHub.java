package thefloydman.moremystcraft.block;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.capability.ICapabilityHub;
import thefloydman.moremystcraft.capability.ProviderCapabilityHub;
import thefloydman.moremystcraft.data.worldsaveddata.MoreMystcraftSavedDataPerSave;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.MoreMystcraftCreativeTabs;
import thefloydman.moremystcraft.util.Reference;

public class BlockJourneyHub extends BlockHorizontal implements ITileEntityProvider {

	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
	public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(1.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public final JourneyClothUtils.Type TYPE;

	public BlockJourneyHub(JourneyClothUtils.Type type) {

		super(Material.IRON);
		this.TYPE = type;
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(Reference.MOD_ID + ".journey_hub_" + type.name().toLowerCase());
		this.setRegistryName(Reference.forMoreMystcraft("journey_hub_" + type.name().toLowerCase()));
		this.setCreativeTab(MoreMystcraftCreativeTabs.MORE_MYSTCRAFT);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED,
				Boolean.valueOf(false)));

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
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		int power = 0;
		TileEntity tileEntity = blockAccess.getTileEntity(pos);
		if (tileEntity instanceof TileEntitySingleItem) {
			ICapabilityHub capStack = ((TileEntitySingleItem) tileEntity).getItem()
					.getCapability(ProviderCapabilityHub.UUID_LIST, side);
			MoreMystcraftSavedDataPerSave data = MoreMystcraftSavedDataPerSave.get(tileEntity.getWorld());
			List<UUID> uuids = capStack.getUUIDs();
			for (UUID id : uuids) {
				power += data.journeyClothActivatedByAnyone(id) ? 1 : 0;
			}
		}
		return MathHelper.clamp(power, 0, 15);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySingleItem();
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
		return new BlockStateContainer(this, new IProperty[] { FACING, POWERED });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, Boolean.valueOf(false));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntitySingleItem) {
				ItemStack newStack = stack.copy();
				newStack.setCount(1);
				TileEntitySingleItem hubEntity = (TileEntitySingleItem) tileEntity;
				hubEntity.setItem(newStack);
			}
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof IInventory) {
			InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (!world.isRemote) {
			if (!this.isPowered(state)) {
				world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)));
				world.notifyNeighborsOfStateChange(pos, this, false);
				world.scheduleBlockUpdate(pos, this, this.tickRate(world), 0);
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity instanceof TileEntitySingleItem) {
					ICapabilityHub capStack = ((TileEntitySingleItem) tileEntity).getItem()
							.getCapability(ProviderCapabilityHub.UUID_LIST, facing);
					List<UUID> uuids = capStack.getUUIDs();
					for (UUID id : uuids) {
						System.out.println(id);
					}
				}
			}
		}

		return true;

	}

	public boolean isPowered(IBlockState state) {
		return state.getProperties().get(POWERED).equals(Boolean.valueOf(true));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			if (((Boolean) state.getValue(POWERED)).booleanValue()) {
				world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
				world.notifyNeighborsOfStateChange(pos, this, false);
				world.markBlockRangeForRenderUpdate(pos, pos);
			}
		}
	}

	@Override
	public int tickRate(World worldIn) {
		return 100;
	}

}