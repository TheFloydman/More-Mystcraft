package thefloydman.moremystcraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
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
import com.xcompwiz.mystcraft.world.ChunkProviderMyst;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.proxy.ClientProxy;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.world.gen.feature.WorldGenLibraryReplacement;

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
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		ClientProxy.registerRenders(event);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerColors(ColorHandlerEvent event) {
		ClientProxy.registerColors(event);
	}

}
