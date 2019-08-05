package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.data.DummyRecipe;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.init.MoreMystcraftItems;

public class CraftingHandler {
	
	public static IForgeRegistryModifiable<IRecipe> recipeRegistry;

	public static void handleRecipes(IForgeRegistry<IRecipe> registry) {
		recipeRegistry = (IForgeRegistryModifiable<IRecipe>) registry;
		List<IRecipe> recipes = new ArrayList(recipeRegistry.getValuesCollection());

		for (IRecipe recipe : recipes) {
			for (Recipes en : Recipes.values()) {
				handleRecipe(recipe, en);
			}
		}
	}
	
	protected static void handleRecipe(IRecipe recipe, Recipes en) {
		if (en.option != true) {
			ItemStack output = recipe.getRecipeOutput();
			if (output.getItem().equals(en.item)) {
				recipeRegistry.remove(recipe.getRegistryName());
				recipeRegistry.register(DummyRecipe.from(recipe));
			}
		}
	}
	
	protected enum Recipes {
		BOOK_BINDER("book_binder"),
		LOCKED_LECTERN("locked_lectern"),
		LOCKED_BOOKSTAND("locked_bookstand"),
		TRAFFIC_CONE("traffic_cone"),
		NEXUS_CONTROLLER("nexus_controller"),
		UNSTABLE_RECEPTACLE("unstable_receptacle"),
		MAINTAINER_SUIT("maintainer_suit");
		
		protected boolean option;
		protected Item item;
		
		Recipes(String str) {
			
			Map<String, Boolean> booleanMap = new HashMap<String, Boolean>();
			booleanMap.put("book_binder", MoreMystcraftConfig.getBookBinderRecipeEnabled());
			booleanMap.put("locked_lectern", MoreMystcraftConfig.getLockedLecternRecipeEnabled());
			booleanMap.put("locked_bookstand", MoreMystcraftConfig.getLockedBookstandRecipeEnabled());
			booleanMap.put("traffic_cone", MoreMystcraftConfig.getTrafficConeRecipeEnabled());
			booleanMap.put("nexus_controller", MoreMystcraftConfig.getNexusControllerRecipeEnabled());
			booleanMap.put("unstable_receptacle", MoreMystcraftConfig.getUnstableReceptacleRecipeEnabled());
			booleanMap.put("maintainer_suit", MoreMystcraftConfig.getMaintainerSuitRecipeEnabled());
			
			Map<String, Item> itemMap = new HashMap<String, Item>();
			itemMap.put("book_binder", Item.getItemFromBlock(com.xcompwiz.mystcraft.data.ModBlocks.bookbinder));
			itemMap.put("locked_lectern", Item.getItemFromBlock(MoreMystcraftBlocks.LOCKED_LECTERN));
			itemMap.put("locked_bookstand", Item.getItemFromBlock(MoreMystcraftBlocks.LOCKED_BOOKSTAND));
			itemMap.put("traffic_cone", Item.getItemFromBlock(MoreMystcraftBlocks.TRAFFIC_CONE));
			itemMap.put("nexus_controller", Item.getItemFromBlock(MoreMystcraftBlocks.NEXUS_CONTROLLER));
			itemMap.put("unstable_receptacle", Item.getItemFromBlock(MoreMystcraftBlocks.UNSTABLE_RECEPTACLE));
			itemMap.put("maintainer_suit", MoreMystcraftItems.MAINTAINER_SUIT);
			
			this.option = booleanMap.get(str);
			this.item = itemMap.get(str);
		}
	}
}