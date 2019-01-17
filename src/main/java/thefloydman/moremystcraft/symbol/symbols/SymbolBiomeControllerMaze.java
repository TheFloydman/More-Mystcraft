package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IBiomeController;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;

import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.world.gen.MazeGeneratorRecursiveBacktracker;
import thefloydman.moremystcraft.world.gen.NoiseGeneratorCellularAutomata;

public class SymbolBiomeControllerMaze extends MoreMystcraftSymbolBase {

	public SymbolBiomeControllerMaze(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final Number size = controller.popModifier("size").asNumber();
		final List<Biome> biomes = new ArrayList<Biome>();
		for (Biome biome = ModifierUtils.popBiome(controller); biome != null; biome = ModifierUtils
				.popBiome(controller)) {
			biomes.add(biome);
		}
		final Random rand = new Random(controller.getSeed());
		while (biomes.size() < 2) {
			biomes.add(SymbolBiome.getRandomBiome(rand));
		}
		controller.registerInterface(new BiomeController(biomes, size));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private class BiomeController implements IBiomeController {
		private List<Biome> biomes;
		private double size;
		private int resolution;
		private Biome[] biomeArray;
		Map<String, int[][]> mazeMap = new HashMap<>();

		public BiomeController(final List<Biome> biomes, Number size) {
			this.biomes = biomes;
			if (size != null) {
				if (size.doubleValue() == 3) {
					this.size = 64;
				} else if (size.doubleValue() == 2) {
					this.size = 32;
				} else if (size.doubleValue() == 1) {
					this.size = 16;
				} else if (size.doubleValue() == 0.5) {
					this.size = 8;
				} else if (size.doubleValue() == 0.25) {
					this.size = 4;
				}
			} else {
				this.size = 16;
			}
		}

		@Override
		public List<Biome> getValidSpawnBiomes() {
			return this.biomes;
		}

		@Override
		public Biome getBiomeAtCoords(final int i, final int j) {
			int mazeChunkX = (int) Math.floor(i / (16 * this.size));
			int mazeChunkZ = (int) Math.floor(j / (16 * this.size));
			String index = String.valueOf(mazeChunkX) + "," + String.valueOf(mazeChunkZ);
			if (mazeMap.get(index) == null) {
				generateMazeSquare(index);
			}
			int localX = (int) Math.abs((i - (i % this.size)) / this.size);
			while (localX > 15) {
				localX -= 16;
			}
			int localZ = (int) Math.abs((j - (j % this.size)) / this.size);
			while (localZ > 15) {
				localZ -= 16;
			}
			if (mazeMap.get(index)[localX][localZ] == 2) {
				return this.biomes.get(0);
			} else if (mazeMap.get(index)[localX][localZ] == 1) {
				return this.biomes.get(1);
			} else {
				return this.biomes.get(0);
			}
		}

		@Override
		public Biome[] getBiomesForGeneration(Biome[] aBiome, final int x, final int z, final int xSize,
				final int zSize) {
			if (aBiome == null || aBiome.length < xSize * zSize) {
				aBiome = new Biome[xSize * zSize];
			}
			for (int i = 0; i < xSize * zSize; ++i) {
				aBiome[i] = this.getBiomeAtCoords((x + i % xSize) * 4, (z + i / xSize) * 4);
			}
			return aBiome;
		}

		@Override
		public Biome[] getBiomesAtCoords(Biome[] aBiome, final int x, final int z, final int xSize, final int zSize,
				final boolean flag) {
			if (aBiome == null || aBiome.length < xSize * zSize) {
				aBiome = new Biome[xSize * zSize];
			}
			if (flag && xSize == 16 && zSize == 16 && (x & 0xF) == 0x0 && (z & 0xF) == 0x0) {
				return this.createBiomeArray(aBiome, x, z, xSize, zSize);
			}
			for (int i = 0; i < xSize * zSize; ++i) {
				aBiome[i] = this.getBiomeAtCoords(x + i % xSize, z + i / xSize);
			}
			return aBiome;
		}

		private Biome[] createBiomeArray(Biome[] aBiome, final int i, final int j, final int k, final int l) {
			if (aBiome == null || aBiome.length < k * l) {
				aBiome = new Biome[k * l];
			}
			for (int i2 = 0; i2 < k * l; ++i2) {
				aBiome[i2] = this.getBiomeAtCoords(i + i2 % k, j + i2 / k);
			}
			return aBiome;
		}

		@Override
		public void cleanupCache() {
		}

		private Double distanceToCoords(int x, int z) {
			return Math.abs(Math.sqrt((x * x) + (z * z)));
		}

		private void generateMazeSquare(String index) {
			if (this.biomes.get(0).equals(null)) {
				return;
			}
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
