package thefloydman.moremystcraft.util;

import net.minecraft.util.ResourceLocation;

public class Reference {
	
	public static final String MOD_ID = "moremystcraft";
	public static final String NAME = "More Mystcraft";
	public static final String VERSION = "0.5.4";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "thefloydman.moremystcraft.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "thefloydman.moremystcraft.proxy.CommonProxy";
	public static final String DEPENDENCIES = "required-after:mystcraft";
	
	public static ResourceLocation forMoreMystcraft(final String path) {
        return new ResourceLocation("moremystcraft", path);
    }
	
}