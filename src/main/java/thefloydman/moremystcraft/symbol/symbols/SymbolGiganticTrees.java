package thefloydman.moremystcraft.symbol.symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IPopulate;

import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.world.gen.feature.WorldGenGiganticTree;

public class SymbolGiganticTrees extends MoreMystcraftSymbolBase {

	public SymbolGiganticTrees(final ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		WorldGenGiganticTree generator = new WorldGenGiganticTree(Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState());
		controller.registerInterface(new Populator(generator));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private class Populator implements IPopulate {
		private WorldGenGiganticTree generator;

		public Populator(final WorldGenGiganticTree generator) {
			this.generator = generator;
		}

		@Override
		public boolean populate(final World world, final Random rand, final int k, final int l, final boolean flag) {
			if (!flag && rand.nextInt(50) == 0) {
				return this.generator.generate(world, rand, k + rand.nextInt(16), l + rand.nextInt(16));
			}
			return false;
		}
	}
}