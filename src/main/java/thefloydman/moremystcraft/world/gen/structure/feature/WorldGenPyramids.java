package thefloydman.moremystcraft.world.gen.structure.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.config.ModConfig;

import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

public class WorldGenPyramids extends WorldGeneratorAdv {
	private IBlockState state;

	public WorldGenPyramids(final Block block) {
		this(block.getDefaultState());
	}

	public WorldGenPyramids(final IBlockState state) {
		this.state = state;
	}

	@Override
	public boolean doGeneration(final World world, final Random rand, final BlockPos base) {
		if (base.getY() == 0
				|| world.getBlockState(new BlockPos(base.getX(), base.getY() - 1, base.getZ())).getMaterial()
						.equals(Material.WOOD)
				|| world.getBlockState(new BlockPos(base.getX(), base.getY() - 1, base.getZ())).getMaterial()
						.equals(Material.LEAVES)
				|| world.getBlockState(new BlockPos(base.getX(), base.getY() - 1, base.getZ())).getMaterial()
						.isLiquid()) {
			return false;
		}
		int height = ((int) (Math.random() * 7)) + 2;
		for (int curY = base.getY() + height, width = 0; curY >= base.getY(); curY--, width++) {
			fillSquareLayer(new BlockPos(base.getX(), curY, base.getZ()), width, world);
		}
		for (int curY = base.getY(), width = height; curY <= base.getY() + height; curY++, width--) {
			cleanupPyramidLayer(new BlockPos(base.getX(), curY, base.getZ()), width, width, world);
		}
		return true;
	}

	private void fillRecLayer(BlockPos center, int widthX, int widthZ, World world) {
		for (int curX = center.getX() - widthX; curX <= center.getX() + widthX; curX++) {
			for (int curZ = center.getZ() - widthZ; curZ <= center.getZ() + widthZ; curZ++) {
				//if (!world.getBlockState(new BlockPos(curX, center.getY(), curZ)).getMaterial().isSolid()) {
					this.placeBlock(world, new BlockPos(curX, center.getY(), curZ), this.state, 2);
				//}
			}
		}
	}

	private void fillSquareLayer(BlockPos center, int width, World world) {
		fillRecLayer(center, width, width, world);
	}

	private void cleanupPyramidLayer(BlockPos center, int widthX, int widthZ, World world) {
		for (int curX = center.getX() - widthX; curX <= center.getX() + widthX; curX++) {
			for (int curZ = center.getZ() - widthZ; curZ <= center.getZ() + widthZ; curZ++) {
				if (!world.getBlockState(new BlockPos(curX, center.getY() - 1, curZ)).getMaterial().isSolid()) {
					this.placeBlock(world, new BlockPos(curX, center.getY(), curZ), Blocks.AIR.getDefaultState(), 2);
					if ((int) (Math.random() * 2) == 0) {
						int groundY = findGround(world, curX, center.getY(), curZ);
						if (groundY > 0) {
							this.placeBlock(world, new BlockPos(curX, groundY + 1, curZ), this.state, 2);
						}

					}
				}
			}
		}
	}

	private int findGround(World world, int x, int y, int z) {
		int oldY = y;
		for (; y >= 0; y--) {
			if (world.getBlockState(new BlockPos(x, y, z)).getMaterial().isSolid()) {
				if (world.getBlockState(new BlockPos(x, y, z)).getMaterial().equals(Material.LEAVES)
						|| world.getBlockState(new BlockPos(x, y, z)).getMaterial().equals(Material.WOOD)) {
					return 0;
				}
				return y;
			}
		}
		return y;
	}
}
