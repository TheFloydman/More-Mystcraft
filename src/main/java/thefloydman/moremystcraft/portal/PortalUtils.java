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

public abstract class PortalUtils {

	public static Block getReceptacleBlock() {
		return MoreMystcraftBlocks.UNSTABLE_RECEPTACLE;
	}

	public static int isValidLinkPortalBlock(final IBlockState blockstate, final Block frameBlock,
			final Block portalBlock) {
		if (blockstate.getBlock().equals(frameBlock)) {
			return 1;
		}
		if (blockstate.getBlock().equals(portalBlock)) {
			return 1;
		}
		return 0;
	}

	private static EnumFacing getBlockFacing(final IBlockState blockstate, final Block frameBlock,
			final Block portalBlock) {
		if (blockstate.getBlock() == frameBlock) {
			return (EnumFacing) blockstate.getValue((IProperty) BlockCrystal.SOURCE_DIRECTION);
		}
		if (blockstate.getBlock() == portalBlock) {
			return (EnumFacing) blockstate.getValue((IProperty) BlockUnstablePortal.SOURCE_DIRECTION);
		}
		if (blockstate.getBlock() == getReceptacleBlock()) {
			return (EnumFacing) blockstate.getValue((IProperty) BlockUnstableBookReceptacle.ROTATION);
		}
		return EnumFacing.DOWN;
	}

	private static boolean isBlockActive(final IBlockState blockstate, final Block frameBlock,
			final Block portalBlock) {
		if (blockstate.getBlock() == frameBlock) {
			return (boolean) blockstate.getValue((IProperty) BlockCrystal.IS_PART_OF_PORTAL);
		}
		return blockstate.getBlock() == portalBlock
				&& (boolean) blockstate.getValue((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL);
	}

	private static IBlockState getDirectedState(final IBlockState blockstate, final int m, final Block frameBlock,
			final Block portalBlock) {
		if (blockstate.getBlock() == frameBlock) {
			return blockstate.withProperty((IProperty) BlockCrystal.IS_PART_OF_PORTAL, (Comparable) true)
					.withProperty((IProperty) BlockCrystal.SOURCE_DIRECTION, (Comparable) EnumFacing.values()[m]);
		}
		if (blockstate.getBlock() == portalBlock) {
			return blockstate.withProperty((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL, (Comparable) true)
					.withProperty((IProperty) BlockUnstablePortal.SOURCE_DIRECTION,
							(Comparable) EnumFacing.values()[m]);
		}
		return blockstate;
	}

	private static IBlockState getDisabledState(final IBlockState blockstate, final Block frameBlock,
			final Block portalBlock) {
		if (blockstate.getBlock() == frameBlock) {
			return blockstate.withProperty((IProperty) BlockCrystal.IS_PART_OF_PORTAL, (Comparable) false);
		}
		if (blockstate.getBlock() == portalBlock) {
			return blockstate.withProperty((IProperty) BlockUnstablePortal.IS_PART_OF_PORTAL, (Comparable) false);
		}
		return blockstate;
	}

	public static void validatePortal(final World world, final BlockPos start, final Block frameBlock,
			final Block portalBlock) {
		if (world.isRemote) {
			return;
		}
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		blocks.add(start);
		while (blocks.size() > 0) {
			final BlockPos coords = blocks.remove(0);
			if (world.getBlockState(coords).getBlock() != portalBlock) {
				continue;
			}
			validatePortal(world, coords, blocks, frameBlock, portalBlock);
		}
	}

	public static void firePortal(final World world, final BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		final BlockPos coord = getReceptacleBase(pos,
				getBlockFacing(world.getBlockState(pos), frameBlock, portalBlock));
		onpulse(world, coord, frameBlock, portalBlock);
		pathto(world, pos, frameBlock, portalBlock);
	}

	public static void shutdownPortal(final World world, final BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		unpath(world, pos, frameBlock, portalBlock);
	}

	public static BlockPos getReceptacleBase(final BlockPos pos, final EnumFacing facing) {
		return pos.offset(facing.getOpposite());
	}

	private static void pathto(final World world, final BlockPos pos, final Block frameBlock, final Block portalBlock) {
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		final List<BlockPos> portals = new LinkedList<BlockPos>();
		final List<BlockPos> repath = new LinkedList<BlockPos>();
		final List<BlockPos> redraw = new LinkedList<BlockPos>();
		blocks.add(pos);
		while (portals.size() > 0 || blocks.size() > 0) {
			while (blocks.size() > 0) {
				final BlockPos coords = blocks.remove(0);
				directPortal(world, coords.east(), 5, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.up(), 1, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.south(), 3, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.west(), 6, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.down(), 2, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.north(), 4, blocks, portals, frameBlock, portalBlock);
				redraw.add(coords);
			}
			if (portals.size() > 0) {
				final BlockPos coords = portals.remove(0);
				directPortal(world, coords.east(), 5, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.up(), 1, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.south(), 3, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.west(), 6, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.down(), 2, blocks, portals, frameBlock, portalBlock);
				directPortal(world, coords.north(), 4, blocks, portals, frameBlock, portalBlock);
				if (world.getBlockState(coords).getBlock() != portalBlock) {
					continue;
				}
				repath.add(coords);
			}
		}
		while (repath.size() > 0) {
			final BlockPos coords = repath.remove(0);
			if (world.getBlockState(coords).getBlock() == portalBlock) {
				if (!isPortalBlockStable(world, coords, frameBlock, portalBlock)) {
					repathNeighbors(world, coords, frameBlock, portalBlock);
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

	private static void repathNeighbors(final World world, final BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		final TileEntity tileentity = getTileEntity((IBlockAccess) world, pos, frameBlock, portalBlock);
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		blocks.add(pos);
		world.setBlockState(pos, getDisabledState(world.getBlockState(pos), frameBlock, portalBlock), 2);
		while (blocks.size() > 0) {
			final BlockPos coords = blocks.remove(0);
			redirectPortal(world, tileentity, coords.east(), 5, blocks, frameBlock, portalBlock);
			redirectPortal(world, tileentity, coords.up(), 1, blocks, frameBlock, portalBlock);
			redirectPortal(world, tileentity, coords.south(), 3, blocks, frameBlock, portalBlock);
			redirectPortal(world, tileentity, coords.west(), 6, blocks, frameBlock, portalBlock);
			redirectPortal(world, tileentity, coords.down(), 2, blocks, frameBlock, portalBlock);
			redirectPortal(world, tileentity, coords.north(), 4, blocks, frameBlock, portalBlock);
		}
	}

	private static void redirectPortal(final World world, final TileEntity tileentity, final BlockPos pos,
			final int meta, final List<BlockPos> blocks, final Block frameBlock, final Block portalBlock) {
		final IBlockState blockstate = world.getBlockState(pos);
		if (isValidLinkPortalBlock(blockstate, frameBlock, portalBlock) == 0) {
			return;
		}
		if (isBlockActive(blockstate, frameBlock, portalBlock)
				&& getBlockFacing(blockstate, frameBlock, portalBlock).ordinal() + 1 == meta) {
			for (int m = 1; m < 7; ++m) {
				if (m != meta) {
					world.setBlockState(pos, getDirectedState(blockstate, m - 1, frameBlock, portalBlock), 2);
					final TileEntity local = getTileEntity((IBlockAccess) world, pos, frameBlock, portalBlock);
					if (local == tileentity || (local != null && tileentity == null)) {
						return;
					}
				}
			}
			world.setBlockState(pos, blockstate.getBlock().getDefaultState(), 2);
		}
	}

	private static void unpath(final World world, final BlockPos pos, final Block frameBlock, final Block portalBlock) {
		final List<BlockPos> blocks = new LinkedList<BlockPos>();
		final List<BlockPos> notify = new LinkedList<BlockPos>();
		blocks.add(pos);
		while (blocks.size() > 0) {
			final BlockPos coords = blocks.remove(0);
			depolarize(world, coords.east(), blocks, frameBlock, portalBlock);
			depolarize(world, coords.up(), blocks, frameBlock, portalBlock);
			depolarize(world, coords.south(), blocks, frameBlock, portalBlock);
			depolarize(world, coords.west(), blocks, frameBlock, portalBlock);
			depolarize(world, coords.down(), blocks, frameBlock, portalBlock);
			depolarize(world, coords.north(), blocks, frameBlock, portalBlock);
			notify.add(coords);
		}
		for (final BlockPos coords2 : notify) {
			if (world.isChunkGeneratedAt(coords2.getX() >> 4, coords2.getZ() >> 4)) {
				final IBlockState blockState = world.getBlockState(coords2);
				world.notifyBlockUpdate(coords2, blockState, blockState, 3);
			}
		}
	}

	private static void onpulse(final World world, final BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		final LinkedList<BlockPos> set = new LinkedList<BlockPos>();
		final Stack<BlockPos> validate = new Stack<BlockPos>();
		addSurrounding(set, pos);
		while (set.size() > 0) {
			final BlockPos coords = set.remove(0);
			expandPortal(world, coords, set, validate, frameBlock, portalBlock);
		}
		while (validate.size() > 0) {
			final BlockPos coords = validate.pop();
			if (!checkPortalTension(world, coords, frameBlock, portalBlock)) {
				world.setBlockState(coords, Blocks.AIR.getDefaultState(), 0);
			}
		}
	}

	private static boolean isPortalBlockStable(final World world, final BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		return world.isRemote || (checkPortalTension(world, pos, frameBlock, portalBlock)
				&& getTileEntity((IBlockAccess) world, pos, frameBlock, portalBlock) != null);
	}

	private static boolean checkPortalTension(final World world, final BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		if (world.isRemote) {
			return true;
		}
		int score = 0;
		if (isValidLinkPortalBlock(world.getBlockState(pos.east()), frameBlock, portalBlock) > 0
				&& isValidLinkPortalBlock(world.getBlockState(pos.west()), frameBlock, portalBlock) > 0) {
			++score;
		}
		if (isValidLinkPortalBlock(world.getBlockState(pos.up()), frameBlock, portalBlock) > 0
				&& isValidLinkPortalBlock(world.getBlockState(pos.down()), frameBlock, portalBlock) > 0) {
			++score;
		}
		if (isValidLinkPortalBlock(world.getBlockState(pos.south()), frameBlock, portalBlock) > 0
				&& isValidLinkPortalBlock(world.getBlockState(pos.north()), frameBlock, portalBlock) > 0) {
			++score;
		}
		return score > 1;
	}

	private static void validatePortal(final World world, final BlockPos pos, final Collection<BlockPos> blocks,
			final Block frameBlock, final Block portalBlock) {
		if (!isPortalBlockStable(world, pos, frameBlock, portalBlock)) {
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
			final Stack<BlockPos> created, final Block frameBlock, final Block portalBlock) {
		if (!world.isAirBlock(pos)) {
			return;
		}
		final int score = isValidLinkPortalBlock(world.getBlockState(pos.east()), frameBlock, portalBlock)
				+ isValidLinkPortalBlock(world.getBlockState(pos.west()), frameBlock, portalBlock)
				+ isValidLinkPortalBlock(world.getBlockState(pos.up()), frameBlock, portalBlock)
				+ isValidLinkPortalBlock(world.getBlockState(pos.down()), frameBlock, portalBlock)
				+ isValidLinkPortalBlock(world.getBlockState(pos.south()), frameBlock, portalBlock)
				+ isValidLinkPortalBlock(world.getBlockState(pos.north()), frameBlock, portalBlock);
		if (score > 1) {
			world.setBlockState(pos, portalBlock.getDefaultState(), 0);
			created.push(pos);
			addSurrounding(set, pos);
		}
	}

	private static void directPortal(final World world, final BlockPos pos, final int meta, final List<BlockPos> blocks,
			final List<BlockPos> portals, final Block frameBlock, final Block portalBlock) {
		final IBlockState blockState = world.getBlockState(pos);
		if (isValidLinkPortalBlock(blockState, frameBlock, portalBlock) == 0) {
			return;
		}
		if (isBlockActive(blockState, frameBlock, portalBlock)) {
			return;
		}
		world.setBlockState(pos, getDirectedState(blockState, meta - 1, frameBlock, portalBlock), 0);
		if (blockState.getBlock() == portalBlock) {
			portals.add(pos);
		} else {
			blocks.add(pos);
		}
	}

	private static void depolarize(final World world, final BlockPos pos, final List<BlockPos> blocks,
			final Block frameBlock, final Block portalBlock) {
		final IBlockState blockstate = world.getBlockState(pos);
		if (isValidLinkPortalBlock(blockstate, frameBlock, portalBlock) == 0) {
			return;
		}
		if (!isBlockActive(blockstate, frameBlock, portalBlock)) {
			return;
		}
		world.setBlockState(pos, blockstate.getBlock().getDefaultState(), 0);
		if (blockstate.getBlock() == portalBlock && !isPortalBlockStable(world, pos, frameBlock, portalBlock)) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
		blocks.add(pos);
	}

	public static TileEntity getTileEntity(final IBlockAccess blockaccess, BlockPos pos, final Block frameBlock,
			final Block portalBlock) {
		final HashSet<BlockPos> visited = new HashSet<BlockPos>();
		for (IBlockState blockstate = blockaccess.getBlockState(pos); blockstate
				.getBlock() != getReceptacleBlock(); blockstate = blockaccess.getBlockState(pos)) {
			if (isValidLinkPortalBlock(blockstate, frameBlock, portalBlock) == 0) {
				return null;
			}
			if (!isBlockActive(blockstate, frameBlock, portalBlock)) {
				return null;
			}
			if (!visited.add(pos)) {
				return null;
			}
			pos = pos.offset(getBlockFacing(blockstate, frameBlock, portalBlock));
		}
		return blockaccess.getTileEntity(pos);
	}
}