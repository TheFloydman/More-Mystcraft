package thefloydman.moremystcraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.event.LinkEvent;
import com.xcompwiz.mystcraft.api.exception.APIUndefined;
import com.xcompwiz.mystcraft.api.exception.APIVersionRemoved;
import com.xcompwiz.mystcraft.api.exception.APIVersionUndefined;
import com.xcompwiz.mystcraft.api.hook.DimensionAPI;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.symbol.SymbolRemappings;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.proxy.ClientProxy;
import thefloydman.moremystcraft.util.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
	}

	public DimensionAPI dimensionApi;

	public void init(FMLInitializationEvent event) {
		try {
			MoreMystcraft.logger.info("Initializing Mystcraft APIs");
			dimensionApi = (DimensionAPI) MystObjects.entryPoint.getProviderInstance().getAPIInstance("dimension-1");
			MoreMystcraft.logger.info("Successfully initialized Mystcraft APIs");
		} catch (APIVersionRemoved e1) {
			MoreMystcraft.logger.error("API version removed!");
		} catch (APIVersionUndefined e2) {
			MoreMystcraft.logger.error("API version undefined!");
		} catch (APIUndefined e3) {
			MoreMystcraft.logger.error("API undefined!");
		}
		/*
		// Remap and blacklist original sized Biome Distribution Pages.
		if (ModConfig.originalBioConsEnabled == false) {
			SymbolRemappings.addSymbolRemapping(forMystcraft("biocontiny"), forMoreMystcraft("size_tiny"),
					forMoreMystcraft("biocon_normal"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconsmall"), forMoreMystcraft("size_small"),
					forMoreMystcraft("biocon_normal"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconmedium"), forMoreMystcraft("size_medium"),
					forMoreMystcraft("biocon_normal"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconlarge"), forMoreMystcraft("size_large"),
					forMoreMystcraft("biocon_normal"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconhuge"), forMoreMystcraft("size_huge"),
					forMoreMystcraft("biocon_normal"));

			SymbolManager.blackListSymbol(forMystcraft("biocontiny"));
			SymbolManager.blackListSymbol(forMystcraft("bioconsmall"));
			SymbolManager.blackListSymbol(forMystcraft("bioconmedium"));
			SymbolManager.blackListSymbol(forMystcraft("bioconlarge"));
			SymbolManager.blackListSymbol(forMystcraft("bioconhuge"));
		}*/

		//MinecraftForge.EVENT_BUS.register(new GenericJumpEvent());
		//MinecraftForge.EVENT_BUS.register(new GenericUpdateEvent());
		//MinecraftForge.EVENT_BUS.register(new GenericFallEvent());
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event) {
		ClientProxy.registerRenders(event);
	}

	/*private ResourceLocation forMystcraft(String name) {
		return new ResourceLocation("mystcraft", name);
	}*/
	
/*
	static class GenericJumpEvent {

		@SubscribeEvent
		public void onLivingJumpEvent(LivingJumpEvent event) {
			double addY = 2D; // change to the entity's Y motion.
			event.getEntity().motionY *= addY;
			event.getEntity().velocityChanged = true;
			// event.entity.motionY *= addY;
			// event.entity.velocityChanged = true;
		}
	}
	
	static class GenericUpdateEvent {

		@SubscribeEvent
		public void onLivingUpdateEvent(LivingUpdateEvent event) {
			double addY = 2D; // change to the entity's Y motion.
			if (event.getEntity().fallDistance > 0) {
				event.getEntity().motionY /= addY;
			}
			//double addY = 2D; // change to the entity's Y motion.
			//event.getEntity().motionY *= addY;
			//event.getEntity().velocityChanged = true;
			// event.entity.motionY *= addY;
			// event.entity.velocityChanged = true;
		}
	}

	static class GenericFallEvent {
		@SubscribeEvent
		public void onLivingFallEvent(LivingFallEvent event) {
			float addY = 2.0F; // change to the entity's Y motion.
			event.setDistance(event.getDistance() / addY);
			event.setDamageMultiplier(event.getDamageMultiplier() / addY);
		}
	}*/
}
