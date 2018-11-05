package thefloydman.moremystcraft.grammar;

import net.minecraft.util.ResourceLocation;

public class ModGrammarData
{
    public static final ResourceLocation SIZE_SEQ;
    public static final ResourceLocation SIZE_BASIC;
    public static final ResourceLocation SIZE_COMMON;
    public static final ResourceLocation SIZE_UNCOMMON;
    public static final ResourceLocation SIZE_RARE;
    
    private static ResourceLocation forMoreMystcraft(final String path) {
        return new ResourceLocation("moremystcraft", path);
    }
    
    static {
        SIZE_SEQ = forMoreMystcraft("size");
        SIZE_BASIC = forMoreMystcraft("size_basic");
        SIZE_COMMON = forMoreMystcraft("size_common");
        SIZE_UNCOMMON = forMoreMystcraft("size_uncommon");
        SIZE_RARE = forMoreMystcraft("size_rare");
    }
}