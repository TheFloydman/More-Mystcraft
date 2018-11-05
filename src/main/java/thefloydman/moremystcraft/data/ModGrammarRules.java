package thefloydman.moremystcraft.data;

import net.minecraft.util.*;
import thefloydman.moremystcraft.grammar.ModGrammarData;

import com.xcompwiz.mystcraft.api.grammar.*;
import com.xcompwiz.mystcraft.grammar.*;
import com.xcompwiz.util.*;
import java.util.*;

public class ModGrammarRules {

	private static final ResourceLocation SIZE_GEN = forMoreMystcraft("size_adv");
	public static final ResourceLocation SIZE_EXT = forMoreMystcraft("size_ext");
	public static final ResourceLocation SIZE_SUN = forMoreMystcraft("size_sun");
	public static final ResourceLocation SIZE_ISLAND_BIOCON = forMoreMystcraft("size_island_biocon");

	public static void initialize() {
		registerRule(buildRule(1, ModGrammarData.SIZE_SEQ, ModGrammarRules.SIZE_GEN));
		registerRule(buildRule(2, ModGrammarRules.SIZE_GEN, ModGrammarRules.SIZE_GEN, ModGrammarData.SIZE_BASIC));
		registerRule(buildRule(0, ModGrammarRules.SIZE_GEN, ModGrammarData.SIZE_BASIC));
		registerRule(buildRule(1, ModGrammarData.SIZE_BASIC, ModGrammarRules.SIZE_GEN));
		registerRule(buildRule(0, ModGrammarRules.SIZE_SUN, ModGrammarData.SIZE_COMMON));
		registerRule(buildRule(2, ModGrammarRules.SIZE_SUN, ModGrammarData.SIZE_UNCOMMON));
		registerRule(buildRule(4, ModGrammarRules.SIZE_SUN, ModGrammarData.SIZE_RARE));
		registerRule(buildRule(0, ModGrammarRules.SIZE_ISLAND_BIOCON, ModGrammarData.SIZE_COMMON));
		registerRule(buildRule(1, ModGrammarRules.SIZE_ISLAND_BIOCON, ModGrammarData.SIZE_UNCOMMON));
		registerRule(buildRule(2, ModGrammarRules.SIZE_ISLAND_BIOCON, ModGrammarData.SIZE_RARE));
	}

	private static void registerRule(final GrammarGenerator.Rule rule) {
		GrammarGenerator.registerRule(rule);
	}

	private static ResourceLocation forMoreMystcraft(final String path) {
		return new ResourceLocation("moremystcraft", path);
	}

	private static GrammarGenerator.Rule buildRule(final Integer rank, final ResourceLocation parent,
			final ResourceLocation... args) {
		final ArrayList<ResourceLocation> list = CollectionUtils.buildList(args);
		return new GrammarGenerator.Rule(parent, list, rank);
	}
}
