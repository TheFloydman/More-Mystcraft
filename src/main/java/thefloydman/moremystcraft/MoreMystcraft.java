package thefloydman.moremystcraft;

import org.apache.logging.log4j.Logger;

import com.mojang.realmsclient.client.Request.Post;
import com.xcompwiz.mystcraft.world.ChunkProviderMyst;
import com.xcompwiz.mystcraft.world.gen.structure.MapGenScatteredFeatureMyst;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.world.WorldEvent;
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
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.world.gen.structure.feature.WorldGenStudy;
import thefloydman.moremystcraft.data.MoreMystcraftGrammarRules;
import thefloydman.moremystcraft.data.MoreMystcraftSymbols;
import thefloydman.moremystcraft.data.MoreMystcraftSymbolRules;
import thefloydman.moremystcraft.gui.GuiHandler;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.init.MoreMystcraftItems;
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
		proxy.preInit(event);
		MoreMystcraftGrammarRules.initialize();
		GameRegistry.registerWorldGenerator(new WorldGenStudy(), Integer.MAX_VALUE);
		GameRegistry.registerTileEntity(TileEntityUnstableBookReceptacle.class,
				new ResourceLocation(Reference.MOD_ID, "unstable_receptacle"));
		MoreMystcraftSymbols.initialize();
		MoreMystcraftSymbolRules.initialize();
		MoreMystcraftBlocks.init();
		MoreMystcraftItems.init();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
		CraftingHandler.removeRecipes();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {

	}

}
