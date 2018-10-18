package thefloydman.moremystcraft.data;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;

import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.util.CollectionUtils;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;
import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.api.grammar.GrammarData;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import thefloydman.moremystcraft.grammar.ModGrammarData;
import thefloydman.moremystcraft.symbol.SymbolBase;

public class SymbolRules {
	public static void initialize() {
		addRuleInternal("Pyramids",
				buildRule(3, GrammarData.FEATURE_MEDIUM, GrammarData.BLOCK_STRUCTURE, forMoreMystcraft("pyramids")));
		addRuleInternal("biocon_rings", buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST,
				GrammarData.BIOME, GrammarData.BIOME, ModGrammarData.SIZE_SEQ, forMoreMystcraft("biocon_rings")));
		addRuleInternal("biocon_natural", buildRule(1, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST,
				GrammarData.BIOME, GrammarData.BIOME, ModGrammarData.SIZE_BASIC, forMoreMystcraft("biocon_natural")));
		addRuleInternal("size_tiny", buildRule(2, ModGrammarData.SIZE_BASIC, forMoreMystcraft("size_tiny")));
		addRuleInternal("size_small", buildRule(1, ModGrammarData.SIZE_BASIC, forMoreMystcraft("size_small")));
		addRuleInternal("size_medium", buildRule(0, ModGrammarData.SIZE_BASIC, forMoreMystcraft("size_medium")));
		addRuleInternal("size_large", buildRule(3, ModGrammarData.SIZE_BASIC, forMoreMystcraft("size_large")));
		addRuleInternal("size_huge", buildRule(4, ModGrammarData.SIZE_BASIC, forMoreMystcraft("size_huge")));
	}

	private static void addRuleInternal(final String key, final GrammarGenerator.Rule rule) {
		addRule(forMoreMystcraft(key), rule);
	}

	private static ResourceLocation forMoreMystcraft(final String path) {
		return new ResourceLocation("moremystcraft", path);
	}

	private static void addRule(final ResourceLocation string, final GrammarGenerator.Rule rule) {
		final IAgeSymbol symbol = SymbolManager.getAgeSymbol(string);
		if (string == null) {
			LoggerUtils.info("Failed to add rule to symbol " + string, new Object[0]);
			return;
		}
		if (symbol instanceof SymbolBase) {
			((SymbolBase) symbol).addRule(rule);
		}
	}

	private static GrammarGenerator.Rule buildRule(final Integer rank, final ResourceLocation parent,
			final ResourceLocation... args) {
		final ArrayList<ResourceLocation> list = CollectionUtils.buildList(args);
		return new GrammarGenerator.Rule(parent, list, rank);
	}
}