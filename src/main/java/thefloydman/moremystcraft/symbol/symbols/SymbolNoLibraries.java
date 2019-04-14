package thefloydman.moremystcraft.symbol.symbols;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IPopulate;
import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.world.gen.feature.WorldGenLibraryReplacement;
import thefloydman.moremystcraft.world.gen.feature.WorldGenPyramids;

public class SymbolNoLibraries extends MoreMystcraftSymbolBase {
	public SymbolNoLibraries(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}
	
	public int instabilityModifier(int count) {
		return -1000;
	}

}
