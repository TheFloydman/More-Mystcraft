package thefloydman.moremystcraft.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import thefloydman.moremystcraft.client.gui.GuiMaintainerSuit;
import thefloydman.moremystcraft.client.render.RenderJourney;
import thefloydman.moremystcraft.client.render.RenderUnstableBookReceptacle;
import thefloydman.moremystcraft.tileentity.TileEntityJourney;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;
import thefloydman.moremystcraft.util.handlers.PotionListHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new GuiMaintainerSuit());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityUnstableBookReceptacle.class,
				new RenderUnstableBookReceptacle());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJourney.class, new RenderJourney());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void serverStop(FMLServerStoppedEvent event) {
		super.serverStop(event);
		PotionListHandler.clearPotionsList();
	}

}