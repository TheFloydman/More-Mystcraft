package thefloydman.moremystcraft.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPyramids extends WorldGeneratorAdv {
	private List<IBlockState> blocks;

	public WorldGenPyramids(List<IBlockState> blockList) {
		this.blocks = blockList;
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
		int height = rand.nextInt(7) + 2;
		for (int curY = base.getY(), width = height; curY <= base.getY() + height; curY++, width--) {
			fillSquareLayer(new BlockPos(base.getX(), curY, base.getZ()), width, world, rand);
			cleanupPyramidLayer(new BlockPos(base.getX(), curY, base.getZ()), width, width, world, rand);
		}
		return true;
	}

	private void fillRecLayer(BlockPos center, int widthX, int widthZ, World world, Random rand) {
		for (int curX = center.getX() - widthX; curX <= center.getX() + widthX; curX++) {
			for (int curZ = center.getZ() - widthZ; curZ <= center.getZ() + widthZ; curZ++) {
				this.placeBlock(world, new BlockPos(curX, center.getY(), curZ),
						this.blocks.get(rand.nextInt(this.blocks.size())), 2);
			}
		}
	}

	private void fillSquareLayer(BlockPos center, int width, World world, Random rand) {
		fillRecLayer(center, width, width, world, rand);
	}

	private void cleanupPyramidLayer(BlockPos center, int widthX, int widthZ, World world, Random rand) {
		for (int curX = center.getX() - widthX; curX <= center.getX() + widthX; curX++) {
			for (int curZ = center.getZ() - widthZ; curZ <= center.getZ() + widthZ; curZ++) {
				if (!world.getBlockState(new BlockPos(curX, center.getY() - 1, curZ)).getMaterial().isSolid()) {
					this.placeBlock(world, new BlockPos(curX, center.getY(), curZ), Blocks.AIR.getDefaultState(), 2);
					if ((int) (Math.random() * 2) == 0) {
						int groundY = findGround(world, curX, center.getY(), curZ);
						if (groundY > 0) {
							this.placeBlock(world, new BlockPos(curX, groundY + 1, curZ),
									this.blocks.get(rand.nextInt(this.blocks.size())), 2);
						}

					}
				}
			}
		}
	}

	private int findGround(World world, int x, int y, int z) {
		for (; y > 0; y--) {
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
