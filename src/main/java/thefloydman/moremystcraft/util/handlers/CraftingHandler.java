package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import com.xcompwiz.mystcraft.data.ModBlocks;
import com.xcompwiz.mystcraft.data.ModItems;

import thefloydman.moremystcraft.config.ModConfig;
import thefloydman.moremystcraft.data.DummyRecipe;

public class CraftingHandler {
	public static void RegisterRecipes() {

	}

	public static void removeRecipes() {
		ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
		ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());

		for (IRecipe r : recipes) {
			ItemStack output = r.getRecipeOutput();
			if (ModConfig.bookBinderRecipeEnabled != true) {
				if (output.getItem() == Item.getItemFromBlock(ModBlocks.bookbinder)) {
					recipeRegistry.remove(r.getRegistryName());
					recipeRegistry.register(DummyRecipe.from(r));
				}
			}
		}
	}
}