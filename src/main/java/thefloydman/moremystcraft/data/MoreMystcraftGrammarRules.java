package thefloydman.moremystcraft.data;

import net.minecraft.util.*;
import thefloydman.moremystcraft.grammar.MoreMystcraftGrammarData;
import thefloydman.moremystcraft.util.Reference;

import com.xcompwiz.mystcraft.api.grammar.*;
import com.xcompwiz.mystcraft.grammar.*;
import com.xcompwiz.util.*;
import java.util.*;

public class MoreMystcraftGrammarRules {

	private static final ResourceLocation SIZE_GEN = Reference.forMoreMystcraft("size_adv");
	public static final ResourceLocation SIZE_EXT = Reference.forMoreMystcraft("size_ext");
	public static final ResourceLocation SIZE_SUN = Reference.forMoreMystcraft("size_sun");
	public static final ResourceLocation SIZE_ISLAND_BIOCON = Reference.forMoreMystcraft("size_island_biocon");
	public static final ResourceLocation TILT_SUN_SEQ = Reference.forMoreMystcraft("tilt_sun");

	public static void initialize() {
		registerRule(buildRule(1, MoreMystcraftGrammarData.SIZE_SEQ, MoreMystcraftGrammarRules.SIZE_GEN));
		registerRule(buildRule(2, MoreMystcraftGrammarRules.SIZE_GEN, MoreMystcraftGrammarRules.SIZE_GEN, MoreMystcraftGrammarData.SIZE_BASIC));
		registerRule(buildRule(0, MoreMystcraftGrammarRules.SIZE_GEN, MoreMystcraftGrammarData.SIZE_BASIC));
		registerRule(buildRule(0, MoreMystcraftGrammarData.SIZE_BASIC, MoreMystcraftGrammarData.SIZE_COMMON));
		registerRule(buildRule(1, MoreMystcraftGrammarData.SIZE_BASIC, MoreMystcraftGrammarData.SIZE_UNCOMMON));
		registerRule(buildRule(2, MoreMystcraftGrammarData.SIZE_BASIC, MoreMystcraftGrammarData.SIZE_RARE));
		registerRule(buildRule(null, MoreMystcraftGrammarRules.SIZE_SUN, MoreMystcraftGrammarData.SIZE_COMMON));
		registerRule(buildRule(null, MoreMystcraftGrammarRules.SIZE_SUN, MoreMystcraftGrammarData.SIZE_UNCOMMON));
		registerRule(buildRule(null, MoreMystcraftGrammarRules.SIZE_SUN, MoreMystcraftGrammarData.SIZE_RARE));
		registerRule(buildRule(0, MoreMystcraftGrammarRules.SIZE_SUN, MoreMystcraftGrammarData.SIZE_COMMON));
		registerRule(buildRule(2, MoreMystcraftGrammarRules.SIZE_SUN, MoreMystcraftGrammarData.SIZE_UNCOMMON));
		registerRule(buildRule(4, MoreMystcraftGrammarRules.SIZE_SUN, MoreMystcraftGrammarData.SIZE_RARE));
		registerRule(buildRule(0, MoreMystcraftGrammarRules.SIZE_ISLAND_BIOCON, MoreMystcraftGrammarData.SIZE_COMMON));
		registerRule(buildRule(1, MoreMystcraftGrammarRules.SIZE_ISLAND_BIOCON, MoreMystcraftGrammarData.SIZE_UNCOMMON));
		registerRule(buildRule(2, MoreMystcraftGrammarRules.SIZE_ISLAND_BIOCON, MoreMystcraftGrammarData.SIZE_RARE));
		registerRule(buildRule(null, MoreMystcraftGrammarRules.TILT_SUN_SEQ, MoreMystcraftGrammarData.TILT_COMMON));
		registerRule(buildRule(null, MoreMystcraftGrammarRules.TILT_SUN_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON));
		registerRule(buildRule(null, MoreMystcraftGrammarRules.TILT_SUN_SEQ, MoreMystcraftGrammarData.TILT_RARE));
		registerRule(buildRule(0, MoreMystcraftGrammarRules.TILT_SUN_SEQ, MoreMystcraftGrammarData.TILT_COMMON_SEQ));
		registerRule(buildRule(2, MoreMystcraftGrammarRules.TILT_SUN_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON_SEQ));
		registerRule(buildRule(4, MoreMystcraftGrammarRules.TILT_SUN_SEQ, MoreMystcraftGrammarData.TILT_RARE_SEQ));
		registerRule(buildRule(0, MoreMystcraftGrammarData.TILT_COMMON_SEQ, MoreMystcraftGrammarData.TILT_COMMON));
		registerRule(buildRule(1, MoreMystcraftGrammarData.TILT_COMMON_SEQ, MoreMystcraftGrammarData.TILT_COMMON, MoreMystcraftGrammarData.TILT_UNCOMMON));
		registerRule(buildRule(1, MoreMystcraftGrammarData.TILT_COMMON_SEQ, MoreMystcraftGrammarData.TILT_COMMON, MoreMystcraftGrammarData.TILT_UNCOMMON, MoreMystcraftGrammarData.TILT_COMMON_SEQ));
		registerRule(buildRule(0, MoreMystcraftGrammarData.TILT_UNCOMMON_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON));
		registerRule(buildRule(1, MoreMystcraftGrammarData.TILT_UNCOMMON_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON, MoreMystcraftGrammarData.TILT_RARE));
		registerRule(buildRule(1, MoreMystcraftGrammarData.TILT_UNCOMMON_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON, MoreMystcraftGrammarData.TILT_RARE, MoreMystcraftGrammarData.TILT_UNCOMMON_SEQ));
		registerRule(buildRule(1, MoreMystcraftGrammarData.TILT_RARE_SEQ, MoreMystcraftGrammarData.TILT_RARE));
		registerRule(buildRule(0, MoreMystcraftGrammarData.TILT_RARE_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON, MoreMystcraftGrammarData.TILT_RARE));
		registerRule(buildRule(0, MoreMystcraftGrammarData.TILT_RARE_SEQ, MoreMystcraftGrammarData.TILT_UNCOMMON, MoreMystcraftGrammarData.TILT_RARE, MoreMystcraftGrammarData.TILT_UNCOMMON_SEQ));
	}

	private static void registerRule(final GrammarGenerator.Rule rule) {
		GrammarGenerator.registerRule(rule);
	}

	private static GrammarGenerator.Rule buildRule(final Integer rank, final ResourceLocation parent,
			final ResourceLocation... args) {
		final ArrayList<ResourceLocation> list = CollectionUtils.buildList(args);
		return new GrammarGenerator.Rule(parent, list, rank);
	}
}
