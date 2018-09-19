package thefloydman.moremystcraft.symbol.symbols;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IPopulate;
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