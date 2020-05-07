package thefloydman.moremystcraft.data;

import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.symbol.SymbolManager;

import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;
import thefloydman.moremystcraft.symbol.modifiers.SymbolSunColor;
import thefloydman.moremystcraft.symbol.symbols.SymbolAbandonedStudy;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerBoxes;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerHorizontalBands;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerIsland;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerMaze;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerNormal;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerRings;
import thefloydman.moremystcraft.symbol.symbols.SymbolBiomeControllerVerticalBands;
import thefloydman.moremystcraft.symbol.symbols.SymbolGiganticTrees;
import thefloydman.moremystcraft.symbol.symbols.SymbolLackingOres;
import thefloydman.moremystcraft.symbol.symbols.SymbolNoLibraries;
import thefloydman.moremystcraft.symbol.symbols.SymbolOceanMonument;
import thefloydman.moremystcraft.symbol.symbols.SymbolPyramids;
import thefloydman.moremystcraft.symbol.symbols.SymbolTerrainGenMaze;
import thefloydman.moremystcraft.symbol.symbols.SymbolTintedSun;
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
		registerSymbol(new SymbolTintedSun(Reference.forMoreMystcraft("sun_tinted")), 2,
				new String[] { "Celestial", "Image", "Stimulate", "Color" });
		registerSymbol(new SymbolOceanMonument(Reference.forMoreMystcraft("ocean_monument")), 3, "Civilization",
				"Machine", "Power", "Entropy");
		registerSymbol(new SymbolTerrainGenMaze(Reference.forMoreMystcraft("terrain_maze")), 4,
				new String[] { "Terrain", "Form", "Infinite", "Maze" });
		registerSymbol(new SymbolGiganticTrees(Reference.forMoreMystcraft("gigantic_trees")), 2,
				new String[] { "Nature", "Stimulate", "Spur", "Elevate" });
		registerSymbol(new SymbolNoLibraries(Reference.forMoreMystcraft("no_libraries")), null,
				new String[] { "Civilization", "Contradict", "Inhibit", "Void" });
		registerSymbol(new SymbolLackingOres(Reference.forMoreMystcraft("lacking_ores")), 3,
				new String[] { "Nature", "Possibility", "Form", "Void" });
		registerSymbol(new SymbolSunColor(Reference.forMoreMystcraft("sun_color")), 1,
				new String[] { "Celestial", "Image", "Color", "Change" });
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
