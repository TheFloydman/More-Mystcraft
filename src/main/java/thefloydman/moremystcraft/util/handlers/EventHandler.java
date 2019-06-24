package thefloydman.moremystcraft.util.handlers;

import java.util.UUID;

import com.google.common.base.Optional;
import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.api.event.LinkEvent;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.item.ItemLinkbookUnlinked;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkController;
import com.xcompwiz.mystcraft.linking.LinkOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.client.render.RenderMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.capability.CapabilityMaintainerSuit;
import thefloydman.moremystcraft.entity.capability.ProviderMaintainerSuit;
import thefloydman.moremystcraft.entity.capability.ProviderSuited;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.init.MoreMystcraftEntityEntries;
import thefloydman.moremystcraft.init.MoreMystcraftItems;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.util.Reference;

@EventBusSubscriber
public class EventHandler {

	private static boolean wasWearingMaintainerSuit = false;

	@SubscribeEvent
	public static void registerColors(ColorHandlerEvent event) {
		MoreMystcraftBlocks.registerModelColors();
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		MoreMystcraftBlocks.registerMystcraftModels();
		RenderingRegistry.registerEntityRenderingHandler(EntityMaintainerSuit.class, RenderMaintainerSuit::new);
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().register(MoreMystcraftEntityEntries.MAINTAINER_SUIT);
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		World world = event.getWorld();
		if (!world.isRemote && event.getHand().equals(EnumHand.MAIN_HAND)) {
			EntityPlayer entityPlayer = event.getEntityPlayer();
			Entity entityTarget = event.getTarget();
			if (entityTarget instanceof EntityMaintainerSuit) {
				EntityMaintainerSuit entitySuit = (EntityMaintainerSuit) entityTarget;
				if (entityPlayer.isSneaking()) {
					ItemStack itemStack = new ItemStack(MoreMystcraftItems.MAINTAINER_SUIT);
					entityPlayer.addItemStackToInventory(itemStack);
					((EntityPlayerMP) (entityPlayer)).sendContainerToPlayer(entityPlayer.inventoryContainer);
					world.removeEntity(entityTarget);
				} else if (!(entityPlayer.getRidingEntity() instanceof EntityMaintainerSuit)) {
					entityPlayer.startRiding(entitySuit);
				}
			}
		}
	}

	@SubscribeEvent
	public static void keyPressed(KeyInputEvent event) {
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		MoreMystcraftItems.registerItems(event);
	}

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
	}

	@SubscribeEvent
	public static void entityHurt(LivingHurtEvent event) {
		if (event.getEntity().isRiding()) {
			if (event.getEntity().getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
				return;
			}
		}
		if (event.getEntity() instanceof EntityMaintainerSuit) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void livingAttackEntity(LivingAttackEvent event) {
		Entity victim = event.getEntity();
		Entity aggressor = event.getSource().getTrueSource();
		if (victim instanceof EntityPlayer) {
			if (victim.isRiding()) {
				if (victim.getRidingEntity() instanceof EntityMaintainerSuit) {
					event.setCanceled(true);
				}
			}
		} else if (aggressor instanceof EntityPlayer) {
			if (aggressor.isRiding()) {
				if (aggressor.getRidingEntity() instanceof EntityMaintainerSuit) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void potionApplicable(PotionEvent.PotionApplicableEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			if (entity.isRiding()) {
				if (entity.getRidingEntity() instanceof EntityMaintainerSuit) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public static void breakBlock(BlockEvent.BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void playerInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.posY <= 0) {
			return;
		}
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCancellationResult(EnumActionResult.FAIL);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void playerInteractLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCancellationResult(EnumActionResult.FAIL);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void playerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCancellationResult(EnumActionResult.FAIL);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void playerInteractRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() instanceof ItemLinking || event.getItemStack().getItem() instanceof ItemLinkbookUnlinked) {
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCancellationResult(EnumActionResult.FAIL);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLinkAlter(LinkEvent.LinkEventAlter event) {
		Entity entity = event.entity;
		Entity mount = entity.getRidingEntity();
		if (mount != null) {
			if (mount instanceof EntityMaintainerSuit) {
				entity.dismountRidingEntity();
				event.origin.removeEntity(mount);
				wasWearingMaintainerSuit = true;
			}
		}
	}

	@SubscribeEvent
	public static void onLinkAfter(LinkEvent.LinkEventEnd event) {
		Entity entity = event.entity;
		if (wasWearingMaintainerSuit == true) {
			EntityMaintainerSuit suit = new EntityMaintainerSuit(event.destination);
			suit.setPosition(entity.posX, entity.posY, entity.posZ);
			event.destination.spawnEntity(suit);
			wasWearingMaintainerSuit = false;
			entity.startRiding(suit);
		}
	}

	@SubscribeEvent
	public static void renderPlayer(RenderPlayerEvent.Pre event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void renderHand(RenderSpecificHandEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void pickupItem(EntityItemPickupEvent event) {
		Entity entity = event.getEntity();
		if (entity.isRiding()) {
			if (entity.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void tossItem(ItemTossEvent event) {
		EntityPlayer player = event.getPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
				ItemStack item = event.getEntityItem().getItem();
				player.addItemStackToInventory(item);
			}
		}
	}

	/*
	 * @SubscribeEvent public static void placeBlock(BlockEvent.PlaceEvent event) {
	 * EntityPlayer player = event.getPlayer(); if (player.isRiding()) { if
	 * (player.getRidingEntity() instanceof EntityMaintainerSuit) {
	 * event.setCanceled(true); } } }
	 */

}
