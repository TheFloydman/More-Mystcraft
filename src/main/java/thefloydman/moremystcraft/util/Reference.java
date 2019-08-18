package thefloydman.moremystcraft.util;

import net.minecraft.util.ResourceLocation;

public class Reference {

	public static final String MOD_ID = "moremystcraft";
	public static final String NAME = "More Mystcraft";
	public static final String VERSION = "0.8.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "thefloydman.moremystcraft.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "thefloydman.moremystcraft.proxy.CommonProxy";
	public static final String DEPENDENCIES = "required-after:mystcraft";

	public static ResourceLocation forMoreMystcraft(final String path) {
		return new ResourceLocation("moremystcraft", path);
	}

	public enum Messages {
		CLOTH_ADDED_TO_HUB("journey_cloth_added_to_hub"),
		HUB_FULL("journey_hub_full"),
		CLOTH_REMOVED_FROM_HUB("journey_cloth_removed_from_hub"),
		CLOTH_ACTIVATED("journey_cloth_activated"),
		CLOTH_DEACTIVATED("journey_cloth_deactivated"),
		USE_UNLINKED_BOOK_IN_ADVENTURE_MODE("use_unlinked_book_in_adventure_mode"),
		CHANGE_TO_ADVENTURE_MODE("change_to_adventure_mode"),
		RETURN_TO_PREVIOUS_MODE("return_to_previous_mode");

		public String key;

		Messages(String str) {
			this.key = "message.moremystcraft." + str + ".desc";
		}
	}

}