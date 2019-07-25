package thefloydman.moremystcraft.block;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.util.Reference;

public class BlockNexusStorage extends BlockHorizontal {

	public BlockNexusStorage() {

		super(Material.IRON);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName("moremystcraft.nexus_storage");
		this.setRegistryName(Reference.forMoreMystcraft("nexus_storage"));
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);

	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));

	}

	@Override
	public int getMetaFromState(IBlockState state) {

		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();

	}
}