package thefloydman.moremystcraft.util.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.api.event.DenseOresEvent;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;
import com.xcompwiz.mystcraft.world.agedata.AgeData;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import thefloydman.moremystcraft.data.MoreMystcraftWorldSavedData;
import thefloydman.moremystcraft.util.Reference;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadHandler {

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (world.isRemote) {
			return;
		}
		int dimId = world.provider.getDimension();
		try {
			if (dimId < 0) {
				return;
			} else if (world.provider.getDimensionType().equals(Mystcraft.dimensionType)) {
				AgeData data = new AgeData("currentDim").getAge(dimId, false);
				List symbolList = data.getSymbols(false);
				ResourceLocation ores = Reference.forMoreMystcraft("lacking_ores");
				if (symbolList.contains(ores)) {
					ResourceLocation denseOres = new ResourceLocation("mystcraft", "DenseOres");
					if (symbolList.contains(denseOres)) {
						MoreMystcraftWorldSavedData savedData = MoreMystcraftWorldSavedData.get(world);
						boolean instabilityAdded = savedData.getAdded();
						if (instabilityAdded == false) {
							((WorldProviderMyst) (world.provider)).getAgeController().addInstability(1500);
							savedData.setAdded(true);
						}
					}
				}
			}
		} catch (NullPointerException e) {
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Dimension ID: " + Integer.toString(dimId));
		}
	}

}
