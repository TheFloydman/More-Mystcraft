package thefloydman.moremystcraft.world.gen.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenGiganticTree {

	World world;
	Random rand;
	final IBlockState logBlockState;
	final IBlockState leafBlockState;
	int treeHeightMinimum;
	int treeHeightVariant;
	int treeHeightActual;
	int xAnchor;
	int zAnchor;
	int leavesDistance;
	List<Integer> trunkColumnsX;
	List<Integer> trunkColumnsZ;

	public WorldGenGiganticTree(IBlockState logBlockState, IBlockState leafBlockState) {
		this.logBlockState = logBlockState;
		this.leafBlockState = leafBlockState;
	}

	public boolean generate(final World world, final Random rand, int x, int z) {
		this.world = world;
		this.rand = rand;
		this.treeHeightMinimum = this.world.provider.getAverageGroundLevel() + 128;
		this.treeHeightVariant = 16;
		this.treeHeightActual = treeHeightMinimum + 1 + this.rand.nextInt(this.treeHeightVariant);
		this.xAnchor = x;
		this.zAnchor = z;
		this.leavesDistance = 3;
		this.trunkColumnsX = new ArrayList<Integer>();
		this.trunkColumnsZ = new ArrayList<Integer>();

		this.generateTrunk();
		this.generateAllBranchesAndLeaves();
		this.generateTopLeaves();

		return true;
	}
	
	protected void generateTopLeaves() {
		generateLeavesSphere(new BlockPos(xAnchor, this.treeHeightActual - 1, zAnchor));
		generateLeavesSphere(new BlockPos(xAnchor + 3, this.treeHeightActual - 1, zAnchor));
		generateLeavesSphere(new BlockPos(xAnchor + 3, this.treeHeightActual - 1, zAnchor + 3));
		generateLeavesSphere(new BlockPos(xAnchor, this.treeHeightActual - 1, zAnchor + 3));
	}

	protected void generateAllBranchesAndLeaves() {

		if (trunkColumnsX.size() != trunkColumnsZ.size()) {
			System.out.println("Trunk Column Arrays are not the same size. Aborting branch and leaves generation.");
			return;
		}

		for (int i = 0; i < this.trunkColumnsX.size(); i++) {
			if (trunkColumnsX.get(i) == 1 && trunkColumnsZ.get(i) == 0) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 1, this.treeHeightActual - 8, this.zAnchor - 1),
						BranchDirections.NORTH.ordinal());
			} else if (trunkColumnsX.get(i) == 2 && trunkColumnsZ.get(i) == 0) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 2, this.treeHeightActual - 8, this.zAnchor - 1),
						BranchDirections.NORTH.ordinal());
			} else if (trunkColumnsX.get(i) == 2 && trunkColumnsZ.get(i) == 1) {
				generateColumnBranchesAndLeaves(new BlockPos(this.xAnchor + 3, this.treeHeightActual - 8, this.zAnchor),
						BranchDirections.NORTHEAST.ordinal());
			} else if (trunkColumnsX.get(i) == 3 && trunkColumnsZ.get(i) == 1) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 4, this.treeHeightActual - 8, this.zAnchor + 1),
						BranchDirections.EAST.ordinal());
			} else if (trunkColumnsX.get(i) == 3 && trunkColumnsZ.get(i) == 2) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 4, this.treeHeightActual - 8, this.zAnchor + 2),
						BranchDirections.EAST.ordinal());
			} else if (trunkColumnsX.get(i) == 2 && trunkColumnsZ.get(i) == 2) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 3, this.treeHeightActual - 8, this.zAnchor + 3),
						BranchDirections.SOUTHEAST.ordinal());
			} else if (trunkColumnsX.get(i) == 2 && trunkColumnsZ.get(i) == 3) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 2, this.treeHeightActual - 8, this.zAnchor + 4),
						BranchDirections.SOUTH.ordinal());
			} else if (trunkColumnsX.get(i) == 1 && trunkColumnsZ.get(i) == 3) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor + 1, this.treeHeightActual - 8, this.zAnchor + 4),
						BranchDirections.SOUTH.ordinal());
			} else if (trunkColumnsX.get(i) == 1 && trunkColumnsZ.get(i) == 2) {
				generateColumnBranchesAndLeaves(new BlockPos(this.xAnchor, this.treeHeightActual - 8, this.zAnchor + 3),
						BranchDirections.SOUTHWEST.ordinal());
			} else if (trunkColumnsX.get(i) == 0 && trunkColumnsZ.get(i) == 2) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor - 1, this.treeHeightActual - 8, this.zAnchor + 2),
						BranchDirections.WEST.ordinal());
			} else if (trunkColumnsX.get(i) == 2 && trunkColumnsZ.get(i) == 1) {
				generateColumnBranchesAndLeaves(
						new BlockPos(this.xAnchor - 1, this.treeHeightActual - 8, this.zAnchor + 1),
						BranchDirections.WEST.ordinal());
			} else if (trunkColumnsX.get(i) == 1 && trunkColumnsZ.get(i) == 1) {
				generateColumnBranchesAndLeaves(new BlockPos(this.xAnchor, this.treeHeightActual - 8, this.zAnchor),
						BranchDirections.NORTHWEST.ordinal());
			}
		}

	}

	protected boolean generateColumnBranchesAndLeaves(BlockPos pos, final int direction) {

		for (int i = 0; i < 8; i++) {
			if (this.rand.nextInt(4) != 0 || world.getBlockState(pos.up(i).down()).equals(this.logBlockState)) {
				continue;
			}
			int length = 4 + this.rand.nextInt(3);
			switch (direction) {
			case 0:
				generateBlockLine(pos.up(i), pos.up(i).north(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).north(length - 1));
				break;
			case 1:
				generateBlockLine(pos.up(i), pos.up(i).north(length).east(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).north(length - 1).east(length - 1));
				break;
			case 2:
				generateBlockLine(pos.up(i), pos.up(i).east(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).east(length - 1));
				break;
			case 3:
				generateBlockLine(pos.up(i), pos.up(i).south(length).east(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).south(length - 1).east(length - 1));
				break;
			case 4:
				generateBlockLine(pos.up(i), pos.up(i).south(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).south(length - 1));
				break;
			case 5:
				generateBlockLine(pos.up(i), pos.up(i).south(length).west(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).south(length - 1).west(length - 1));
				break;
			case 6:
				generateBlockLine(pos.up(i), pos.up(i).west(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).west(length - 1));
				break;
			case 7:
				generateBlockLine(pos.up(i), pos.up(i).north(length).west(length), this.logBlockState);
				generateLeavesSphere(pos.up(i).north(length - 1).west(length - 1));
				break;
			}
		}

		return true;
	}

	protected void generateLeavesSphere(final BlockPos pos) {
		for (int x = -this.leavesDistance; x <= this.leavesDistance; x++) {
			for (int y = -this.leavesDistance; y <= this.leavesDistance; y++) {
				for (int z = -this.leavesDistance; z <= this.leavesDistance; z++) {
					if (this.world.getBlockState(pos.east(x).up(y).south(z)).equals(Blocks.AIR.getDefaultState())) {
						if (indirectDistanceBetween(pos, pos.east(x).up(y).south(z)) < 5) {
							if (directDistanceBetween(pos, pos.east(x).up(y).south(z)) <= this.leavesDistance) {
								this.world.setBlockState(pos.east(x).up(y).south(z), this.leafBlockState);
							}
						}
					}
				}
			}
		}
	}

	protected float directDistanceBetween(final BlockPos pos0, final BlockPos pos1) {
		float xDif = pos1.getX() - pos0.getX();
		float yDif = pos1.getY() - pos0.getY();
		float zDif = pos1.getZ() - pos0.getZ();
		float x2 = xDif * xDif;
		float y2 = yDif * yDif;
		float z2 = zDif * zDif;
		return MathHelper.sqrt(x2 + y2 + z2);
	}

	protected void generateTrunk() {
		generateTrunkColumn(0, 1);
		generateTrunkColumn(0, 2);
		generateTrunkColumn(1, 0);
		generateTrunkColumn(1, 1);
		generateTrunkColumn(1, 2);
		generateTrunkColumn(1, 3);
		generateTrunkColumn(2, 0);
		generateTrunkColumn(2, 1);
		generateTrunkColumn(2, 2);
		generateTrunkColumn(2, 3);
		generateTrunkColumn(3, 1);
		generateTrunkColumn(3, 2);
	}

	protected void generateTrunkColumn(final int x, final int z) {
		int bottomY = findGround(x, z) + 1;
		if (bottomY == 0) {
			return;
		}
		generateBlockLine(new BlockPos(x + this.xAnchor, bottomY, z + this.zAnchor),
				new BlockPos(x + this.xAnchor, this.treeHeightActual, z + this.zAnchor), this.logBlockState);
		this.trunkColumnsX.add(x);
		this.trunkColumnsZ.add(z);

	}

	protected boolean generateBlockLine(final BlockPos startPos, final BlockPos endPos, final IBlockState state) {
		if (startPos.equals(endPos)) {
			return false;
		}
		int xDif = MathHelper.abs(endPos.getX() - startPos.getX());
		int yDif = MathHelper.abs(endPos.getY() - startPos.getY());
		int zDif = MathHelper.abs(endPos.getZ() - startPos.getZ());
		int steps = Math.max(Math.max(xDif, yDif), Math.max(zDif, yDif));
		float xStep = (startPos.getX() < endPos.getX()) ? xDif / steps : -xDif / steps;
		float yStep = (startPos.getY() < endPos.getY()) ? yDif / steps : -yDif / steps;
		float zStep = (startPos.getZ() < endPos.getZ()) ? zDif / steps : -zDif / steps;
		float xCurrent = startPos.getX();
		float yCurrent = startPos.getY();
		float zCurrent = startPos.getZ();
		while (!endPos.equals(new BlockPos(Math.round(xCurrent), Math.round(yCurrent), Math.round(zCurrent)))) {
			this.world.setBlockState(new BlockPos(Math.round(xCurrent), Math.round(yCurrent), Math.round(zCurrent)),
					state);
			xCurrent += xStep;
			yCurrent += yStep;
			zCurrent += zStep;
		}
		return true;
	}

	protected int findGround(int xLocal, int zLocal) {
		int x = xLocal + this.xAnchor;
		int z = zLocal + this.zAnchor;
		int y = 255;
		for (; y >= 0; y--) {
			if (this.world.getBlockState(new BlockPos(x, y, z)).getMaterial().isSolid()) {
				if (this.world.getBlockState(new BlockPos(x, y, z)).getMaterial().equals(Material.LEAVES)
						|| this.world.getBlockState(new BlockPos(x, y, z)).getMaterial().equals(Material.WOOD)
						|| this.world.getBlockState(new BlockPos(x, y, z)).getMaterial().equals(Material.ICE)) {
					continue;
				}
				return y;
			}
		}
		return y;
	}
	
	protected int indirectDistanceBetween(BlockPos pos0, BlockPos pos1) {
		return MathHelper.abs(pos1.getX() - pos0.getX()) + MathHelper.abs(pos1.getY() - pos0.getY())
				+ MathHelper.abs(pos1.getZ() - pos0.getZ());
	}

	protected enum BranchDirections {
		NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
	}

}