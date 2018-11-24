package thefloydman.moremystcraft.world.gen.structure.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;

import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

public class WorldGenReplacementLibrary extends WorldGeneratorAdv {
	private IBlockState state;

	public WorldGenReplacementLibrary(final Block block) {
		this(block.getDefaultState());
	}

	public WorldGenReplacementLibrary(final IBlockState state) {
		this.state = state;
	}

	@Override
	public boolean doGeneration(final World world, final Random rand, final BlockPos base) {
		Chunk chunk = world.getChunkFromBlockCoords(base);
		int chunkX = chunk.x * 16;
		int chunkZ = chunk.z * 16;
		int pointZeroX = chunkX + 4;
		int pointOneX = chunkX + 11;
		int pointTwoX = chunkX + 4;
		int pointThreeX = chunkX + 11;
		int pointZeroZ = chunkZ + 4;
		int pointOneZ = chunkZ + 11;
		int pointTwoZ = chunkZ + 4;
		int pointThreeZ = chunkZ + 11;
		Block[] blockArray = new Block[4];
		BlockPos[] blockPosArray = new BlockPos[4];
		blockArray[0] = findBlockFromAbove(world, pointZeroX, pointZeroZ);
		blockPosArray[0] = findBlockPosFromAbove(world, pointZeroX, pointZeroZ);
		if (blockArray[0].equals(Blocks.COBBLESTONE) || blockArray[0].equals(Blocks.STONE_STAIRS)) {
			BlockPos startingPos = new BlockPos(findStartingPos(world, blockPosArray[0]));
		}
		blockArray[1] = findBlockFromAbove(world, pointOneX, pointOneZ);
		blockPosArray[1] = findBlockPosFromAbove(world, pointOneX, pointOneZ);
		if (blockArray[1].equals(Blocks.COBBLESTONE) || blockArray[1].equals(Blocks.STONE_STAIRS)) {

		}
		blockArray[2] = findBlockFromAbove(world, pointTwoX, pointTwoZ);
		blockPosArray[2] = findBlockPosFromAbove(world, pointTwoX, pointTwoZ);
		if (blockArray[2].equals(Blocks.COBBLESTONE) || blockArray[2].equals(Blocks.STONE_STAIRS)) {

		}
		blockArray[3] = findBlockFromAbove(world, pointThreeX, pointThreeZ);
		blockPosArray[3] = findBlockPosFromAbove(world, pointThreeX, pointThreeZ);
		if (blockArray[3].equals(Blocks.COBBLESTONE) || blockArray[3].equals(Blocks.STONE_STAIRS)) {

		}
		return true;
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

	private Block findBlockFromAbove(World world, int x, int z) {
		for (int y = 255; y >= 0; y--) {
			if (!world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.AIR)) {
				return world.getBlockState(new BlockPos(x, y, z)).getBlock();
			}
		}
		return null;
	}

	private BlockPos findBlockPosFromAbove(World world, int x, int z) {
		for (int y = 255; y >= 0; y--) {
			if (!world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.AIR)) {
				return new BlockPos(x, y, z);
			}
		}
		return null;
	}

	private BlockPos findStartingPos(World world, BlockPos pos) {
		boolean foundCobble = true;
		while (foundCobble == true) {
			pos = new BlockPos(pos.getX() - 1, pos.getY() + 1, pos.getZ());
			
			if (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock()
					.equals(Blocks.COBBLESTONE)
					|| world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock()
							.equals(Blocks.STONE_STAIRS)) {

			}
		}
		return null;
	}
}