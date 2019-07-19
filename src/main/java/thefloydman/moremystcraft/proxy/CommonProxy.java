package thefloydman.moremystcraft.proxy;

import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.exception.APIUndefined;
import com.xcompwiz.mystcraft.api.exception.APIVersionRemoved;
import com.xcompwiz.mystcraft.api.exception.APIVersionUndefined;
import com.xcompwiz.mystcraft.api.hook.DimensionAPI;
import com.xcompwiz.mystcraft.api.hook.PageAPI;
import com.xcompwiz.mystcraft.api.hook.SymbolAPI;
import com.xcompwiz.mystcraft.api.hook.WordAPI;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.entity.capability.CapabilityPotionDummy;
import thefloydman.moremystcraft.entity.capability.IPotionDummy;
import thefloydman.moremystcraft.entity.capability.StoragePotionDummy;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.util.handlers.MaintainerSuitEventHandler;

public class CommonProxy {

	public DimensionAPI dimensionApi;
	public PageAPI pageApi;
	public SymbolAPI symbolApi;
	public WordAPI wordApi;

	public void preInit(FMLPreInitializationEvent event) {
		MoreMystcraftPacketHandler.register();
		CapabilityManager.INSTANCE.register(IPotionDummy.class, new StoragePotionDummy(), CapabilityPotionDummy.class);
		GameRegistry.registerTileEntity(TileEntityNexusController.class,
				Reference.forMoreMystcraft("nexus_controller"));
	}

	public void init(FMLInitializationEvent event) {
		try {
			MoreMystcraft.logger.info("Initializing Mystcraft APIs");
			dimensionApi = (DimensionAPI) MystObjects.entryPoint.getProviderInstance().getAPIInstance("dimension-1");
			pageApi = (PageAPI) MystObjects.entryPoint.getProviderInstance().getAPIInstance("page-1");
			symbolApi = (SymbolAPI) MystObjects.entryPoint.getProviderInstance().getAPIInstance("symbol-1");
			wordApi = (WordAPI) MystObjects.entryPoint.getProviderInstance().getAPIInstance("word-1");
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

	public void serverStop(FMLServerStoppedEvent event) {
		MaintainerSuitEventHandler.clearPotionsList();
	}

}
