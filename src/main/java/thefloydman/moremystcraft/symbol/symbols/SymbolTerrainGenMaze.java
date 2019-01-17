package thefloydman.moremystcraft.symbol.symbols;

import net.minecraft.util.*;
import com.xcompwiz.mystcraft.api.world.*;
import com.xcompwiz.mystcraft.api.world.logic.*;

import java.util.HashMap;
import java.util.Map;

import com.xcompwiz.mystcraft.api.symbol.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.world.chunk.*;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.world.gen.MazeGeneratorRecursiveBacktracker;

public class SymbolTerrainGenMaze extends MoreMystcraftSymbolBase {
	public SymbolTerrainGenMaze(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final TerrainGenerator gen = new TerrainGenerator(controller);
		BlockDescriptor block = ModifierUtils.popBlockMatching(controller, BlockCategory.TERRAIN);
		if (block != null) {
			gen.setFloorBlock(block.blockstate);
		}
		block = ModifierUtils.popBlockMatching(controller, BlockCategory.TERRAIN);
		if (block != null) {
			gen.setWallBlock(block.blockstate);
		}
		controller.registerInterface(gen);
		controller.setCloudHeight(160);
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private static class TerrainGenerator implements ITerrainGenerator {
		private AgeDirector controller;
		private IBlockState wallBlock;
		private IBlockState floorBlock;
		private Map<String, int[][]> mazeMap = new HashMap<>();
		private double size;

		public TerrainGenerator(final AgeDirector controller) {
			this.wallBlock = Blocks.STONE.getDefaultState();
			this.floorBlock = Blocks.STONE.getDefaultState();
			this.controller = controller;
			this.size = 1;
		}

		public void setWallBlock(final IBlockState state) {
			this.wallBlock = state;
		}

		public void setFloorBlock(final IBlockState state) {
			this.floorBlock = state;
		}

		@Override
		public void generateTerrain(final int chunkX, final int chunkZ, final ChunkPrimer primer) {
			final int wallHeight = 96;// this.controller.getAverageGroundLevel();
			final int floorHeight = 64;// this.controller.getSeaLevel();

			for (int y = 0; y < 256; y++) {

				IBlockState blockID = Blocks.AIR.getDefaultState();

				String index = String.valueOf(Math.floor(chunkX / 16)) + "," + String.valueOf(Math.floor(chunkZ / 16));
				if (mazeMap.get(index) == null) {
					generateMazeSquare(index);
				}
				
				
				
				int localX = (int) Math.abs((chunkX - (chunkX % this.size)) / this.size);
				while (localX > 15) {
					localX -= 16;
				}
				int localZ = (int) Math.abs((chunkZ - (chunkZ % this.size)) / this.size);
				while (localZ > 15) {
					localZ -= 16;
				}

				for (int z = 0; z < 16; z++) {
					for (int x = 0; x < 16; x++) {
						if (y < 3) {
							blockID = Blocks.BEDROCK.getDefaultState();
						} else if (mazeMap.get(index)[localX][localZ] == 2 && y < floorHeight) {
							blockID = this.floorBlock;
						} else if (mazeMap.get(index)[localX][localZ] == 1 && y < wallHeight) {
							blockID = this.wallBlock;
						}
						primer.setBlockState(x, y, z, blockID);
					}
				}

			}
		}

		private void generateMazeSquare(String index) {
			int[][] mazeSquare = new MazeGeneratorRecursiveBacktracker(16, 16).getMaze();
			int randomTop;
			int randomSide;
			randomTop = (int) ((Math.random() * (16 - 1)) + 1);
			randomTop += randomTop % 2 == 0 ? 1 : 0;
			randomSide = (int) ((Math.random() * (16 - 1)) + 1);
			randomSide += randomSide % 2 == 0 ? 1 : 0;
			mazeSquare[randomTop][0] = 2;
			mazeSquare[0][randomSide] = 2;
			this.mazeMap.put(index, mazeSquare);
		}
	}
}
