package thefloydman.moremystcraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.config.ModConfig;
import thefloydman.moremystcraft.init.ModBlocks;
import thefloydman.moremystcraft.util.Reference;

public class ClientProxy extends CommonProxy {

	public static void registerRenders(ModelRegistryEvent event) {
		ModBlocks.registerMystcraftModels();
	}

}