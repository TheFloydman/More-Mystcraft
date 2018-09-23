package thefloydman.moremystcraft;

import org.apache.logging.log4j.Logger;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import thefloydman.moremystcraft.proxy.CommonProxy;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.world.gen.structure.feature.WorldGeneratorAbandonedStudy;
import thefloydman.moremystcraft.data.ModSymbols;
import thefloydman.moremystcraft.gui.GuiHandler;
import thefloydman.moremystcraft.init.ModBlocks;
import thefloydman.moremystcraft.util.handlers.CraftingHandler;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class MoreMystcraft {
	@Instance
	public static MoreMystcraft instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static Logger logger;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		GameRegistry.registerWorldGenerator(new WorldGeneratorAbandonedStudy(), Integer.MAX_VALUE);
		ModSymbols.initialize();
		ModBlocks.init();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
		NetworkRegistry.INSTANCE.registerGuiHandler((Object)MoreMystcraft.instance, (IGuiHandler)new GuiHandler());
		CraftingHandler.removeRecipes();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {

	}

}
