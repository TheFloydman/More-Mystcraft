package thefloydman.moremystcraft.proxy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.client.render.RenderUnstableBookReceptacle;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;
import thefloydman.moremystcraft.util.handlers.PotionListHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileEntityUnstableBookReceptacle.class,
				(TileEntitySpecialRenderer) new RenderUnstableBookReceptacle());
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