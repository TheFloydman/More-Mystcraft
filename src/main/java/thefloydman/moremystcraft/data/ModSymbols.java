package thefloydman.moremystcraft.data;

import com.xcompwiz.mystcraft.logging.LoggerUtils;
import com.xcompwiz.mystcraft.symbol.SymbolManager;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.symbol.SymbolBase;
import thefloydman.moremystcraft.symbol.symbols.SymbolAbandonedStudy;

public class ModSymbols {

	public static void registerSymbol(SymbolBase symbol, Integer cardrank, String... poem) {
		if (poem.length != 4)
			LoggerUtils.warn("Weird poem length (%d) when registering %s",
					new Object[] { Integer.valueOf(poem.length), symbol.getRegistryName().toString() });
		symbol.setWords(poem);
		symbol.setCardRank(cardrank);
		SymbolManager.tryAddSymbol(symbol);
	}

	public static void initialize() {
		registerSymbol(new SymbolAbandonedStudy(forMoreMyst("abandoned_study")), 3,
				new String[] { "Civilization", "Possibility", "Power", "Wisdom" });
	}

	private static ResourceLocation forMoreMyst(String name) {
		return new ResourceLocation("moremystcraft", name);
	}

}
