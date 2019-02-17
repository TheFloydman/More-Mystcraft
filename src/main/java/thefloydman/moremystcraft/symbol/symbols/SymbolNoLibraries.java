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
		WorldGenLibraryReplacement generator = new WorldGenLibraryReplacement(1);
		controller.registerInterface(new Populator(generator));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private static class Populator implements IPopulate {
		private WorldGenLibraryReplacement generator;

		public Populator(final WorldGenLibraryReplacement generator) {
			this.generator = generator;
		}

		@Override
		public boolean populate(final World world, final Random rand, final int k, final int l, final boolean flag) {
			this.generator.generate(rand, new ChunkPos(new BlockPos(k, 0, l)).x, new ChunkPos(new BlockPos(k, 0, l)).z,
					world, null, null);
			return true;
		}
	}
}
