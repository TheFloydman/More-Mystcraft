package thefloydman.moremystcraft.symbol.symbols;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.structure.StructureOceanMonument;

import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IPopulate;
import com.xcompwiz.mystcraft.api.world.logic.ITerrainAlteration;
import com.xcompwiz.mystcraft.api.world.logic.ITerrainFeatureLocator;
import com.xcompwiz.mystcraft.world.ChunkProviderMyst;

import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;

public class SymbolOceanMonument extends MoreMystcraftSymbolBase {
	public SymbolOceanMonument(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		final StructureOceanMonument generator = new StructureOceanMonument();
		controller.registerInterface(new TerrainAlteration(generator));
		controller.registerInterface(new Populator(generator));
		controller.registerInterface(new FeatureLocator(generator));
	}

	@Override
	public int instabilityModifier(final int count) {
		if (count > 3) {
			return 100;
		}
		return 0;
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private class Populator implements IPopulate {
		private StructureOceanMonument generator;

		public Populator(final StructureOceanMonument gen) {
			this.generator = gen;
		}

		@Override
		public boolean populate(final World worldObj, final Random rand, final int i, final int j, final boolean flag) {
			return this.generator.generateStructure(worldObj, rand, new ChunkPos(i >> 4, j >> 4));
		}
	}

	private class TerrainAlteration implements ITerrainAlteration {
		private StructureOceanMonument generator;

		public TerrainAlteration(final StructureOceanMonument gen) {
			this.generator = gen;
		}

		@Override
		public void alterTerrain(final World worldObj, final int chunkX, final int chunkZ, final ChunkPrimer primer) {
			this.generator.generate(worldObj, chunkX, chunkZ, primer);
		}
	}

	private class FeatureLocator implements ITerrainFeatureLocator {
		private StructureOceanMonument generator;

		public FeatureLocator(final StructureOceanMonument gen) {
			this.generator = gen;
		}

		@Override
		public BlockPos locate(final World world, final String s, final BlockPos pos, final boolean genChunks) {
			if ("Monument".equals(s) && this.generator != null) {
				return this.generator.getNearestStructurePos(world, pos, genChunks);
			}
			return null;
		}

		@Override
		public boolean isInsideFeature(final World world, final String identifier, final BlockPos pos) {
			return "Monument".equals(identifier) && this.generator != null
					&& this.generator.isPositionInStructure(world, pos);
		}
	}
}
