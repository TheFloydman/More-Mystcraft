package thefloydman.moremystcraft.util.handlers;

import java.util.List;

import com.xcompwiz.mystcraft.api.event.LinkEvent;
import com.xcompwiz.mystcraft.item.ItemLinkbookUnlinked;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.capability.ICapabilityAdventurePanel;
import thefloydman.moremystcraft.capability.ICapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.capability.ProviderCapabilityAdventurePanel;
import thefloydman.moremystcraft.capability.ProviderCapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.client.render.RenderMaintainerSuit;
import thefloydman.moremystcraft.client.render.RenderPotionDummy;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityPotionDummy;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.init.MoreMystcraftEntityEntries;
import thefloydman.moremystcraft.init.MoreMystcraftItems;
import thefloydman.moremystcraft.item.ItemMaintainerSuit;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.util.Reference;

@EventBusSubscriber
public class EventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerColors(ColorHandlerEvent.Block event) {
		// MoreMystcraftBlocks.registerModelColors();
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		MoreMystcraftBlocks.registerMystcraftModels();
		if (MoreMystcraftConfig.getMaintainerSuitEnabled()) {
			RenderingRegistry.registerEntityRenderingHandler(EntityMaintainerSuit.class, RenderMaintainerSuit::new);
			RenderingRegistry.registerEntityRenderingHandler(EntityPotionDummy.class, RenderPotionDummy::new);
		}
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		if (MoreMystcraftConfig.getMaintainerSuitEnabled()) {
			event.getRegistry().register(MoreMystcraftEntityEntries.MAINTAINER_SUIT);
			event.getRegistry().register(MoreMystcraftEntityEntries.POTION_DUMMY);
		}
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		CraftingHandler.handleRecipes(event.getRegistry());
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

	@SubscribeEvent
	public static void onItemToss(ItemTossEvent event) {
		Entity player = event.getPlayer();
		World world = player.getEntityWorld();
		if (!world.isRemote) {
			if (event.getEntityItem().getItem().getItem() instanceof ItemMaintainerSuit) {
				event.setCanceled(true);
				Vec3d playerPos = player.getPositionVector();
				Vec3d playerLook = player.getLookVec();
				Vec3d playerEyes = playerPos.addVector(0.0D, (double) player.getEyeHeight(), 0.0D);
				Vec3d playerEnd = playerEyes.add(playerLook).add(playerLook);
				RayTraceResult result = world.rayTraceBlocks(playerEyes, playerEnd, false, false, true);
				Vec3d hitVec = playerEnd;
				if (!result.typeOfHit.equals(RayTraceResult.Type.MISS)) {
					hitVec = result.hitVec;
				}
				Vec3d groundVec = hitVec.addVector(0.0D, -2.0D, 0.0D);
				BlockPos groundPos = world.rayTraceBlocks(hitVec, groundVec, false, false, true).getBlockPos();
				EntityMaintainerSuit entity = new EntityMaintainerSuit(world);
				entity.setPosition(groundPos.getX() + 0.5D, groundPos.getY() + 1.0D, groundPos.getZ() + 0.5D);
				world.spawnEntity(entity);
			}
		}
	}

	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof EntityPlayer) {
			event.addCapability(Reference.forMoreMystcraft("journey_cloths_activated"),
					new ProviderCapabilityJourneyClothsCollected());
			event.addCapability(Reference.forMoreMystcraft("previous_gamemode"),
					new ProviderCapabilityAdventurePanel());
		}
	}

	@SubscribeEvent
	public static void onLinkStart(LinkEvent.LinkEventStart event) {

		if (event.entity instanceof EntityPlayer) {

			EntityPlayerMP player = (EntityPlayerMP) event.entity;

			ICapabilityAdventurePanel cap = event.entity.getCapability(ProviderCapabilityAdventurePanel.ADVENTURE_PANEL,
					null);
			GameType type = cap.getPreviousGameMode();
			if (type != null) {
				player.setGameType(type);
			}
			cap.setPreviousGameMode(player.interactionManager.getGameType());

			if (MoreMystcraftConfig.getPostMessageOnLink()) {
				PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
				String[] names = playerList.getOnlinePlayerNames();
				for (String name : names) {
					if (!name.equals(player.getDisplayNameString())) {
						MoreMystcraftPacketHandler.sendTranslatedMessage(playerList.getPlayerByUsername(name),
								Reference.Message.PLAYER_LINKING.key, Reference.MessageType.LINKING_LAG.ordinal(),
								player.getDisplayNameString());
					}
				}
			}

		}

	}

	@SubscribeEvent
	public static void onLinkEnd(LinkEvent.LinkEventEnd event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayerMP player = (EntityPlayerMP) event.entity;
			ICapabilityAdventurePanel cap = event.entity.getCapability(ProviderCapabilityAdventurePanel.ADVENTURE_PANEL,
					null);
			if (event.info.getFlag("Adventure")) {
				player.setGameType(GameType.ADVENTURE);
				MoreMystcraftPacketHandler.sendTranslatedMessage(player, Reference.Message.CHANGE_TO_ADVENTURE_MODE.key,
						Reference.MessageType.STATUS.ordinal(), "none");
				cap.setLinkedToAdventure(true);
			} else {
				player.setGameType(cap.getPreviousGameMode());
				cap.setLinkedToAdventure(false);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		if (entity instanceof EntityPlayer && !world.isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			ICapabilityAdventurePanel cap = player.getCapability(ProviderCapabilityAdventurePanel.ADVENTURE_PANEL,
					null);
			cap.setDeathDimension(event.getEntityLiving().getEntityWorld().provider.getDimension());
		}
	}

	@SubscribeEvent
	public static void onLivingSpawn(LivingSpawnEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		if (entity instanceof EntityPlayer && !world.isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			ICapabilityAdventurePanel cap = player.getCapability(ProviderCapabilityAdventurePanel.ADVENTURE_PANEL,
					null);
			if (cap.getLinkedToAdventure() && player.interactionManager.getGameType().equals(GameType.ADVENTURE)) {
				if (cap.getDeathDimension() != world.provider.getDimension()) {
					player.setGameType(cap.getPreviousGameMode());
					cap.setLinkedToAdventure(false);
				}
			}
		}
	}

	@SubscribeEvent
	public static void playerInteractRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (!event.getWorld().isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
			if (player.interactionManager.getGameType().equals(GameType.ADVENTURE)) {
				if (event.getItemStack().getItem() instanceof ItemLinkbookUnlinked
						&& !MoreMystcraftConfig.getUnlinkedBooksEnabledInAdventureMode()) {
					MoreMystcraftPacketHandler.sendTranslatedMessage(player,
							Reference.Message.USE_UNLINKED_BOOK_IN_ADVENTURE_MODE.key,
							Reference.MessageType.STATUS.ordinal(), "none");
					event.setCancellationResult(EnumActionResult.FAIL);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {

		if (event.isWasDeath()) {

			EntityPlayer entityOld = event.getOriginal();
			EntityPlayer entityNew = event.getEntityPlayer();

			ICapabilityAdventurePanel capModeOld = entityOld
					.getCapability(ProviderCapabilityAdventurePanel.ADVENTURE_PANEL, null);
			ICapabilityAdventurePanel capModeNew = entityNew
					.getCapability(ProviderCapabilityAdventurePanel.ADVENTURE_PANEL, null);
			capModeNew.setDeathDimension(capModeOld.getDeathDimension());
			capModeNew.setLinkedToAdventure(capModeOld.getLinkedToAdventure());
			capModeNew.setPreviousGameMode(capModeOld.getPreviousGameMode());

			ICapabilityJourneyClothsCollected capJourneyOld = entityOld
					.getCapability(ProviderCapabilityJourneyClothsCollected.JOURNEY_CLOTH, null);
			ICapabilityJourneyClothsCollected capJourneyNew = entityNew
					.getCapability(ProviderCapabilityJourneyClothsCollected.JOURNEY_CLOTH, null);
			capJourneyNew.setActivatedCloths(capJourneyOld.getActivatedCloths());

		}

	}

}
