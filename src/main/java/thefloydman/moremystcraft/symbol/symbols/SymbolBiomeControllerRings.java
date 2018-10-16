package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IBiomeController;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;

import thefloydman.moremystcraft.symbol.SymbolBase;

public class SymbolBiomeControllerRings extends SymbolBase {
	
	List<Integer> thicknesses = new ArrayList<Integer>();
	final int MINIMUM_THICKNESS = 8;
	final int ADDITIONAL_THICKNESS = 248;
	
	public SymbolBiomeControllerRings(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final List<Biome> biomes = new ArrayList<Biome>();
		for (Biome biome = ModifierUtils.popBiome(controller); biome != null; biome = ModifierUtils
				.popBiome(controller)) {
			biomes.add(biome);
			thicknesses.add((int) (Math.random() * ADDITIONAL_THICKNESS) + MINIMUM_THICKNESS);
		}
		final Random rand = new Random(controller.getSeed());
		while (biomes.size() < 2) {
			biomes.add(SymbolBiome.getRandomBiome(rand));
			thicknesses.add((int) (Math.random() * ADDITIONAL_THICKNESS) + MINIMUM_THICKNESS);
		}
		controller.registerInterface(new BiomeController(biomes));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private class BiomeController implements IBiomeController {
		private List<Biome> biomes;

		public BiomeController(final List<Biome> biomes) {
			this.biomes = biomes;
		}

		@Override
		public List<Biome> getValidSpawnBiomes() {
			return this.biomes;
		}

		@Override
		public Biome getBiomeAtCoords(final int i, final int j) {
			Double distance = distanceToCoords(i, j);
			int ringNumber = 0;
			for (int a = 0; distance >= 0; a++, ringNumber++) {
				if (a >= thicknesses.size()) {
					a -= thicknesses.size();
				}
				distance -= thicknesses.get(a);
			}
			ringNumber -= 1;
			while (ringNumber > this.biomes.size() - 1) {
				ringNumber -= this.biomes.size();
			}
			return this.biomes.get(ringNumber);
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
	}
}
