package thefloydman.moremystcraft.symbol.symbols;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.ITerrainAlteration;

import thefloydman.moremystcraft.symbol.SymbolBase;
import thefloydman.moremystcraft.world.gen.structure.feature.SubWorldGeneratorAbandonedStudy;
import thefloydman.moremystcraft.world.gen.structure.feature.WorldGeneratorAbandonedStudy;

public class SymbolAbandonedStudy extends SymbolBase {

	public SymbolAbandonedStudy(ResourceLocation registryName) {
		super(registryName);
	}

	public boolean generatesConfigOption() {
		return true;
	}

	public void registerLogic(AgeDirector controller, long seed) {
		WorldGeneratorAbandonedStudy generator = new WorldGeneratorAbandonedStudy();
		controller.registerInterface(new TerrainAlteration(generator));
	}

	private class TerrainAlteration implements ITerrainAlteration {
		private WorldGeneratorAbandonedStudy generator;

		public TerrainAlteration(WorldGeneratorAbandonedStudy gen) {
			this.generator = gen;
		}

		public void alterTerrain(World world, int chunkX, int chunkZ, ChunkPrimer primer) {
			this.generator.generate(world.getChunkProvider(), world, chunkX, chunkZ, primer);
		}
	}

	public int instabilityModifier(int count) {
		if (count > 1) {
			return 500;
		}
		return 0;
	}

	@Override
	public IAgeSymbol setRegistryName(ResourceLocation name) {
		return null;
	}

}
