package thefloydman.moremystcraft.data;

import java.util.ArrayList;

import com.xcompwiz.mystcraft.api.grammar.GrammarData;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;
import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.util.CollectionUtils;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.grammar.MoreMystcraftGrammarData;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftSymbolRules {
	public static void initialize() {
		addRuleInternal("abandoned_study",
				buildRule(null, GrammarData.FEATURE_MEDIUM, Reference.forMoreMystcraft("abandoned_study")));
		addRuleInternal("pyramids", buildRule(3, GrammarData.FEATURE_MEDIUM, MoreMystcraftGrammarRules.MULTIPLE_BLOCKS,
				Reference.forMoreMystcraft("pyramids")));
		addRuleInternal("biocon_rings", buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST,
				GrammarData.BIOME, MoreMystcraftGrammarRules.SIZE_BIOCON, Reference.forMoreMystcraft("biocon_rings")));
		addRuleInternal("biocon_boxes", buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST,
				GrammarData.BIOME, MoreMystcraftGrammarRules.SIZE_BIOCON, Reference.forMoreMystcraft("biocon_boxes")));
		addRuleInternal("biocon_bands_hor",
				buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST, GrammarData.BIOME,
						MoreMystcraftGrammarRules.SIZE_BIOCON, Reference.forMoreMystcraft("biocon_bands_hor")));
		addRuleInternal("biocon_bands_ver",
				buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST, GrammarData.BIOME,
						MoreMystcraftGrammarRules.SIZE_BIOCON, Reference.forMoreMystcraft("biocon_bands_ver")));
		addRuleInternal("biocon_normal",
				buildRule(1, GrammarData.BIOMECONTROLLER, GrammarData.BIOME_LIST, GrammarData.BIOME, GrammarData.BIOME,
						MoreMystcraftGrammarData.SIZE_BASIC, Reference.forMoreMystcraft("biocon_normal")));
		addRuleInternal("biocon_island", buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME,
				MoreMystcraftGrammarRules.SIZE_ISLAND_BIOCON, Reference.forMoreMystcraft("biocon_island")));
		addRuleInternal("biocon_maze", buildRule(2, GrammarData.BIOMECONTROLLER, GrammarData.BIOME, GrammarData.BIOME,
				MoreMystcraftGrammarData.SIZE_BASIC, Reference.forMoreMystcraft("biocon_maze")));
		addRuleInternal("sun_tinted",
				buildRule(1, GrammarData.SUN, GrammarData.SUNSET, MoreMystcraftGrammarData.SUN_COLOR,
						MoreMystcraftGrammarRules.SIZE_SUN, GrammarData.PERIOD_SEQ, GrammarData.ANGLE_SEQ,
						GrammarData.PHASE_SEQ, MoreMystcraftGrammarRules.TILT_SUN_SEQ,
						Reference.forMoreMystcraft("sun_tinted")));
		addRuleInternal("size_tiny",
				buildRule(0, MoreMystcraftGrammarData.SIZE_RARE, Reference.forMoreMystcraft("size_tiny")));
		addRuleInternal("size_small",
				buildRule(0, MoreMystcraftGrammarData.SIZE_UNCOMMON, Reference.forMoreMystcraft("size_small")));
		addRuleInternal("size_medium",
				buildRule(0, MoreMystcraftGrammarData.SIZE_COMMON, Reference.forMoreMystcraft("size_medium")));
		addRuleInternal("size_large",
				buildRule(0, MoreMystcraftGrammarData.SIZE_UNCOMMON, Reference.forMoreMystcraft("size_large")));
		addRuleInternal("size_huge",
				buildRule(0, MoreMystcraftGrammarData.SIZE_RARE, Reference.forMoreMystcraft("size_huge")));
		addRuleInternal("tilt_zero",
				buildRule(0, MoreMystcraftGrammarData.TILT_COMMON, Reference.forMoreMystcraft("tilt_zero")));
		addRuleInternal("tilt_half",
				buildRule(0, MoreMystcraftGrammarData.TILT_UNCOMMON, Reference.forMoreMystcraft("tilt_half")));
		addRuleInternal("tilt_full",
				buildRule(0, MoreMystcraftGrammarData.TILT_RARE, Reference.forMoreMystcraft("tilt_full")));
		addRuleInternal("ocean_monument",
				buildRule(2, GrammarData.FEATURE_MEDIUM, Reference.forMoreMystcraft("ocean_monument")));
		addRuleInternal("terrain_maze", buildRule(4, GrammarData.TERRAIN, GrammarData.BLOCK_TERRAIN,
				GrammarData.BLOCK_TERRAIN, Reference.forMoreMystcraft("terrain_maze")));
		addRuleInternal("gigantic_trees",
				buildRule(2, GrammarData.FEATURE_LARGE, Reference.forMoreMystcraft("gigantic_trees")));
		addRuleInternal("lacking_ores",
				buildRule(3, GrammarData.FEATURE_LARGE, Reference.forMoreMystcraft("lacking_ores")));
		addRuleInternal("sun_color", buildRule(0, MoreMystcraftGrammarData.SUN_COLOR, MoreMystcraftGrammarRules.SUN_COLOR_BASIC, GrammarData.GRADIENT_SEQ, Reference.forMoreMystcraft("sun_color")));
	}

	private static void addRuleInternal(final String key, final GrammarGenerator.Rule rule) {
		addRule(Reference.forMoreMystcraft(key), rule);
	}

	private static void addRule(final ResourceLocation string, final GrammarGenerator.Rule rule) {
		final IAgeSymbol symbol = SymbolManager.getAgeSymbol(string);
		if (string == null) {
			LoggerUtils.info("Failed to add rule to symbol " + string, new Object[0]);
			return;
		}
		if (symbol instanceof MoreMystcraftSymbolBase) {
			((MoreMystcraftSymbolBase) symbol).addRule(rule);
		}
	}

	private static GrammarGenerator.Rule buildRule(final Integer rank, final ResourceLocation parent,
			final ResourceLocation... args) {
		final ArrayList<ResourceLocation> list = CollectionUtils.buildList(args);
		return new GrammarGenerator.Rule(parent, list, rank);
	}
}