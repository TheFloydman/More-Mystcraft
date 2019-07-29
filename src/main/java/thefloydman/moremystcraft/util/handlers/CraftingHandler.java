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

public class CraftingHandler {
	public static void RegisterRecipes() {

	}

	public static void removeRecipes(IForgeRegistry<IRecipe> registry) {
		IForgeRegistryModifiable<IRecipe> recipeRegistry = (IForgeRegistryModifiable<IRecipe>) registry;
		List<IRecipe> recipes = new ArrayList(recipeRegistry.getValuesCollection());

		for (IRecipe r : recipes) {
			ItemStack output = r.getRecipeOutput();
			if (new MoreMystcraftConfig().getBookBinderRecipeEnabled() != true) {
				if (output.getItem() == Item.getItemFromBlock(com.xcompwiz.mystcraft.data.ModBlocks.bookbinder)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
			if (new MoreMystcraftConfig().getLockedBookstandRecipeEnabled() != true) {
				if (output.getItem() == Item.getItemFromBlock(MoreMystcraftBlocks.LOCKED_BOOKSTAND)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
			if (new MoreMystcraftConfig().getLockedLecternRecipeEnabled() != true) {
				if (output.getItem() == Item.getItemFromBlock(MoreMystcraftBlocks.LOCKED_LECTERN)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
			if (new MoreMystcraftConfig().getTrafficConeRecipeEnabled() != true) {
				if (output.getItem() == Item.getItemFromBlock(MoreMystcraftBlocks.TRAFFIC_CONE)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
			if (new MoreMystcraftConfig().getNexusControllerRecipeEnabled() != true) {
				if (output.getItem() == Item.getItemFromBlock(MoreMystcraftBlocks.NEXUS_CONTROLLER)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
			if (new MoreMystcraftConfig().getUnstableReceptacleRecipeEnabled() != true) {
				if (output.getItem() == Item.getItemFromBlock(MoreMystcraftBlocks.UNSTABLE_RECEPTACLE)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
		}
	}
}