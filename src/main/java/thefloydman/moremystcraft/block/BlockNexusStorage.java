package thefloydman.moremystcraft.block;

import java.util.Collections;
import java.util.Map;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
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
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		Map<BlockPos, Integer> nexusBlocks = BlockNexusController.getConnectedNexusBlocks(worldIn, pos, 0);
		boolean placeMod = Collections.frequency(nexusBlocks.values(), 1) < 2;
		boolean placeVanilla = super.canPlaceBlockAt(worldIn, pos);
		if (worldIn.isRemote && (!placeVanilla || !placeMod)) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("message.moremystcraft.nexus_place_error.desc"));
		}
		return placeVanilla && placeMod;
	}
}