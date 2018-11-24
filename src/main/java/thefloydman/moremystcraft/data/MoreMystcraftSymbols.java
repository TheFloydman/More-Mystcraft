package thefloydman.moremystcraft.data;

import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.world.AgeController;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.symbol.modifiers.SymbolSize;
import thefloydman.moremystcraft.symbol.modifiers.SymbolTilt;
import thefloydman.moremystcraft.symbol.symbols.*;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftSymbols {

	public static void initialize() {
		registerSymbol(new SymbolAbandonedStudy(Reference.forMoreMystcraft("abandoned_study")), 3,
				new String[] { "Civilization", "Possibility", "Power", "Wisdom" });
		registerSymbol(new SymbolPyramids(Reference.forMoreMystcraft("pyramids")), 2,
				new String[] { "Civilization", "Constraint", "Creativity", "Elevate" });
		registerSymbol(new SymbolBiomeControllerRings(Reference.forMoreMystcraft("biocon_rings")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Circle" });
		registerSymbol(new SymbolBiomeControllerHorizontalBands(Reference.forMoreMystcraft("biocon_bands_hor")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Horizontal" });
		registerSymbol(new SymbolBiomeControllerVerticalBands(Reference.forMoreMystcraft("biocon_bands_ver")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Vertical" });
		registerSymbol(new SymbolBiomeControllerBoxes(Reference.forMoreMystcraft("biocon_boxes")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Square" });
		registerSymbol(new SymbolBiomeControllerNormal(Reference.forMoreMystcraft("biocon_normal")), 2,
				new String[] { "Constraint", "Nature", "Weave", "Tradition" });
		registerSymbol(new SymbolBiomeControllerIsland(Reference.forMoreMystcraft("biocon_island")), 2,
				new String[] { "Constraint", "Nature", "Weave", "Island" });
		registerSymbol(new SymbolBiomeControllerMaze(Reference.forMoreMystcraft("biocon_maze")), 2,
				new String[] { "Constraint", "Nature", "Weave", "Maze" });
		registerSymbol(new SymbolSunTinted(Reference.forMoreMystcraft("sun_tinted")), 2,
				new String[] { "Celestial", "Image", "Stimulate", "Color" });
		// Register size modifiers.
		registerSymbol(new SymbolSize(Reference.forMoreMystcraft("size_tiny"), 0.25, "Tiny"), 2, "Control", "Growth",
				"Form", "Tiny");
		registerSymbol(new SymbolSize(Reference.forMoreMystcraft("size_small"), 0.5, "Small"), 1, "Control", "Growth",
				"Form", "Small");
		registerSymbol(new SymbolSize(Reference.forMoreMystcraft("size_medium"), 1, "Medium"), 0, "Control", "Growth",
				"Form", "Medium");
		registerSymbol(new SymbolSize(Reference.forMoreMystcraft("size_large"), 2, "Large"), 1, "Control", "Growth",
				"Form", "Large");
		registerSymbol(new SymbolSize(Reference.forMoreMystcraft("size_huge"), 3, "Huge"), 2, "Control", "Growth",
				"Form", "Huge");
		// Register tilt modifiers.
		registerSymbol(new SymbolTilt(Reference.forMoreMystcraft("tilt_zero"), 0.0f, "No"), 0, "Transform", "Motion",
				"Tilt", "Zero");
		registerSymbol(new SymbolTilt(Reference.forMoreMystcraft("tilt_half"), 45.0f, "Half"), 2, "Transform", "Motion",
				"Tilt", "Half");
		registerSymbol(new SymbolTilt(Reference.forMoreMystcraft("tilt_full"), 90.0f, "Full"), 4, "Transform", "Motion",
				"Tilt", "Full");
	}

	public static void registerSymbol(MoreMystcraftSymbolBase symbol, Integer cardrank, String... poem) {
		if (poem.length != 4)
			LoggerUtils.warn("Weird poem length (%d) when registering %s",
					new Object[] { Integer.valueOf(poem.length), symbol.getRegistryName().toString() });
		symbol.setWords(poem);
		symbol.setCardRank(cardrank);
		SymbolManager.tryAddSymbol(symbol);
	}

}
