package thefloydman.moremystcraft.init;

import java.util.ArrayList;
import java.util.List;

import com.xcompwiz.mystcraft.config.MystConfig;
import com.xcompwiz.mystcraft.data.ModRegistryPrimer;

import thefloydman.moremystcraft.block.BlockTrafficCone;
import thefloydman.moremystcraft.block.BlockLockedBookstand;
import thefloydman.moremystcraft.block.BlockLockedLectern;
import thefloydman.moremystcraft.block.BlockNexusController;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import thefloydman.moremystcraft.util.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class MoreMystcraftBlocks {
	public static final Block LOCKED_LECTERN = (Block) new BlockLockedLectern();
	public static final Block LOCKED_BOOKSTAND = (Block) new BlockLockedBookstand();
	public static final Block TRAFFIC_CONE = (Block) new BlockTrafficCone();
	// public static final Block NEXUS_CONTROLLER = (Block) new
	// BlockNexusController();

	public static void init() {
		if (MoreMystcraftConfig.blocks.lockedLecternEnabled == true) {
			LOCKED_LECTERN.setRegistryName(Reference.forMoreMystcraft("locked_lectern"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_LECTERN);
			registerMystcraftItemBlock(LOCKED_LECTERN);
		}
		if (MoreMystcraftConfig.blocks.lockedBookstandEnabled == true) {
			LOCKED_BOOKSTAND.setRegistryName(Reference.forMoreMystcraft("locked_bookstand"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_BOOKSTAND);
			registerMystcraftItemBlock(LOCKED_BOOKSTAND);
		}
	}

	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		// event.getRegistry().registerAll(NEXUS_CONTROLLER);
		if (MoreMystcraftConfig.blocks.trafficConeEnabled == true) {
			event.getRegistry().registerAll(TRAFFIC_CONE);
		}
	}

	@SubscribeEvent
	public static void registerItemBlock(RegistryEvent.Register<Item> event) {
		// event.getRegistry().registerAll(new
		// ItemBlock(NEXUS_CONTROLLER).setRegistryName(NEXUS_CONTROLLER.getRegistryName()));
		if (MoreMystcraftConfig.blocks.trafficConeEnabled == true) {
			event.getRegistry()
					.registerAll(new ItemBlock(TRAFFIC_CONE).setRegistryName(TRAFFIC_CONE.getRegistryName()));
		}
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		// registerRender(Item.getItemFromBlock(NEXUS_CONTROLLER));
		if (MoreMystcraftConfig.blocks.trafficConeEnabled == true) {
			registerRender(Item.getItemFromBlock(TRAFFIC_CONE));
		}
	}

	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static void registerMystcraftItemBlock(final Block b) {
		final ItemBlock ib = new ItemBlock(b);
		ib.setRegistryName(b.getRegistryName());
		ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) ib);
	}

	public static void registerMystcraftModels() {
		if (MoreMystcraftConfig.blocks.lockedLecternEnabled == true) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LOCKED_LECTERN), 0,
					mrlItemBlockModel("blocklectern"));
		}
		if (MoreMystcraftConfig.blocks.lockedBookstandEnabled == true) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LOCKED_BOOKSTAND), 0,
					mrlItemBlockModel("blockbookstand"));
		}
	}

	private static ModelResourceLocation mrlItemBlockModel(final String name) {
		return new ModelResourceLocation(new ResourceLocation("mystcraft", name), "inventory");
	}
}