package thefloydman.moremystcraft.init;

import java.util.ArrayList;
import java.util.List;

import com.xcompwiz.mystcraft.config.MystConfig;
import com.xcompwiz.mystcraft.data.ModRegistryPrimer;

import thefloydman.moremystcraft.block.BlockLockedBookstand;
import thefloydman.moremystcraft.block.BlockLockedLectern;
import thefloydman.moremystcraft.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import thefloydman.moremystcraft.util.Reference;

public class ModBlocks {
	public static final Block LOCKED_LECTERN = (Block) new BlockLockedLectern();
	public static final Block LOCKED_BOOKSTAND = (Block) new BlockLockedBookstand();

	public static void init() {
		if (ModConfig.lockedLecternEnabled == true) {
			LOCKED_LECTERN.setRegistryName(new ResourceLocation("moremystcraft", "locked_lectern"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_LECTERN);
			registerItemBlock(LOCKED_LECTERN);
		}
		if (ModConfig.lockedBookstandEnabled == true) {
			LOCKED_BOOKSTAND.setRegistryName(new ResourceLocation("moremystcraft", "locked_bookstand"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_BOOKSTAND);
			registerItemBlock(LOCKED_BOOKSTAND);
		}
	}

	private static void registerItemBlock(final Block b) {
		final ItemBlock ib = new ItemBlock(b);
		ib.setRegistryName(b.getRegistryName());
		ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) ib);
	}
	
	public static void registerModels() {
		if (ModConfig.lockedLecternEnabled == true) {
			ModelLoader.setCustomModelResourceLocation(
					Item.getItemFromBlock(LOCKED_LECTERN), 0,
					mrlItemBlockModel("blocklectern"));
		}
		if (ModConfig.lockedBookstandEnabled == true) {
			ModelLoader.setCustomModelResourceLocation(
					Item.getItemFromBlock(LOCKED_BOOKSTAND), 0,
					mrlItemBlockModel("blockbookstand"));
		}
	}

	private static ModelResourceLocation mrlItemBlockModel(final String name) {
		return new ModelResourceLocation(new ResourceLocation("mystcraft", name), "inventory");
	}
}