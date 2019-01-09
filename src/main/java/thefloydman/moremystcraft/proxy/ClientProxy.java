package thefloydman.moremystcraft.proxy;

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
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import thefloydman.moremystcraft.client.render.RenderMaintainerSuitFactory;
import thefloydman.moremystcraft.client.render.RenderUnstableBookReceptacle;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;
import thefloydman.moremystcraft.tileentity.TileEntityUnstableBookReceptacle;
import thefloydman.moremystcraft.util.Reference;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		/*RenderingRegistry.registerEntityRenderingHandler(EntityMaintainerSuit.class,
				RenderMaintainerSuitFactory.INSTANCE);*/
	}

	@Override
	public void init(FMLInitializationEvent event) {
		this.registerTileEntityRenderers();
	}

	public static void registerRenders(ModelRegistryEvent event) {
		MoreMystcraftBlocks.registerMystcraftModels();
	}
	
	public static void registerColors(ColorHandlerEvent event) {
		MoreMystcraftBlocks.registerModelColors();
	}

	private void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileEntityUnstableBookReceptacle.class,
				(TileEntitySpecialRenderer) new RenderUnstableBookReceptacle());
	}

}