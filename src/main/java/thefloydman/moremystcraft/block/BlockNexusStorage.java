package thefloydman.moremystcraft.block;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.util.Reference;

public class BlockNexusStorage extends Block {

	public static final AxisAlignedBB TRAFFIC_CONE_AABB = new AxisAlignedBB(0.1875D, 0D, 0.1875D, 0.8125D, 0.6875D,
			0.8125D);
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

	public BlockNexusStorage() {

		super(Material.IRON);
		this.setHardness(2.5f);
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName("moremystcraft.traffic_cone");
		this.setRegistryName(Reference.forMoreMystcraft("traffic_cone"));
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);
		this.setDefaultState(blockState.getBaseState().withProperty(ROTATION, 0));

	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ROTATION, meta);

	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { ROTATION });
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
		return TRAFFIC_CONE_AABB;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {

		if (state.getValue(ROTATION) == 3) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(ROTATION, 0));
		} else {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(ROTATION, state.getValue(ROTATION) + 1));
		}
		return true;

	}
}