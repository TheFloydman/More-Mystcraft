package thefloydman.moremystcraft;

import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thefloydman.moremystcraft.client.gui.GuiMaintainerSuit;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.data.MoreMystcraftGrammarRules;
import thefloydman.moremystcraft.data.MoreMystcraftSymbolRules;
import thefloydman.moremystcraft.data.MoreMystcraftSymbols;
import thefloydman.moremystcraft.gui.GuiHandler;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.proxy.CommonProxy;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.util.handlers.CraftingHandler;
import thefloydman.moremystcraft.util.handlers.OreGenHandler;
import thefloydman.moremystcraft.util.handlers.WorldLoadHandler;
import thefloydman.moremystcraft.world.gen.feature.WorldGenLibraryReplacement;
import thefloydman.moremystcraft.world.gen.feature.WorldGenStudy;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class MoreMystcraft {
	@Instance
	public static MoreMystcraft instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
		MoreMystcraftGrammarRules.initialize();
		GameRegistry.registerWorldGenerator(new WorldGenStudy(), Integer.MAX_VALUE);

		if (!new MoreMystcraftConfig().getLibrariesEnabled() || new MoreMystcraftConfig().getLibrariesUpgraded()) {
			GameRegistry.registerWorldGenerator(new WorldGenLibraryReplacement(), Integer.MAX_VALUE);
		}

		GameRegistry.registerTileEntity(TileEntityUnstableBookReceptacle.class,
				new ResourceLocation(Reference.MOD_ID, "unstable_receptacle"));
		MoreMystcraftSymbols.initialize();
		MoreMystcraftSymbolRules.initialize();
		MoreMystcraftBlocks.init();

		MinecraftForge.EVENT_BUS.register(new WorldLoadHandler());
		MinecraftForge.ORE_GEN_BUS.register(new OreGenHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		CraftingHandler.removeRecipes();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	@EventHandler
	public void serverStop(FMLServerStoppedEvent event) {
		proxy.serverStop(event);
	}

}
