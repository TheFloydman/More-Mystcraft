package thefloydman.moremystcraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
import thefloydman.moremystcraft.config.ModConfig;
import thefloydman.moremystcraft.init.ModBlocks;
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

		// Remap and blacklist original sized Biome Distribution Pages.
		if (ModConfig.originalBioConsEnabled == false) {
			SymbolRemappings.addSymbolRemapping(forMystcraft("biocontiny"), forMoreMystcraft("size_tiny"),
					forMoreMystcraft("biocon_natural"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconsmall"), forMoreMystcraft("size_small"),
					forMoreMystcraft("biocon_natural"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconmedium"), forMoreMystcraft("size_medium"),
					forMoreMystcraft("biocon_natural"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconlarge"), forMoreMystcraft("size_large"),
					forMoreMystcraft("biocon_natural"));
			SymbolRemappings.addSymbolRemapping(forMystcraft("bioconhuge"), forMoreMystcraft("size_huge"),
					forMoreMystcraft("biocon_natural"));
			
			SymbolManager.blackListSymbol(forMystcraft("biocontiny"));
			SymbolManager.blackListSymbol(forMystcraft("bioconsmall"));
			SymbolManager.blackListSymbol(forMystcraft("bioconmedium"));
			SymbolManager.blackListSymbol(forMystcraft("bioconlarge"));
			SymbolManager.blackListSymbol(forMystcraft("bioconhuge"));
		}
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event) {
		ClientProxy.registerRenders(event);
	}

	private ResourceLocation forMystcraft(String name) {
		return new ResourceLocation("mystcraft", name);
	}

	private ResourceLocation forMoreMystcraft(String name) {
		return new ResourceLocation("moremystcraft", name);
	}
}
