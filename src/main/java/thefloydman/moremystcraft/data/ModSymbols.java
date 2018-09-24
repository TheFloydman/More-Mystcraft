package thefloydman.moremystcraft.data;

import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.world.AgeController;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.symbol.SymbolBase;
import thefloydman.moremystcraft.symbol.symbols.SymbolAbandonedStudy;
import thefloydman.moremystcraft.symbol.symbols.SymbolPyramids;

public class ModSymbols {

	public static void initialize() {
		registerSymbol(new SymbolAbandonedStudy(forMoreMystcraft("abandoned_study")), 3,
				new String[] { "Civilization", "Possibility", "Power", "Wisdom" });
		registerSymbol(new SymbolPyramids(forMoreMystcraft("pyramids")), 2,
				new String[] { "Civilization", "Possibility", "Power", "Wisdom" });
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
