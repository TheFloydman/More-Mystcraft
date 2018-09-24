package thefloydman.moremystcraft.data;

import java.util.*;

import net.minecraft.util.*;

import com.xcompwiz.mystcraft.grammar.*;
import com.xcompwiz.mystcraft.logging.*;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.api.symbol.*;
import com.xcompwiz.util.*;
import com.xcompwiz.mystcraft.api.grammar.*;

import thefloydman.moremystcraft.symbol.SymbolBase;

public class SymbolRules {
	public static void initialize() {
		addRuleInternal("Pyramids", buildRule(3, GrammarData.FEATURE_MEDIUM, GrammarData.BLOCK_STRUCTURE,
				forMoreMystcraft("pyramids")));
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