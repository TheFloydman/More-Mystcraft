package thefloydman.moremystcraft.block;

import java.util.Collections;
import java.util.Map;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
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
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentTranslation("message.moremystcraft.nexus_place_error.desc"));
		}
		return placeVanilla && placeMod;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		Map<BlockPos, Integer> nexusBlocks = BlockNexusController.getConnectedNexusBlocks(worldIn, pos, 0);
		BlockPos controllerPos = getKey(nexusBlocks, 1);
		worldIn.notifyBlockUpdate(controllerPos, worldIn.getBlockState(controllerPos),
				worldIn.getBlockState(controllerPos), 3);
	}

	/*
	 * Source as of 26 July 2019: https://www.baeldung.com/java-map-key-from-value
	 */
	protected <K, V> K getKey(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		Map<BlockPos, Integer> nexusBlocks = BlockNexusController.getConnectedNexusBlocks(worldIn, pos, 0);
		BlockPos controllerPos = getKey(nexusBlocks, 1);
		worldIn.notifyBlockUpdate(controllerPos, worldIn.getBlockState(controllerPos),
				worldIn.getBlockState(controllerPos), 3);
		super.breakBlock(worldIn, pos, state);
	}

}