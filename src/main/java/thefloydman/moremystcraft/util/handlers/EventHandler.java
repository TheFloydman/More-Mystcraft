package thefloydman.moremystcraft.util.handlers;

import java.util.List;

import com.xcompwiz.mystcraft.world.WorldProviderMyst;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import thefloydman.moremystcraft.client.render.RenderMaintainerSuit;
import thefloydman.moremystcraft.client.render.RenderPotionDummy;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityPotionDummy;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.init.MoreMystcraftEntityEntries;
import thefloydman.moremystcraft.init.MoreMystcraftItems;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;

@EventBusSubscriber
public class EventHandler {

	@SubscribeEvent
	public static void registerColors(ColorHandlerEvent event) {
		MoreMystcraftBlocks.registerModelColors();
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		MoreMystcraftBlocks.registerMystcraftModels();
		RenderingRegistry.registerEntityRenderingHandler(EntityMaintainerSuit.class, RenderMaintainerSuit::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPotionDummy.class, RenderPotionDummy::new);
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().register(MoreMystcraftEntityEntries.MAINTAINER_SUIT);
		event.getRegistry().register(MoreMystcraftEntityEntries.POTION_DUMMY);
	}

	@SubscribeEvent
	public static void keyPressed(KeyInputEvent event) {
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		MoreMystcraftItems.registerItems(event);
	}

	@SubscribeEvent
	public static void entityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		if (world.isRemote) {
			return;
		}
		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			if (entity.dimension > 1 && world.provider instanceof WorldProviderMyst) {
				EntityPotionDummy dummy = new EntityPotionDummy(world);
				dummy.setParent(entity.getUniqueID());
				world.spawnEntity(dummy);
				List<String> potions = MaintainerSuitEventHandler.getPotionsList(entity.dimension);
				if (potions != null) {
					for (String potion : potions) {
						MoreMystcraftPacketHandler.sendPotion(entity.dimension, potion, (EntityPlayerMP) entity);
					}
				}
			}
		}
	}

}
