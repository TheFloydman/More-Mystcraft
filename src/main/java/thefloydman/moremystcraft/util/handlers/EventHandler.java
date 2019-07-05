package thefloydman.moremystcraft.util.handlers;

import java.util.List;

import com.xcompwiz.mystcraft.world.WorldProviderMyst;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.client.render.RenderMaintainerSuit;
import thefloydman.moremystcraft.client.render.RenderPotionDummy;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityPotionDummy;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.init.MoreMystcraftEntityEntries;
import thefloydman.moremystcraft.init.MoreMystcraftItems;
import thefloydman.moremystcraft.item.ItemMaintainerSuit;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;

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

}
