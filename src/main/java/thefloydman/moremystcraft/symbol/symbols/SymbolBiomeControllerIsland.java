package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.List;
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
import thefloydman.moremystcraft.world.gen.NoiseGeneratorCellularAutomata;

public class SymbolBiomeControllerIsland extends MoreMystcraftSymbolBase {

	public SymbolBiomeControllerIsland(final ResourceLocation identifier) {
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
		while (biomes.size() < 1) {
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
		private double minSize;
		private double maxSize;
		private double beachSize;
		private double oceanSize;
		// private List<Integer> unverifiedCells;
		int[][] noiseArray;

		public BiomeController(final List<Biome> biomes, Number size) {
			this.biomes = biomes;
			if (size != null) {
				if (size.doubleValue() == 3) {
					this.size = 128;
				} else if (size.doubleValue() == 2) {
					this.size = 96;
				} else if (size.doubleValue() == 1) {
					this.size = 80;
				} else if (size.doubleValue() == 0.5) {
					this.size = 48;
				} else if (size.doubleValue() == 0.25) {
					this.size = 32;
				}
				// this.size = size.doubleValue() * 64 * 3;
			} else {
				this.size = 64;
			}
			minSize = this.size / 8;
			maxSize = this.size;
			beachSize = this.size + 16;
			oceanSize = this.size + 64;
			this.resolution = (int) (this.maxSize * 2) + 1;
			this.biomeArray = new Biome[this.resolution * this.resolution];
			generateIslandArray();
		}

		@Override
		public List<Biome> getValidSpawnBiomes() {
			return this.biomes;
		}

		@Override
		public Biome getBiomeAtCoords(final int i, final int j) {
			if (Math.abs(i) > this.oceanSize || Math.abs(j) > this.oceanSize) {
				return Biomes.OCEAN;
			}
			if (Math.abs(i) > this.size || Math.abs(j) > this.size) {
				return Biomes.OCEAN;
			}
			if (noiseArray[(int) (j + this.size)][(int) (i + this.size)] == 1) {
				return this.biomes.get(0);
			} else if (noiseArray[(int) (j + this.size)][(int) (i + this.size)] == 2) {
				return this.biomes.get(1);
			} else {
				return Biomes.OCEAN;
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

		private void generateIslandArray() {
			if (this.biomes.get(0).equals(null)) {
				return;
			}

			noiseArray = new NoiseGeneratorCellularAutomata(resolution, resolution, 53, 3, 5, 3).getnoiseArray();
			double[] ellipseCenter = { noiseArray.length / 2, noiseArray[0].length / 2 };
			double[] ellipseRadii = { noiseArray.length / (4 - (2 * Math.random())),
					noiseArray[0].length / (4 - (2 * Math.random())) };
			noiseArray = shaveToEllipse(noiseArray, ellipseCenter, ellipseRadii);
			// noiseArray = addBeaches(noiseArray, ellipseCenter, ellipseRadii);
		}

		private int[][] shaveToEllipse(int[][] input, double[] ellipseCenter, double[] ellipseRadii) {
			for (int i = 0; i < input.length; i++) {
				for (int j = 0; j < input[0].length; j++) {
					if (Math.abs(j - ellipseCenter[1]) > ellipseRadii[1]) {
						input[i][j] = 0;
					} else if (Math.abs(i - ellipseCenter[0]) > ellipseRadii[0]) {
						input[i][j] = 0;
					} else if ((((j - ellipseCenter[1]) * (j - ellipseCenter[1]))
							/ ((ellipseRadii[1]) * (ellipseRadii[1])))
							+ (((i - ellipseCenter[0]) * (i - ellipseCenter[0]))
									/ ((ellipseRadii[0]) * (ellipseRadii[0]))) > 1) {
						input[i][j] = 0;
					}
				}
			}
			return input;
		}

		private int[][] addBeaches(int[][] beachedArray, double[] ellipseCenter, double[] ellipseRadii) {
			int chancePercent = 15;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < beachedArray.length; j++) {
					for (int k = 0; k < beachedArray[j].length; k++) {
						if (beachedArray[j][k] == 1 || beachedArray[j][k] == 2) {
							if (j - 1 >= 0 && k - 1 >= 0) {
								if (this.noiseArray[j - 1][k - 1] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j - 1][k - 1] = 2;
									}
								}
							}
							if (j - 1 >= 0) {
								if (this.noiseArray[j - 1][k] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j - 1][k] = 2;
									}
								}
							}
							if (j - 1 >= 0 && k + 1 < noiseArray[0].length) {
								if (this.noiseArray[j - 1][k + 1] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j - 1][k + 1] = 2;
									}
								}
							}
							if (k - 1 >= 0) {
								if (this.noiseArray[j][k - 1] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j][k - 1] = 2;
									}
								}
							}
							if (k + 1 < noiseArray[0].length) {
								if (this.noiseArray[j][k + 1] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j][k + 1] = 2;
									}
								}
							}
							if (j + 1 < noiseArray.length && k - 1 >= 0) {
								if (this.noiseArray[j + 1][k - 1] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j + 1][k - 1] = 2;
									}
								}
							}
							if (j + 1 < noiseArray.length) {
								if (this.noiseArray[j + 1][k] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j + 1][k] = 2;
									}
								}
							}
							if (j + 1 < noiseArray.length && k + 1 < noiseArray[0].length) {
								if (this.noiseArray[j + 1][k + 1] == 0) {
									if ((int) (Math.random() * 100) < chancePercent) {
										this.noiseArray[j + 1][k + 1] = 2;
									}
								}
							}
						}
					}
				}
			}

			return beachedArray;
		}

		private int coordsToIndex(int x, int z) {
			int xNew = x + (int) this.oceanSize;
			int zNew = z + (int) this.oceanSize;
			int cellNumber = (zNew * resolution) + xNew;
			return cellNumber;
		}

		private int indexToXCoord(int index) {
			int xCoord = (int) ((index - ((Math.floor(index / this.resolution)) * this.resolution)) - this.oceanSize);
			return xCoord;
		}

		private int indexToZCoord(int index) {
			int zCoord = (int) (Math.floor(index / this.resolution) - this.oceanSize);
			return zCoord;
		}
	}
}
