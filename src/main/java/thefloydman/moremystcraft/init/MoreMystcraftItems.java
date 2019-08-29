package thefloydman.moremystcraft.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thefloydman.moremystcraft.item.ItemMaintainerSuit;

@EventBusSubscriber
public class MoreMystcraftItems {

	public static final Item MAINTAINER_SUIT = (Item) new ItemMaintainerSuit();

	public static void registerItems(final RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> itemRegistry = event.getRegistry();
		itemRegistry.registerAll(MAINTAINER_SUIT);

	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(MAINTAINER_SUIT);
	}

	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}