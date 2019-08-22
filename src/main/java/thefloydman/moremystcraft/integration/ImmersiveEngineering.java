package thefloydman.moremystcraft.integration;

import com.xcompwiz.mystcraft.data.ModFluids;
import com.xcompwiz.mystcraft.data.ModItems;

import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ImmersiveEngineering {

	public static void addBottlingMachineRecipes() {

		BottlingMachineRecipe.addRecipe(new ItemStack(ModItems.inkvial), Items.GLASS_BOTTLE,
				new FluidStack(ModFluids.black_ink, 250));

	}

}
