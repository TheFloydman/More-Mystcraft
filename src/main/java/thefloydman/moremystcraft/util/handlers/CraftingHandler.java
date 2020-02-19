package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
		if (en.option != true && en.item != null) {
			ItemStack output = recipe.getRecipeOutput();
			if (output.getItem().equals(en.item)) {
				recipeRegistry.remove(recipe.getRegistryName());
				recipeRegistry.register(DummyRecipe.from(recipe));
			}
		}
	}

	protected enum Recipes {
		BOOK_BINDER(MoreMystcraftConfig.getBookBinderRecipeEnabled(), com.xcompwiz.mystcraft.data.ModBlocks.bookbinder),
		LOCKED_LECTERN(MoreMystcraftConfig.getLockedLecternRecipeDisabled(), MoreMystcraftBlocks.LOCKED_LECTERN),
		LOCKED_BOOKSTAND(MoreMystcraftConfig.getLockedBookstandRecipeEnabled(), MoreMystcraftBlocks.LOCKED_BOOKSTAND),
		TRAFFIC_CONE(MoreMystcraftConfig.getTrafficConeRecipeEnabled(), MoreMystcraftBlocks.TRAFFIC_CONE),
		NEXUS_CONTROLLER(MoreMystcraftConfig.getNexusControllerRecipeEnabled(), MoreMystcraftBlocks.NEXUS_CONTROLLER),
		UNSTABLE_RECEPTACLE(MoreMystcraftConfig.getUnstableReceptacleRecipeEnabled(),
				MoreMystcraftBlocks.UNSTABLE_RECEPTACLE),
		MAINTAINER_SUIT(MoreMystcraftConfig.getMaintainerSuitRecipeEnabled(), MoreMystcraftItems.MAINTAINER_SUIT),
		JOURNEY_CLOTH_HAND(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_CLOTH_HAND_ITEM),
		JOURNEY_CLOTH_SHELL(MoreMystcraftConfig.getJourneyRecipesEnabled(),
				MoreMystcraftBlocks.JOURNEY_CLOTH_SHELL_ITEM),
		JOURNEY_HUB_HAND(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_HUB_HAND_ITEM),
		JOURNEY_HUB_SHELL(MoreMystcraftConfig.getJourneyRecipesEnabled(), MoreMystcraftBlocks.JOURNEY_HUB_SHELL_ITEM);

		protected boolean option;
		protected Object item;

		Recipes(boolean bool, Object object) {
			this.option = bool;
			this.item = null;
			if (object != null) {
				if (object instanceof Block) {
					if (ForgeRegistries.BLOCKS.containsValue((Block) object)) {
						this.item = Item.getItemFromBlock((Block) object);
					}
				} else if (object instanceof Item) {
					if (ForgeRegistries.ITEMS.containsValue((Item) object)) {
						this.item = (Item) object;
					}
				}
			}
		}

	}

}