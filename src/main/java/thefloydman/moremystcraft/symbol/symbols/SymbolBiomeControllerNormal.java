package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.IntCache;

import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IBiomeController;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;
import com.xcompwiz.mystcraft.world.gen.layer.GenLayerBiomeMyst;
import com.xcompwiz.mystcraft.world.gen.layer.GenLayerZoomMyst;

import thefloydman.moremystcraft.symbol.SymbolBase;

public class SymbolBiomeControllerNormal extends SymbolBase {

	public SymbolBiomeControllerNormal(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final Number size = controller.popModifier("size").asNumber();
		int zoom;
		final List<Biome> biomes = new ArrayList<Biome>();
		for (Biome biome = ModifierUtils.popBiome(controller); biome != null; biome = ModifierUtils
				.popBiome(controller)) {
			biomes.add(biome);
		}
		if (size != null) {
			if (size.doubleValue() <= 0.375) {
				controller.registerInterface(new BiomeController(controller, 0, biomes));
			} else if (size.doubleValue() > 0.375 && size.doubleValue() <= 0.75) {
				controller.registerInterface(new BiomeController(controller, 1, biomes));
			} else if (size.doubleValue() > 0.75 && size.doubleValue() <= 1.5) {
				controller.registerInterface(new BiomeController(controller, 2, biomes));
			} else if (size.doubleValue() > 1.5 && size.doubleValue() <= 2.5) {
				controller.registerInterface(new BiomeController(controller, 3, biomes));
			} else if (size.doubleValue() > 2.5) {
				controller.registerInterface(new BiomeController(controller, 4, biomes));
			}
		} else {
			controller.registerInterface(new BiomeController(controller, 2, biomes));
		}
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	/*
	 * This subclass is copied directly from Mystcraft. Do not copy it without
	 * explicit permission from XCompWiz. All Rights Reserved unless otherwise
	 * explicitly stated.
	 */
	static class BiomeController implements IBiomeController {
		private Biome[] allowedBiomes;
		private List<Biome> biomesToSpawnIn;
		private GenLayer genBiomes;
		private GenLayer biomeIndexLayer;
		private BiomeCache biomeCache;
		private int zoomscale;

		protected BiomeController(final AgeDirector controller, final int zoom, final List<Biome> biomes) {
			this.zoomscale = zoom;
			this.biomeCache = new BiomeCache(controller.getBiomeProvider());
			(this.biomesToSpawnIn = new ArrayList<Biome>()).add(Biomes.FOREST);
			this.biomesToSpawnIn.add(Biomes.PLAINS);
			this.biomesToSpawnIn.add(Biomes.TAIGA);
			this.biomesToSpawnIn.add(Biomes.TAIGA_HILLS);
			this.biomesToSpawnIn.add(Biomes.FOREST_HILLS);
			this.biomesToSpawnIn.add(Biomes.JUNGLE);
			this.biomesToSpawnIn.add(Biomes.JUNGLE_HILLS);
			final Random rand = new Random(controller.getSeed());
			while (biomes.size() < 3) {
				biomes.add(SymbolBiome.getRandomBiome(rand));
			}
			this.allowedBiomes = new Biome[biomes.size()];
			this.allowedBiomes = biomes.toArray(this.allowedBiomes);
			final GenLayer[] agenlayer = this.computeGenLayers(controller.getSeed(), WorldType.DEFAULT);
			this.genBiomes = agenlayer[0];
			this.biomeIndexLayer = agenlayer[1];
		}

		@Override
		public List<Biome> getValidSpawnBiomes() {
			return this.biomesToSpawnIn;
		}

		@Override
		public Biome getBiomeAtCoords(final int par1, final int par2) {
			return this.biomeCache.getBiome(par1, par2, (Biome) null);
		}

		@Override
		public Biome[] getBiomesForGeneration(Biome[] par1ArrayOfBiome, final int par2, final int par3, final int par4,
				final int par5) {
			IntCache.resetIntCache();
			if (par1ArrayOfBiome == null || par1ArrayOfBiome.length < par4 * par5) {
				par1ArrayOfBiome = new Biome[par4 * par5];
			}
			final int[] ai = this.genBiomes.getInts(par2, par3, par4, par5);
			for (int i = 0; i < par4 * par5; ++i) {
				par1ArrayOfBiome[i] = Biome.getBiome(ai[i]);
			}
			return par1ArrayOfBiome;
		}

		@Override
		public Biome[] getBiomesAtCoords(Biome[] par1ArrayOfBiome, final int par2, final int par3, final int par4,
				final int par5, final boolean par6) {
			IntCache.resetIntCache();
			if (par1ArrayOfBiome == null || par1ArrayOfBiome.length < par4 * par5) {
				par1ArrayOfBiome = new Biome[par4 * par5];
			}
			if (par6 && par4 == 16 && par5 == 16 && (par2 & 0xF) == 0x0 && (par3 & 0xF) == 0x0) {
				final Biome[] aBiome = this.biomeCache.getCachedBiomes(par2, par3);
				System.arraycopy(aBiome, 0, par1ArrayOfBiome, 0, par4 * par5);
				return par1ArrayOfBiome;
			}
			final int[] ai = this.biomeIndexLayer.getInts(par2, par3, par4, par5);
			for (int i = 0; i < par4 * par5; ++i) {
				par1ArrayOfBiome[i] = Biome.getBiome(ai[i]);
			}
			return par1ArrayOfBiome;
		}

		@Override
		public void cleanupCache() {
			this.biomeCache.cleanupCache();
		}

		private GenLayer[] computeGenLayers(final long par0, final WorldType par2WorldType) {
			GenLayer obj = (GenLayer) new GenLayerIsland(1L);
			GenLayer obj2;
			obj = (obj2 = (GenLayer) new GenLayerFuzzyZoom(2000L, obj));
			obj2 = GenLayerZoomMyst.func_35515_a(1000L, obj2, 0);
			obj2 = GenLayerZoomMyst.func_35515_a(1000L, obj2, this.zoomscale + 1);
			obj2 = (GenLayer) new GenLayerSmooth(1000L, obj2);
			GenLayer obj3 = obj;
			obj3 = GenLayerZoomMyst.func_35515_a(1000L, obj3, 0);
			obj3 = new GenLayerBiomeMyst(200L, obj3, par2WorldType, this.allowedBiomes);
			obj3 = GenLayerZoomMyst.func_35515_a(1000L, obj3, 2);
			for (int i = 0; i < this.zoomscale; ++i) {
				obj3 = new GenLayerZoomMyst(1000 + i, obj3);
			}
			obj3 = (GenLayer) new GenLayerSmooth(1000L, obj3);
			final GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, obj3);
			obj3.initWorldGenSeed(par0);
			genlayervoronoizoom.initWorldGenSeed(par0);
			return new GenLayer[] { obj3, genlayervoronoizoom };
		}
	}
}
