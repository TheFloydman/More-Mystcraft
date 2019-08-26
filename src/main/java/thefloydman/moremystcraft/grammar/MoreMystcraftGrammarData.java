package thefloydman.moremystcraft.grammar;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftGrammarData {
	public static final ResourceLocation SIZE_SEQ;
	public static final ResourceLocation SIZE_BASIC;
	public static final ResourceLocation SIZE_COMMON;
	public static final ResourceLocation SIZE_UNCOMMON;
	public static final ResourceLocation SIZE_RARE;
	public static final ResourceLocation TILT_COMMON;
	public static final ResourceLocation TILT_UNCOMMON;
	public static final ResourceLocation TILT_RARE;
	public static final ResourceLocation TILT_COMMON_SEQ;
	public static final ResourceLocation TILT_UNCOMMON_SEQ;
	public static final ResourceLocation TILT_RARE_SEQ;
	public static final ResourceLocation SUN_COLOR;
	public static final ResourceLocation PYRAMID_BLOCK_LIST;

	static {
		SIZE_SEQ = Reference.forMoreMystcraft("size");
		SIZE_BASIC = Reference.forMoreMystcraft("size_basic");
		SIZE_COMMON = Reference.forMoreMystcraft("size_common");
		SIZE_UNCOMMON = Reference.forMoreMystcraft("size_uncommon");
		SIZE_RARE = Reference.forMoreMystcraft("size_rare");

		TILT_COMMON = Reference.forMoreMystcraft("tilt_common");
		TILT_UNCOMMON = Reference.forMoreMystcraft("tilt_uncommon");
		TILT_RARE = Reference.forMoreMystcraft("tilt_rare");
		TILT_COMMON_SEQ = Reference.forMoreMystcraft("tilt_common_seq");
		TILT_UNCOMMON_SEQ = Reference.forMoreMystcraft("tilt_uncommon_seq");
		TILT_RARE_SEQ = Reference.forMoreMystcraft("tilt_rare_seq");

		SUN_COLOR = Reference.forMoreMystcraft("sun_color");

		PYRAMID_BLOCK_LIST = Reference.forMoreMystcraft("structure_block_list");
	}
}