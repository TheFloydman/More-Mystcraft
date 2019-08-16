package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;
import java.util.List;

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
		BOOK_BINDER(MoreMystcraftConfig.getBookBinderRecipeEnabled(), Item.getItemFromBlock(com.xcompwiz.mystcraft.data.ModBlocks.bookbinder)),
		LOCKED_LECTERN(MoreMystcraftConfig.getLockedLecternRecipeEnabled(), Item.getItemFromBlock(MoreMystcraftBlocks.LOCKED_LECTERN)),
		LOCKED_BOOKSTAND(MoreMystcraftConfig.getLockedBookstandRecipeEnabled(), Item.getItemFromBlock(MoreMystcraftBlocks.LOCKED_BOOKSTAND)),
		TRAFFIC_CONE(MoreMystcraftConfig.getTrafficConeRecipeEnabled(), Item.getItemFromBlock(MoreMystcraftBlocks.TRAFFIC_CONE)),
		NEXUS_CONTROLLER(MoreMystcraftConfig.getNexusControllerRecipeEnabled(), Item.getItemFromBlock(MoreMystcraftBlocks.NEXUS_CONTROLLER)),
		UNSTABLE_RECEPTACLE(MoreMystcraftConfig.getUnstableReceptacleRecipeEnabled(), Item.getItemFromBlock(MoreMystcraftBlocks.UNSTABLE_RECEPTACLE)),
		MAINTAINER_SUIT(MoreMystcraftConfig.getMaintainerSuitRecipeEnabled(), MoreMystcraftItems.MAINTAINER_SUIT),
		JOURNEY_CLOTH_HAND(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_CLOTH_HAND_ITEM),
		JOURNEY_CLOTH_SHELL(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_CLOTH_SHELL_ITEM),
		JOURNEY_HUB_HAND(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_HUB_HAND_ITEM),
		JOURNEY_HUB_SHELL(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_HUB_SHELL_ITEM);
		
		protected boolean option;
		protected Item item;
		
		Recipes(boolean bool, Item it) {			
			this.option = bool;
			this.item = it;
		}
		
	}
	
}