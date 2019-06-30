package thefloydman.moremystcraft.util.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.api.event.DenseOresEvent;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;
import com.xcompwiz.mystcraft.world.agedata.AgeData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import thefloydman.moremystcraft.data.MoreMystcraftSavedDataPerDimension;
import thefloydman.moremystcraft.entity.EntityPotionDummy;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.util.Reference;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadHandler {

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		int dim = world.provider.getDimension();
		if (world.isRemote) {
			PotionListHandler.clearPotionsList();
			return;
		}
		try {
			if (dim < 2) {
				return;
			} else if (world.provider instanceof WorldProviderMyst) {
				MaintainerSuitEventHandler.clearPotionsList(world.provider.getDimension());

				AgeData data = new AgeData("currentDim").getAge(dim, false);
				List symbolList = data.getSymbols(false);
				ResourceLocation ores = Reference.forMoreMystcraft("lacking_ores");
				MoreMystcraftSavedDataPerDimension savedData = MoreMystcraftSavedDataPerDimension.get(world);

				if (symbolList.contains(ores)) {
					ResourceLocation denseOres = new ResourceLocation("mystcraft", "DenseOres");
					if (symbolList.contains(denseOres)) {
						boolean instabilityAdded = savedData.getConflictingOreInstabilityAdded();
						if (instabilityAdded == false) {
							((WorldProviderMyst) (world.provider)).getAgeController().addInstability(1500);
							savedData.setConflictingOreInstabilityAdded(true);
						}
					}
				}

				/*
				 * NBTTagList potionList = savedData.getPotionEffects(); List<String> list = new
				 * ArrayList<String>(); for (int i = 0; i < potionList.tagCount(); i++) {
				 * list.add(potionList.getStringTagAt(i)); }
				 * MaintainerSuitEventHandler.setPotionsList(dimId, list);
				 */

			}
		} catch (NullPointerException e) {
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Dimension ID: " + Integer.toString(dim));
		}
	}

}
