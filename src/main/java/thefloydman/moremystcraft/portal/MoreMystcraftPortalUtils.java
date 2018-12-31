/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.portal;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.xcompwiz.mystcraft.block.BlockCrystal;
import com.xcompwiz.mystcraft.data.ModBlocks;

import thefloydman.moremystcraft.block.BlockUnstableBookReceptacle;
import thefloydman.moremystcraft.block.BlockUnstablePortal;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;

public final class MoreMystcraftPortalUtils {
	public static Block getPortalBlock() {
		return MoreMystcraftBlocks.UNSTABLE_PORTAL;
	}

	public static Block getFrameBlock() {
		return ModBlocks.crystal;
	}

	public static Block getReceptacleBlock() {
		return MoreMystcraftBlocks.UNSTABLE_RECEPTACLE;
	}

	public static int isValidLinkPortalBlock(final IBlockState blockstate) {
		if (blockstate.getBlock() == getFrameBlock()) {
			return 1;
		}
		if (blockstate.getBlock() == getPortalBlock()) {
			return 1;
		}
		return 0;
	}

	private static EnumFacing getBlockFacing(final IBlockState blockstate) {
		if (blockstate.getBlock() == getFrameBlock()) {
			return (EnumFacing) blockstate.getValue((IProperty) BlockCrystal.SOURCE_DIRECTION);
		}
		if (blockstate.getBlock() == getPortalBlock()) {
			return (EnumFacing) blockstate.getValue((IProperty) BlockUnstablePortal.SOURCE_DIRECTION);
		}
		if (blockstate.getBlock() == getReceptacleBlock()) {
			return (EnumFacing) blockstate.getValue((IProperty) BlockUnstableBookReceptacle.ROTATION);
		}
		return EnumFacing.DOWN;
	}

	private static boolean isBlockActive(final IBlockState blockstate) {
		if (blockstate.getBlock() == getFrameBlock()) {
			return (boolean) blockstate.getValue((IProperty) BlockCrystal.IS_PART_OF_PORTAL);
		}
		return blockstate.getBlock() == getPortalBlock()
				&& (boolean) blockstate.getValue((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL);
	}

	private static IBlockState getDirectedState(final IBlockState blockstate, final int m) {
		if (blockstate.getBlock() == getFrameBlock()) {
			return blockstate.withProperty((IProperty) BlockCrystal.IS_PART_OF_PORTAL, (Comparable) true)
					.withProperty((IProperty) BlockCrystal.SOURCE_DIRECTION, (Comparable) EnumFacing.values()[m]);
		}
		if (blockstate.getBlock() == getPortalBlock()) {
			return blockstate.withProperty((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL, (Comparable) true)
					.withProperty((IProperty) BlockUnstablePortal.SOURCE_DIRECTION,
							(Comparable) EnumFacing.values()[m]);
		}
		return blockstate;
	}

	private static IBlockState getDisabledState(final IBlockState blockstate) {
		if (blockstate.getBlock() == getFrameBlock()) {
			return blockstate.withProperty((IProperty) BlockCrystal.IS_PART_OF_PORTAL, (Comparable) false);
		}
		if (blockstate.getBlock() == getPortalBlock()) {
			return blockstate.withProperty((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL, (Comparable) false);
		}
		return blockstate;
	}

	public static void validatePortal(final World world, final BlockPos start) {
		if (world.isRemote) {
			return;
		}
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		blocks.add(start);
		while (blocks.size() > 0) {
			final BlockPos coords = blocks.remove(0);
			if (world.getBlockState(coords).getBlock() != getPortalBlock()) {
				continue;
			}
			validatePortal(world, coords, blocks);
		}
	}

	public static void firePortal(final World world, final BlockPos pos) {
		final BlockPos coord = getReceptacleBase(pos, getBlockFacing(world.getBlockState(pos)));
		onpulse(world, coord);
		pathto(world, pos);
	}

	public static void shutdownPortal(final World world, final BlockPos pos) {
		unpath(world, pos);
	}

	public static BlockPos getReceptacleBase(final BlockPos pos, final EnumFacing facing) {
		return pos.offset(facing.getOpposite());
	}

	private static void pathto(final World world, final BlockPos pos) {
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		final List<BlockPos> portals = new LinkedList<BlockPos>();
		final List<BlockPos> repath = new LinkedList<BlockPos>();
		final List<BlockPos> redraw = new LinkedList<BlockPos>();
		blocks.add(pos);
		while (portals.size() > 0 || blocks.size() > 0) {
			while (blocks.size() > 0) {
				final BlockPos coords = blocks.remove(0);
				directPortal(world, coords.east(), 5, blocks, portals);
				directPortal(world, coords.up(), 1, blocks, portals);
				directPortal(world, coords.south(), 3, blocks, portals);
				directPortal(world, coords.west(), 6, blocks, portals);
				directPortal(world, coords.down(), 2, blocks, portals);
				directPortal(world, coords.north(), 4, blocks, portals);
				redraw.add(coords);
			}
			if (portals.size() > 0) {
				final BlockPos coords = portals.remove(0);
				directPortal(world, coords.east(), 5, blocks, portals);
				directPortal(world, coords.up(), 1, blocks, portals);
				directPortal(world, coords.south(), 3, blocks, portals);
				directPortal(world, coords.west(), 6, blocks, portals);
				directPortal(world, coords.down(), 2, blocks, portals);
				directPortal(world, coords.north(), 4, blocks, portals);
				if (world.getBlockState(coords).getBlock() != getPortalBlock()) {
					continue;
				}
				repath.add(coords);
			}
		}
		while (repath.size() > 0) {
			final BlockPos coords = repath.remove(0);
			if (world.getBlockState(coords).getBlock() == getPortalBlock()) {
				if (!isPortalBlockStable(world, coords)) {
					repathNeighbors(world, coords);
					world.setBlockState(coords, Blocks.AIR.getDefaultState(), 0);
					addSurrounding(repath, coords);
				} else {
					redraw.add(coords);
				}
			}
		}
		for (final BlockPos coords2 : redraw) {
			if (world.isChunkGeneratedAt(coords2.getX() >> 4, coords2.getZ() >> 4)) {
				final IBlockState blockState = world.getBlockState(coords2);
				world.notifyBlockUpdate(coords2, blockState, blockState, 3);
			}
		}
	}

	private static void repathNeighbors(final World world, final BlockPos pos) {
		final TileEntity tileentity = getTileEntity((IBlockAccess) world, pos);
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		blocks.add(pos);
		world.setBlockState(pos, getDisabledState(world.getBlockState(pos)), 2);
		while (blocks.size() > 0) {
			final BlockPos coords = blocks.remove(0);
			redirectPortal(world, tileentity, coords.east(), 5, blocks);
			redirectPortal(world, tileentity, coords.up(), 1, blocks);
			redirectPortal(world, tileentity, coords.south(), 3, blocks);
			redirectPortal(world, tileentity, coords.west(), 6, blocks);
			redirectPortal(world, tileentity, coords.down(), 2, blocks);
			redirectPortal(world, tileentity, coords.north(), 4, blocks);
		}
	}

	private static void redirectPortal(final World world, final TileEntity tileentity, final BlockPos pos,
			final int meta, final List<BlockPos> blocks) {
		final IBlockState blockstate = world.getBlockState(pos);
		if (isValidLinkPortalBlock(blockstate) == 0) {
			return;
		}
		if (isBlockActive(blockstate) && getBlockFacing(blockstate).ordinal() + 1 == meta) {
			for (int m = 1; m < 7; ++m) {
				if (m != meta) {
					world.setBlockState(pos, getDirectedState(blockstate, m - 1), 2);
					final TileEntity local = getTileEntity((IBlockAccess) world, pos);
					if (local == tileentity || (local != null && tileentity == null)) {
						return;
					}
				}
			}
			world.setBlockState(pos, blockstate.getBlock().getDefaultState(), 2);
		}
	}

	private static void unpath(final World world, final BlockPos pos) {
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		final List<BlockPos> notify = new LinkedList<BlockPos>();
		blocks.add(pos);
		while (blocks.size() > 0) {
			final BlockPos coords = blocks.remove(0);
			depolarize(world, coords.east(), blocks);
			depolarize(world, coords.up(), blocks);
			depolarize(world, coords.south(), blocks);
			depolarize(world, coords.west(), blocks);
			depolarize(world, coords.down(), blocks);
			depolarize(world, coords.north(), blocks);
			notify.add(coords);
		}
		for (final BlockPos coords2 : notify) {
			if (world.isChunkGeneratedAt(coords2.getX() >> 4, coords2.getZ() >> 4)) {
				final IBlockState blockState = world.getBlockState(coords2);
				world.notifyBlockUpdate(coords2, blockState, blockState, 3);
			}
		}
	}

	private static void onpulse(final World world, final BlockPos pos) {
		final LinkedList<BlockPos> set = new LinkedList<BlockPos>();
		final Stack<BlockPos> validate = new Stack<BlockPos>();
		addSurrounding(set, pos);
		while (set.size() > 0) {
			final BlockPos coords = set.remove(0);
			expandPortal(world, coords, set, validate);
		}
		while (validate.size() > 0) {
			final BlockPos coords = validate.pop();
			if (!checkPortalTension(world, coords)) {
				world.setBlockState(coords, Blocks.AIR.getDefaultState(), 0);
			}
		}
	}

	private static boolean isPortalBlockStable(final World world, final BlockPos pos) {
		return world.isRemote || (checkPortalTension(world, pos) && getTileEntity((IBlockAccess) world, pos) != null);
	}

	private static boolean checkPortalTension(final World world, final BlockPos pos) {
		if (world.isRemote) {
			return true;
		}
		int score = 0;
		if (isValidLinkPortalBlock(world.getBlockState(pos.east())) > 0
				&& isValidLinkPortalBlock(world.getBlockState(pos.west())) > 0) {
			++score;
		}
		if (isValidLinkPortalBlock(world.getBlockState(pos.up())) > 0
				&& isValidLinkPortalBlock(world.getBlockState(pos.down())) > 0) {
			++score;
		}
		if (isValidLinkPortalBlock(world.getBlockState(pos.south())) > 0
				&& isValidLinkPortalBlock(world.getBlockState(pos.north())) > 0) {
			++score;
		}
		return score > 1;
	}

	private static void validatePortal(final World world, final BlockPos pos, final Collection<BlockPos> blocks) {
		if (!isPortalBlockStable(world, pos)) {
			world.setBlockToAir(pos);
			addSurrounding(blocks, pos);
		}
	}

	private static void addSurrounding(final Collection<BlockPos> set, final BlockPos pos) {
		set.add(new BlockPos((Vec3i) pos.east()));
		set.add(new BlockPos((Vec3i) pos.west()));
		set.add(new BlockPos((Vec3i) pos.up()));
		set.add(new BlockPos((Vec3i) pos.down()));
		set.add(new BlockPos((Vec3i) pos.south()));
		set.add(new BlockPos((Vec3i) pos.north()));
		set.add(new BlockPos((Vec3i) pos.east().up()));
		set.add(new BlockPos((Vec3i) pos.west().up()));
		set.add(new BlockPos((Vec3i) pos.east().down()));
		set.add(new BlockPos((Vec3i) pos.west().down()));
		set.add(new BlockPos((Vec3i) pos.south().up()));
		set.add(new BlockPos((Vec3i) pos.north().up()));
		set.add(new BlockPos((Vec3i) pos.south().down()));
		set.add(new BlockPos((Vec3i) pos.north().down()));
		set.add(new BlockPos((Vec3i) pos.east().south()));
		set.add(new BlockPos((Vec3i) pos.west().south()));
		set.add(new BlockPos((Vec3i) pos.east().north()));
		set.add(new BlockPos((Vec3i) pos.west().north()));
	}

	private static void expandPortal(final World world, final BlockPos pos, final Collection<BlockPos> set,
			final Stack<BlockPos> created) {
		if (!world.isAirBlock(pos)) {
			return;
		}
		final int score = isValidLinkPortalBlock(world.getBlockState(pos.east()))
				+ isValidLinkPortalBlock(world.getBlockState(pos.west()))
				+ isValidLinkPortalBlock(world.getBlockState(pos.up()))
				+ isValidLinkPortalBlock(world.getBlockState(pos.down()))
				+ isValidLinkPortalBlock(world.getBlockState(pos.south()))
				+ isValidLinkPortalBlock(world.getBlockState(pos.north()));
		if (score > 1) {
			world.setBlockState(pos, getPortalBlock().getDefaultState(), 0);
			created.push(pos);
			addSurrounding(set, pos);
		}
	}

	private static void directPortal(final World world, final BlockPos pos, final int meta, final List<BlockPos> blocks,
			final List<BlockPos> portals) {
		final IBlockState blockState = world.getBlockState(pos);
		if (isValidLinkPortalBlock(blockState) == 0) {
			return;
		}
		if (isBlockActive(blockState)) {
			return;
		}
		world.setBlockState(pos, getDirectedState(blockState, meta - 1), 0);
		if (blockState.getBlock() == getPortalBlock()) {
			portals.add(pos);
		} else {
			blocks.add(pos);
		}
	}

	private static void depolarize(final World world, final BlockPos pos, final List<BlockPos> blocks) {
		final IBlockState blockstate = world.getBlockState(pos);
		if (isValidLinkPortalBlock(blockstate) == 0) {
			return;
		}
		if (!isBlockActive(blockstate)) {
			return;
		}
		world.setBlockState(pos, blockstate.getBlock().getDefaultState(), 0);
		if (blockstate.getBlock() == getPortalBlock() && !isPortalBlockStable(world, pos)) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
		blocks.add(pos);
	}

	public static TileEntity getTileEntity(final IBlockAccess blockaccess, BlockPos pos) {
		final HashSet<BlockPos> visited = new HashSet<BlockPos>();
		for (IBlockState blockstate = blockaccess.getBlockState(pos); blockstate
				.getBlock() != getReceptacleBlock(); blockstate = blockaccess.getBlockState(pos)) {
			if (isValidLinkPortalBlock(blockstate) == 0) {
				return null;
			}
			if (!isBlockActive(blockstate)) {
				return null;
			}
			if (!visited.add(pos)) {
				return null;
			}
			pos = pos.offset(getBlockFacing(blockstate));
		}
		return blockaccess.getTileEntity(pos);
	}
}