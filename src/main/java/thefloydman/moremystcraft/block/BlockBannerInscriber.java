package thefloydman.moremystcraft.block;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;

public class BlockBannerInscriber extends BlockContainer implements ITileEntityProvider {

	public BlockBannerInscriber() {
		super(Material.IRON);
		this.setHardness(2.5f);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName("moremystcraft.banner_inscriber");
		this.setRegistryName("moremystcraft", "banner_inscriber");
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);
		this.setLightLevel(0.0f);
	}

	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (world.isRemote) {
			return true;
		}
		player.openGui((Object) MoreMystcraft.instance, MoreMystcraftGUIs.BANNER_INSCRIBER.ordinal(), world, pos.getX(),
				pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityNexusController();
	}

}
