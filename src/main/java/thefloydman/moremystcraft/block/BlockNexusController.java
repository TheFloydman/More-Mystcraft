package thefloydman.moremystcraft.block;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.util.MoreMystcraftCreativeTabs;
import thefloydman.moremystcraft.util.Reference;

public class BlockNexusController extends BlockContainer implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool IN_USE = PropertyBool.create("in_use");

	public BlockNexusController() {
		super(Material.IRON);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(Reference.MOD_ID + ".nexus_controller");
		this.setRegistryName(Reference.MOD_ID, "nexus_controller");
		this.setCreativeTab(MoreMystcraftCreativeTabs.MORE_MYSTCRAFT);
		this.setLightLevel(0.5f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(IN_USE, Boolean.valueOf(false)));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, IN_USE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (world.isRemote || ((Boolean) state.getValue(IN_USE)).booleanValue()) {
			return false;
		}
		world.setBlockState(pos, state.withProperty(IN_USE, Boolean.valueOf(true)));
		((TileEntityNexusController) world.getTileEntity(pos)).setQuery("");
		world.notifyBlockUpdate(pos, MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(),
				MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(), 7);
		player.openGui((Object) MoreMystcraft.instance, MoreMystcraftGUIs.NEXUS_CONTROLLER.ordinal(), world, pos.getX(),
				pos.getY(), pos.getZ());
		return true;
	}

	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityNexusController();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

}