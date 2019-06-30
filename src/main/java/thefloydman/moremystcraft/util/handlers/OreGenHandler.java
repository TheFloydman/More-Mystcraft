package thefloydman.moremystcraft.util.handlers;

import java.util.List;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.world.agedata.AgeData;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefloydman.moremystcraft.util.Reference;

public class OreGenHandler {

	@SubscribeEvent
	public void oreGen(OreGenEvent.GenerateMinable event) {
		World world = event.getWorld();
		int dimId = world.provider.getDimension();
		try {
			if (dimId < 0) {
				return;
			} else if (world.provider.getDimensionType().equals(Mystcraft.dimensionType)) {
				AgeData data = new AgeData("currentDim").getAge(dimId, false);
				List symbolList = data.getSymbols(false);
				ResourceLocation lackingOres = Reference.forMoreMystcraft("lacking_ores");
				if (symbolList.contains(lackingOres)) {
					ResourceLocation denseOres = new ResourceLocation("mystcraft", "DenseOres");
					EventType type = event.getType();
					if (!type.equals(EventType.DIORITE) && !type.equals(EventType.GRANITE)
							&& !type.equals(EventType.ANDESITE) && !type.equals(EventType.DIRT)) {
						event.setResult(Result.DENY);
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
