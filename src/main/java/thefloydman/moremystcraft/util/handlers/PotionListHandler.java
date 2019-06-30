package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PotionListHandler {

	private static Map<Integer, List<String>> potions = new HashMap<Integer, List<String>>();

	public PotionListHandler() {
		clearPotionsList();
	}

	public static void receivePotion(int dim, String str) {
		if (!potions.containsKey(dim)) {
			potions.put(dim, new ArrayList<String>());
		}
		if (!potions.get(dim).contains(str)) {
			potions.get(dim).add(str);
		}
	}

	public static Map<Integer, List<String>> getPotionsList() {
		return potions;
	}

	public static void clearPotionsList() {
		potions = new HashMap<Integer, List<String>>();
	}
}
