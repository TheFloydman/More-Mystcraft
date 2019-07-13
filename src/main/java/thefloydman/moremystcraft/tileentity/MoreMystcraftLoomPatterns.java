package thefloydman.moremystcraft.tileentity;

import thefloydman.moremystcraft.util.MystcraftWords;
import thefloydman.whatloomsahead.tileentity.LoomPatterns;

public class MoreMystcraftLoomPatterns {

	public static void init() {
		for (String word : MystcraftWords.getBaseWords()) {
			LoomPatterns.addPatternToMainList("mystcraft", word, "mystcraft." + word,
					"textures/entity/banner/" + word + ".png");
		}
	}

}
