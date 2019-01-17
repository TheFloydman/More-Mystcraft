/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.block;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkOptions;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;
import com.xcompwiz.mystcraft.world.agedata.AgeData;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementBook.IGuiOnLinkHandler;
import com.xcompwiz.mystcraft.data.ModBlocks;
import com.xcompwiz.mystcraft.data.ModItems;
import com.xcompwiz.mystcraft.item.ItemAgebook;
import com.xcompwiz.mystcraft.item.ItemLinkbook;

import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.portal.MoreMystcraftPortalUtils;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;

public class BlockUnstablePortal extends BlockBreakable {
	public static final PropertyEnum<EnumFacing> SOURCE_DIRECTION;
	public static final PropertyBool IS_PART_OF_PORTAL;
	public static final PropertyEnum<EnumFacing.Axis> RENDER_ROTATION;
	public static final PropertyBool HAS_ROTATION;

	public BlockUnstablePortal() {
		super(Material.PORTAL, false);
		this.setTickRandomly(true);
		this.setBlockUnbreakable();
		this.setSoundType(SoundType.GLASS);
		this.setLightLevel(0.75f);
		this.setUnlocalizedName("moremystcraft.unstable_portal");
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty((IProperty) BlockUnstablePortal.HAS_ROTATION, (Comparable) false)
				.withProperty((IProperty) BlockUnstablePortal.RENDER_ROTATION, (Comparable) EnumFacing.Axis.X)
				.withProperty((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL, (Comparable) false)
				.withProperty((IProperty) BlockUnstablePortal.SOURCE_DIRECTION, (Comparable) EnumFacing.DOWN));
	}

	public IBlockState getStateFromMeta(final int meta) {
		if (meta == 0) {
			return this.getDefaultState();
		}
		final int sh = meta - 1;
		return this.getDefaultState().withProperty((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL, (Comparable) true)
				.withProperty((IProperty) BlockUnstablePortal.SOURCE_DIRECTION, (Comparable) EnumFacing.values()[sh]);
	}

	public int getMetaFromState(final IBlockState state) {
		final int side = ((EnumFacing) state.getValue((IProperty) BlockUnstablePortal.SOURCE_DIRECTION)).ordinal();
		return (boolean) state.getValue((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL) ? (side + 1) : 0;
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer((Block) this,
				new IProperty[] { BlockUnstablePortal.SOURCE_DIRECTION, BlockUnstablePortal.IS_PART_OF_PORTAL,
						BlockUnstablePortal.HAS_ROTATION, BlockUnstablePortal.RENDER_ROTATION });
	}

	public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
		final List<EnumFacing.Axis> validAxis = Lists.newArrayList();
		boolean has = true;
		EnumFacing offset = EnumFacing.NORTH;
		EnumFacing.Axis axis = EnumFacing.Axis.X;
		for (int i = 0; i < 4; ++i) {
			offset = offset.rotateAround(axis);
			if (!this.isPortalBlock(worldIn.getBlockState(pos.offset(offset)))) {
				has = false;
				break;
			}
		}
		if (has) {
			validAxis.add(axis);
		}
		has = true;
		offset = EnumFacing.NORTH;
		axis = EnumFacing.Axis.Y;
		for (int i = 0; i < 4; ++i) {
			offset = offset.rotateAround(axis);
			if (!this.isPortalBlock(worldIn.getBlockState(pos.offset(offset)))) {
				has = false;
				break;
			}
		}
		if (has) {
			validAxis.add(axis);
		}
		has = true;
		offset = EnumFacing.UP;
		axis = EnumFacing.Axis.Z;
		for (int i = 0; i < 4; ++i) {
			offset = offset.rotateAround(axis);
			if (!this.isPortalBlock(worldIn.getBlockState(pos.offset(offset)))) {
				has = false;
				break;
			}
		}
		if (has) {
			validAxis.add(axis);
		}
		state = state.withProperty((IProperty) BlockUnstablePortal.HAS_ROTATION, (Comparable) (validAxis.size() == 1));
		if (validAxis.size() == 1) {
			state = state.withProperty((IProperty) BlockUnstablePortal.RENDER_ROTATION, (Comparable) validAxis.get(0));
		}
		return state;
	}

	private boolean isPortalBlock(final IBlockState state) {
		return state.getBlock().equals(MoreMystcraftBlocks.UNSTABLE_PORTAL)
				|| state.getBlock().equals(ModBlocks.crystal);
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn,
			final BlockPos pos) {
		return BlockUnstablePortal.NULL_AABB;
	}

	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		float xmin = 0.25f;
		float xmax = 0.75f;
		float ymin = 0.25f;
		float ymax = 0.75f;
		float zmin = 0.25f;
		float zmax = 0.75f;
		if (MoreMystcraftPortalUtils.isValidLinkPortalBlock(source.getBlockState(pos.offset(EnumFacing.WEST))) > 0) {
			xmin = 0.0f;
		}
		if (MoreMystcraftPortalUtils.isValidLinkPortalBlock(source.getBlockState(pos.offset(EnumFacing.EAST))) > 0) {
			xmax = 1.0f;
		}
		if (MoreMystcraftPortalUtils.isValidLinkPortalBlock(source.getBlockState(pos.offset(EnumFacing.DOWN))) > 0) {
			ymin = 0.0f;
		}
		if (MoreMystcraftPortalUtils.isValidLinkPortalBlock(source.getBlockState(pos.offset(EnumFacing.UP))) > 0) {
			ymax = 1.0f;
		}
		if (MoreMystcraftPortalUtils.isValidLinkPortalBlock(source.getBlockState(pos.offset(EnumFacing.NORTH))) > 0) {
			zmin = 0.0f;
		}
		if (MoreMystcraftPortalUtils.isValidLinkPortalBlock(source.getBlockState(pos.offset(EnumFacing.SOUTH))) > 0) {
			zmax = 1.0f;
		}
		return new AxisAlignedBB((double) xmin, (double) ymin, (double) zmin, (double) xmax, (double) ymax,
				(double) zmax);
	}

	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess,
			final BlockPos pos, final EnumFacing side) {
		if (!(boolean) blockState.getValue((IProperty) BlockUnstablePortal.HAS_ROTATION)) {
			IBlockState offset = blockAccess.getBlockState(pos.offset(side));
			offset = offset.getActualState(blockAccess, pos.offset(side));
			return !this.isPortalBlock(offset) || (!offset.getBlock().equals(ModBlocks.crystal)
					&& (boolean) offset.getValue((IProperty) BlockUnstablePortal.HAS_ROTATION));
		}
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@SideOnly(Side.CLIENT)
	public static int colorMultiplier(final IBlockAccess blockAccess, final BlockPos pos) {
		return new Color(255, 0, 0).getRGB();
	}

	public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn,
			final BlockPos fromPos) {
		if (worldIn.isRemote) {
			return;
		}
		MoreMystcraftPortalUtils.validatePortal(worldIn, pos);
	}

	public int quantityDropped(final Random par1Random) {
		return 0;
	}

	public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
		return layer == BlockRenderLayer.TRANSLUCENT;
	}

	public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state,
			final Entity entityIn) {
		if (worldIn.isRemote) {
			return;
		}
		final TileEntity te = MoreMystcraftPortalUtils.getTileEntity((IBlockAccess) worldIn, pos);
		if (te == null || !(te instanceof TileEntityUnstableBookReceptacle)) {
			worldIn.setBlockToAir(pos);
			return;
		}
		final TileEntityUnstableBookReceptacle container = (TileEntityUnstableBookReceptacle) te;
		if (container.getBook().isEmpty()) {
			worldIn.setBlockToAir(pos);
		} else if (container.getBook().getItem().equals(ModItems.agebook)) {
			final ItemStack bookStack = container.getBook().copy();
			ItemAgebook bookItem = (ItemAgebook) bookStack.getItem();
			ItemAgebook.create(bookStack, (EntityPlayer) entityIn,
					bookItem.getPageList((EntityPlayer) entityIn, bookStack), bookItem.getTitle(bookStack));
			bookItem.activate(bookStack, worldIn, entityIn);
		} else if (container.getBook().getItem().equals(ModItems.linkbook)) {
			double x = (Math.random() * 20000) - 10000;
			double y = 255;
			double z = (Math.random() * 20000) - 10000;
			BlockPos spawnPos = new BlockPos(x, y, z);
			final ItemStack bookStack = container.getBook().copy();
			ILinkInfo linkinfo = new LinkOptions(bookStack.getTagCompound());
			LinkOptions.setSpawn(bookStack.getTagCompound(), spawnPos);
			ItemLinkbook bookItem = (ItemLinkbook) bookStack.getItem();
			bookItem.activate(bookStack, worldIn, entityIn);
			BlockPos refinedSpawn = getHighestBlockAt(entityIn.getEntityWorld(), spawnPos);
			int attempts = 0;
			while (refinedSpawn == null && attempts < new MoreMystcraftConfig().getSpawnAttempts()) {
				x = (Math.random() * 20000) - 10000;
				z = (Math.random() * 20000) - 10000;
				spawnPos = new BlockPos(x, y, z);
				refinedSpawn = getHighestBlockAt(entityIn.getEntityWorld(), spawnPos);
				if (attempts == new MoreMystcraftConfig().getSpawnAttempts() - 1) {
					refinedSpawn = spawnPos;
				}
				attempts++;
			}
			entityIn.setPositionAndUpdate(refinedSpawn.getX(), refinedSpawn.getY(), refinedSpawn.getZ());
		}
	}

	private BlockPos getHighestBlockAt(final World world, BlockPos pos) {
		int x = pos.getX();
		int y = 255;
		int z = pos.getZ();
		while (y > 0) {
			pos = new BlockPos(x, y, z);
			Material matCur = world.getBlockState(pos).getMaterial();
			if (matCur.equals(Material.AIR) || matCur.equals(Material.CACTUS) || matCur.equals(Material.FIRE)
					|| matCur.isLiquid()) {
				y--;
			} else {
				pos = pos.up();
				return pos;
			}
		}
		return null;
	}

	public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
		if (worldIn.isRemote) {
			return;
		}
		MoreMystcraftPortalUtils.validatePortal(worldIn, pos);
	}

	public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		if (worldIn.isRemote) {
			return;
		}
	}

	public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
		super.breakBlock(worldIn, pos, state);
	}

	static {
		SOURCE_DIRECTION = PropertyEnum.create("source", (Class) EnumFacing.class);
		IS_PART_OF_PORTAL = PropertyBool.create("active");
		RENDER_ROTATION = PropertyEnum.create("renderface", (Class) EnumFacing.Axis.class);
		HAS_ROTATION = PropertyBool.create("hasface");
	}
}