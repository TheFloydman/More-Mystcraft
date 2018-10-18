package thefloydman.moremystcraft.data;

import net.minecraft.util.*;
import thefloydman.moremystcraft.grammar.ModGrammarData;

import com.xcompwiz.mystcraft.api.grammar.*;
import com.xcompwiz.mystcraft.grammar.*;
import com.xcompwiz.util.*;
import java.util.*;

public class ModGrammarRules {
	
    private static final ResourceLocation SIZE_GEN;
    public static final ResourceLocation SIZE_EXT;
    
    public static void initialize() {
        registerRule(buildRule(1, ModGrammarData.SIZE_SEQ, ModGrammarRules.SIZE_GEN));
        registerRule(buildRule(2, ModGrammarRules.SIZE_GEN, ModGrammarRules.SIZE_GEN, ModGrammarData.SIZE_BASIC));
        registerRule(buildRule(0, ModGrammarRules.SIZE_GEN, ModGrammarData.SIZE_BASIC));
        //registerRule(buildRule(null, ModGrammarData.SIZE_SEQ, ModGrammarRules.SIZE_EXT, ModGrammarData.SIZE_BASIC));
        //registerRule(buildRule(null, ModGrammarRules.SIZE_EXT, ModGrammarData.SIZE_SEQ));
        //registerRule(buildRule(1, ModGrammarRules.SIZE_EXT, new ResourceLocation[0]));
        registerRule(buildRule(1, ModGrammarData.SIZE_BASIC, ModGrammarRules.SIZE_GEN));
    }
    
    private static void registerRule(final GrammarGenerator.Rule rule) {
        GrammarGenerator.registerRule(rule);
    }
    
    private static ResourceLocation forMoreMystcraft(final String path) {
        return new ResourceLocation("moremystcraft", path);
    }
    
    private static GrammarGenerator.Rule buildRule(final Integer rank, final ResourceLocation parent, final ResourceLocation... args) {
        final ArrayList<ResourceLocation> list = CollectionUtils.buildList(args);
        return new GrammarGenerator.Rule(parent, list, rank);
    }
    
    static {
        SIZE_GEN = forMoreMystcraft("size_adv");
        SIZE_EXT = forMoreMystcraft("size_ext");
    }
}
