package thefloydman.moremystcraft.init;

import java.awt.Color;

import com.xcompwiz.mystcraft.data.ModRegistryPrimer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thefloydman.moremystcraft.block.BlockJourneyCloth;
import thefloydman.moremystcraft.block.BlockJourneyHub;
import thefloydman.moremystcraft.block.BlockLockedBookstand;
import thefloydman.moremystcraft.block.BlockLockedLectern;
import thefloydman.moremystcraft.block.BlockNexusController;
import thefloydman.moremystcraft.block.BlockTrafficCone;
import thefloydman.moremystcraft.block.BlockUnstableBookReceptacle;
import thefloydman.moremystcraft.block.BlockUnstablePortal;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.item.ItemJourneyCloth;
import thefloydman.moremystcraft.item.ItemJourneyHub;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.Reference;

@EventBusSubscriber
public class MoreMystcraftBlocks {

	public static final Block LOCKED_LECTERN = (Block) new BlockLockedLectern();
	public static final Block LOCKED_BOOKSTAND = (Block) new BlockLockedBookstand();
	public static final Block TRAFFIC_CONE = (Block) new BlockTrafficCone();
	public static final Block UNSTABLE_RECEPTACLE = (Block) new BlockUnstableBookReceptacle();
	public static final Block UNSTABLE_PORTAL = (Block) new BlockUnstablePortal();
	public static final Block NEXUS_CONTROLLER = (Block) new BlockNexusController();
	public static final Block JOURNEY_CLOTH_HAND = (Block) new BlockJourneyCloth(JourneyClothUtils.Type.HAND);
	public static final Block JOURNEY_HUB_HAND = (Block) new BlockJourneyHub(JourneyClothUtils.Type.HAND);
	public static final Block JOURNEY_CLOTH_SHELL = (Block) new BlockJourneyCloth(JourneyClothUtils.Type.SHELL);
	public static final Block JOURNEY_HUB_SHELL = (Block) new BlockJourneyHub(JourneyClothUtils.Type.SHELL);

	public static final Item JOURNEY_CLOTH_HAND_ITEM = new ItemJourneyCloth(JOURNEY_CLOTH_HAND,
			((BlockJourneyCloth) JOURNEY_CLOTH_HAND).TYPE);
	public static final Item JOURNEY_CLOTH_SHELL_ITEM = new ItemJourneyCloth(JOURNEY_CLOTH_SHELL,
			((BlockJourneyCloth) JOURNEY_CLOTH_SHELL).TYPE);
	public static final Item JOURNEY_HUB_HAND_ITEM = new ItemJourneyHub(JOURNEY_HUB_HAND,
			((BlockJourneyHub) JOURNEY_HUB_HAND).TYPE);
	public static final Item JOURNEY_HUB_SHELL_ITEM = new ItemJourneyHub(JOURNEY_HUB_SHELL,
			((BlockJourneyHub) JOURNEY_HUB_SHELL).TYPE);

	public static void init() {
		if (new MoreMystcraftConfig().getLockedLecternEnabled() == true) {
			LOCKED_LECTERN.setRegistryName(Reference.forMoreMystcraft("locked_lectern"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_LECTERN);
			registerMystcraftItemBlock(LOCKED_LECTERN);
		}
		if (new MoreMystcraftConfig().getLockedBookstandEnabled() == true) {
			LOCKED_BOOKSTAND.setRegistryName(Reference.forMoreMystcraft("locked_bookstand"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_BOOKSTAND);
			registerMystcraftItemBlock(LOCKED_BOOKSTAND);
		}

		if (new MoreMystcraftConfig().getUnstableReceptacleEnabled() == true) {

			UNSTABLE_RECEPTACLE.setRegistryName(Reference.forMoreMystcraft("unstable_receptacle"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) UNSTABLE_RECEPTACLE);
			registerMystcraftItemBlock(UNSTABLE_RECEPTACLE);

			UNSTABLE_PORTAL.setRegistryName(Reference.forMoreMystcraft("unstable_portal"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) UNSTABLE_PORTAL);
			registerMystcraftItemBlock(UNSTABLE_PORTAL);
		}
	}

	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		if (new MoreMystcraftConfig().getTrafficConeEnabled() == true) {
			event.getRegistry().register(TRAFFIC_CONE);
		}
		if (new MoreMystcraftConfig().getNexusControllerEnabled() == true) {
			event.getRegistry().register(NEXUS_CONTROLLER);
		}
		event.getRegistry().register(JOURNEY_CLOTH_HAND);
		event.getRegistry().register(JOURNEY_CLOTH_SHELL);
		event.getRegistry().register(JOURNEY_HUB_HAND);
		event.getRegistry().register(JOURNEY_HUB_SHELL);
	}

	@SubscribeEvent
	public static void registerItemBlock(RegistryEvent.Register<Item> event) {
		if (new MoreMystcraftConfig().getTrafficConeEnabled() == true) {
			event.getRegistry().register(new ItemBlock(TRAFFIC_CONE).setRegistryName(TRAFFIC_CONE.getRegistryName()));
		}
		if (new MoreMystcraftConfig().getNexusControllerEnabled() == true) {
			event.getRegistry()
					.register(new ItemBlock(NEXUS_CONTROLLER).setRegistryName(NEXUS_CONTROLLER.getRegistryName()));
		}
		event.getRegistry().register(JOURNEY_CLOTH_HAND_ITEM);
		event.getRegistry().register(JOURNEY_CLOTH_SHELL_ITEM);
		event.getRegistry().register(JOURNEY_HUB_HAND_ITEM);
		event.getRegistry().register(JOURNEY_HUB_SHELL_ITEM);
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		if (new MoreMystcraftConfig().getTrafficConeEnabled() == true) {
			registerRender(Item.getItemFromBlock(TRAFFIC_CONE));
		}
		if (new MoreMystcraftConfig().getNexusControllerEnabled() == true) {
			registerRender(Item.getItemFromBlock(NEXUS_CONTROLLER));
		}
		registerRender(Item.getItemFromBlock(JOURNEY_CLOTH_HAND));
		registerRender(Item.getItemFromBlock(JOURNEY_CLOTH_SHELL));
		registerRender(Item.getItemFromBlock(JOURNEY_HUB_HAND));
		registerRender(Item.getItemFromBlock(JOURNEY_HUB_SHELL));
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
		if (new MoreMystcraftConfig().getLockedLecternEnabled() == true) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LOCKED_LECTERN), 0,
					mrlItemBlockModel("blocklectern"));
		}
		if (new MoreMystcraftConfig().getLockedBookstandEnabled() == true) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LOCKED_BOOKSTAND), 0,
					mrlItemBlockModel("blockbookstand"));
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(UNSTABLE_RECEPTACLE), 0,
				mrlItemBlockModelMore("unstable_receptacle"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(UNSTABLE_PORTAL), 0,
				mrlItemBlockModelMore("unstable_portal"));
	}

	private static ModelResourceLocation mrlItemBlockModel(final String name) {
		return new ModelResourceLocation(new ResourceLocation("mystcraft", name), "inventory");
	}

	private static ModelResourceLocation mrlItemBlockModelMore(final String name) {
		return new ModelResourceLocation(new ResourceLocation("moremystcraft", name), "inventory");
	}

	@SideOnly(Side.CLIENT)
	public static void registerModelColors() {
		final BlockColors colors = Minecraft.getMinecraft().getBlockColors();
		colors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> new Color(255, 0, 0).getRGB(),
				MoreMystcraftBlocks.UNSTABLE_PORTAL);
	}
}