package thefloydman.moremystcraft.block;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.data.MoreMystcraftSavedDataPerDimension;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.Reference;

public class BlockJourneyClothHub extends Block {

	public static final AxisAlignedBB JOURNEY_CLOTH_CONSOLE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

	public BlockJourneyClothHub() {

		super(Material.IRON);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(Reference.MOD_ID + ".journey_cloth_hub");
		this.setRegistryName(Reference.forMoreMystcraft("journey_cloth_hub"));
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);

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
		return JOURNEY_CLOTH_CONSOLE_AABB;
	}
	
}