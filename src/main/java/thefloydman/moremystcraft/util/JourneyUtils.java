package thefloydman.moremystcraft.util;

public class JourneyUtils {

	public static enum PatternType {
		HAND, SHELL, SPIRAL;
	}

	public static enum BlockType {
		CLOTH, HUB;
	}

	public static String blockTypeToString(BlockType type) {
		return type.toString();
	}

	public static BlockType stringToBlockType(String str) {
		return BlockType.valueOf(str.toUpperCase());
	}

}
