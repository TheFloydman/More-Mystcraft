package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.xcompwiz.mystcraft.api.event.LinkEvent;
import com.xcompwiz.mystcraft.item.ItemLinkbookUnlinked;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityPotionDummy;
import thefloydman.moremystcraft.init.MoreMystcraftItems;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;

@EventBusSubscriber
public class MaintainerSuitEventHandler {

	private static Map<UUID, Boolean> wasRidingSuit = new HashMap<UUID, Boolean>();
	private static Map<Integer, List<String>> potions = new HashMap<Integer, List<String>>();
	private static Map<Integer, Integer> potionListChanged = new HashMap<Integer, Integer>();

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
		if (victim instanceof EntityPotionDummy) {
			event.setCanceled(true);
		} else if (victim instanceof EntityPlayer) {
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
		int world = entity.dimension;
		if (world < 2 || !(event.getEntity().world.provider instanceof WorldProviderMyst)) {
			return;
		}
		if (((WorldProviderMyst) event.getEntity().world.provider).getAgeController().getInstabilityScore() <= 0) {
			return;
		}
		if (entity instanceof EntityPotionDummy) {
			String potion = event.getPotionEffect().getEffectName();
			if (!potions.containsKey(world)) {
				potions.put(world, new ArrayList<String>());
			}
			if (!potionListChanged.containsKey(world)) {
				potionListChanged.put(world, 0);
			}
			if (!potions.get(world).contains(potion) && potionListChanged.get(world) == 0) {
				potions.get(world).add(0, potion);
				potionListChanged.put(world, potionListChanged.get(world) + 1);
			}
		} else if (entity instanceof EntityPlayer) {
			if (entity.isRiding()) {
				if (entity.getRidingEntity() instanceof EntityMaintainerSuit) {
					event.setResult(Result.DENY);
					if (potionListChanged.get(world) != null) {
						if (potionListChanged.get(world) > 0) {
							for (int i = 0; i < potionListChanged.get(world); i++) {
								MoreMystcraftPacketHandler.sendPotion(world, potions.get(world).get(i),
										(EntityPlayerMP) entity);
								/*
								 * MoreMystcraftSavedDataPerDimension savedData =
								 * MoreMystcraftSavedDataPerDimension .get(entity.getEntityWorld()); NBTTagList
								 * effects = savedData.getPotionEffects(); boolean hasEffect = false; for (int j
								 * = 0; j < effects.tagCount(); j++) { if
								 * (effects.getStringTagAt(j).equals(potion)) { hasEffect = true; break; } } if
								 * (hasEffect == false) { effects.appendTag(new NBTTagString(potion));
								 * savedData.setPotionEffects(effects); }
								 */
							}
							potionListChanged.put(world, 0);
						}
					}
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
		if (event.getItemStack().getItem() instanceof ItemLinking
				|| event.getItemStack().getItem() instanceof ItemLinkbookUnlinked) {
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
				wasRidingSuit.put(entity.getUniqueID(), true);
			}
		}
	}

	@SubscribeEvent
	public static void onLinkAfter(LinkEvent.LinkEventEnd event) {
		Entity entity = event.entity;
		UUID uuid = entity.getUniqueID();
		if (!wasRidingSuit.containsKey(uuid)) {
			wasRidingSuit.put(uuid, false);
		}
		if (wasRidingSuit.get(entity.getUniqueID())) {
			EntityMaintainerSuit suit = new EntityMaintainerSuit(event.destination);
			suit.setPosition(entity.posX, entity.posY, entity.posZ);
			event.destination.spawnEntity(suit);
			wasRidingSuit.put(entity.getUniqueID(), false);
			entity.startRiding(suit);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderPlayer(RenderPlayerEvent.Pre event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.isRiding()) {
			if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
				event.setCanceled(true);
			}
		}
	}

	@SideOnly(Side.CLIENT)
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType().equals(ElementType.CROSSHAIRS)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			if (player.isRiding()) {
				if (player.getRidingEntity() instanceof EntityMaintainerSuit) {
					event.setCanceled(true);
				}
			}
		}
	}

	public static void clearPotionsList() {
		potions = new HashMap<Integer, List<String>>();
		potionListChanged = new HashMap<Integer, Integer>();
	}

	public static void clearPotionsList(int dim) {
		potions.remove(dim);
		potionListChanged.remove(dim);
	}

	public static void setPotionsList(Integer dim, List<String> effects) {
		potions = new HashMap<Integer, List<String>>();
		potions.put(dim, effects);
		potionListChanged = new HashMap<Integer, Integer>();
		potionListChanged.put(dim, effects.size());
	}

	public static List<String> getPotionsList(int dim) {
		return potions.get(dim);
	}

}
