package thefloydman.moremystcraft.util;

public class JourneyUtils {

	public static enum PatternType {
		HAND, SHELL, SPIRAL;
	}

	public static enum BlockType {
		CLOTH, HUB;
	}

	public static String blockTypeToString(BlockType type) {
		return type.toString().toLowerCase();
	}

	public static BlockType stringToBlockType(String str) {
		for (BlockType type : BlockType.values()) {
			if (blockTypeToString(type).equals(str)) {
				return type;
			}
		}
		return BlockType.CLOTH;
	}

}
