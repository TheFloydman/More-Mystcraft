package thefloydman.moremystcraft.grammar;

import net.minecraft.util.ResourceLocation;

public class ModGrammarData
{
    public static final ResourceLocation SIZE_SEQ;
    public static final ResourceLocation SIZE_BASIC;
    
    private static ResourceLocation forMoreMystcraft(final String path) {
        return new ResourceLocation("moremystcraft", path);
    }
    
    static {
        SIZE_SEQ = forMoreMystcraft("size");
        SIZE_BASIC = forMoreMystcraft("size_basic");
    }
}