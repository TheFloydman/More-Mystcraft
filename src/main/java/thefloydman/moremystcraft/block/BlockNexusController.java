package thefloydman.moremystcraft.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.util.Reference;

public class BlockNexusController extends BlockContainer implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockNexusController() {
		super(Material.IRON);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(Reference.MOD_ID + ".nexus_controller");
		this.setRegistryName(Reference.MOD_ID, "nexus_controller");
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystCommon);
		this.setLightLevel(0.5f);
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
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntityNexusController tileEntity = ((TileEntityNexusController) world.getTileEntity(pos));
		tileEntity.setQuery("");
		world.notifyBlockUpdate(pos, MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(),
				MoreMystcraftBlocks.NEXUS_CONTROLLER.getDefaultState(), 3);
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

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		Map<BlockPos, Integer> nexusBlocks = getConnectedNexusBlocks(worldIn, pos, 1);
		boolean placeMod = Collections.frequency(nexusBlocks.values(), 1) < 2;
		boolean placeVanilla = super.canPlaceBlockAt(worldIn, pos);
		if (worldIn.isRemote && (!placeVanilla || !placeMod)) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("message.moremystcraft.nexus_place_error.desc"));
		}
		return placeVanilla && placeMod;
	}

	public static Map<BlockPos, Integer> getConnectedNexusBlocks(World world, BlockPos pos, int type) {
		Map<BlockPos, Integer> finishedBlocks = new HashMap<BlockPos, Integer>();
		Map<BlockPos, Integer> uncheckedBlocks = new HashMap<BlockPos, Integer>();
		uncheckedBlocks.put(pos, type);
		do {
			Map<BlockPos, Integer> map = getAdjacentNexusBlocks(world, pos, finishedBlocks, uncheckedBlocks);			
			for (Map.Entry<BlockPos, Integer> entry : map.entrySet()) {
				if (!uncheckedBlocks.containsKey(entry.getKey()) && !finishedBlocks.containsKey(entry.getKey())) {
					uncheckedBlocks.put(entry.getKey(), entry.getValue());
				}
			}
			finishedBlocks.put(pos, type);
			uncheckedBlocks.remove(pos);
			if (uncheckedBlocks.size() > 0) {
				pos = uncheckedBlocks.entrySet().stream().findFirst().get().getKey();
				type = uncheckedBlocks.entrySet().stream().findFirst().get().getValue();
			}
		} while (uncheckedBlocks.size() > 0);
		return finishedBlocks;
	}

	public static Map<BlockPos, Integer> getAdjacentNexusBlocks(final World world, final BlockPos pos,
			Map<BlockPos, Integer> checkedBlocks, Map<BlockPos, Integer> uncheckedBlocks) {
		Map<BlockPos, Integer> blocks = new HashMap<BlockPos, Integer>();
		BlockPos[] positions = new BlockPos[] { pos.down(), pos.north(), pos.east(), pos.south(), pos.west(),
				pos.up() };
		for (BlockPos position : positions) {
			if (checkedBlocks.containsKey(position)) {
				continue;
			}
			if (world.getBlockState(position).getBlock() instanceof BlockNexusStorage) {
				blocks.put(position, 0);
			} else if (world.getBlockState(position).getBlock() instanceof BlockNexusController) {
				blocks.put(position, 1);
			}
		}
		return blocks;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		/*worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos),
				worldIn.getBlockState(pos), 3);*/
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

}
