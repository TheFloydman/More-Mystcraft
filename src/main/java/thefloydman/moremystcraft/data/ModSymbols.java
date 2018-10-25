package thefloydman.moremystcraft.data;

import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.world.AgeController;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.symbol.SymbolBase;
import thefloydman.moremystcraft.symbol.modifiers.SymbolSize;
import thefloydman.moremystcraft.symbol.symbols.SymbolAbandonedStudy;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerBoxes;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerHorizontalBands;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerIsland;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerNormal;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerRings;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerVerticalBands;
import thefloydman.moremystcraft.symbol.symbols.SymbolPyramids;

public class ModSymbols {

	public static void initialize() {
		registerSymbol(new SymbolAbandonedStudy(forMoreMystcraft("abandoned_study")), 3,
				new String[] { "Civilization", "Possibility", "Power", "Wisdom" });
		registerSymbol(new SymbolPyramids(forMoreMystcraft("pyramids")), 2,
				new String[] { "Civilization", "Constraint", "Creativity", "Elevate" });
		registerSymbol(new SymbolBiomeControllerRings(forMoreMystcraft("biocon_rings")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Circle" });
		registerSymbol(new SymbolBiomeControllerHorizontalBands(forMoreMystcraft("biocon_bands_hor")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Horizontal" });
		registerSymbol(new SymbolBiomeControllerVerticalBands(forMoreMystcraft("biocon_bands_ver")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Vertical" });
		registerSymbol(new SymbolBiomeControllerBoxes(forMoreMystcraft("biocon_boxes")), 3,
				new String[] { "Constraint", "Nature", "Cycle", "Square" });
		registerSymbol(new SymbolBiomeControllerNormal(forMoreMystcraft("biocon_normal")), 2,
				new String[] { "Constraint", "Nature", "Weave", "Tradition" });
		registerSymbol(new SymbolBiomeControllerIsland(forMoreMystcraft("biocon_island")), 2,
				new String[] { "Constraint", "Nature", "Weave", "Island" });
		// Register size modifiers.
		registerSymbol(new SymbolSize(forMoreMystcraft("size_tiny"), 0.25, "Tiny"), 2, "Control", "Growth", "Form",
				"Tiny");
		registerSymbol(new SymbolSize(forMoreMystcraft("size_small"), 0.5, "Small"), 1, "Control", "Growth", "Form",
				"Small");
		registerSymbol(new SymbolSize(forMoreMystcraft("size_medium"), 1, "Medium"), 0, "Control", "Growth", "Form",
				"Medium");
		registerSymbol(new SymbolSize(forMoreMystcraft("size_large"), 2, "Large"), 1, "Control", "Growth", "Form",
				"Large");
		registerSymbol(new SymbolSize(forMoreMystcraft("size_huge"), 3, "Huge"), 2, "Control", "Growth", "Form",
				"Huge");
	}

	public static void registerSymbol(SymbolBase symbol, Integer cardrank, String... poem) {
		if (poem.length != 4)
			LoggerUtils.warn("Weird poem length (%d) when registering %s",
					new Object[] { Integer.valueOf(poem.length), symbol.getRegistryName().toString() });
		symbol.setWords(poem);
		symbol.setCardRank(cardrank);
		SymbolManager.tryAddSymbol(symbol);
	}

	private static ResourceLocation forMoreMystcraft(String name) {
		return new ResourceLocation("moremystcraft", name);
	}

}
