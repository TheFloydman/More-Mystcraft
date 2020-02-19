package thefloydman.moremystcraft.capability.adventurepanel;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.GameType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCapabilityAdventurePanel implements IStorage<ICapabilityAdventurePanel> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityAdventurePanel> capability, ICapabilityAdventurePanel instance,
			EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		if (instance != null) {
			if (instance.getPreviousGameMode() != null) {
				nbt.setInteger("previous_gamemode", instance.getPreviousGameMode().getID());
				nbt.setBoolean("linked_to_adventure", instance.getLinkedToAdventure());
				nbt.setInteger("death_dimension", instance.getDeathDimension());
			}
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICapabilityAdventurePanel> capability, ICapabilityAdventurePanel instance,
			EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				if (((NBTTagCompound) nbt).hasKey("previous_gamemode")) {
					instance.setPreviousGameMode(
							GameType.getByID(((NBTTagCompound) nbt).getInteger("uuprevious_gamemodeid")));
				} else if (((NBTTagCompound) nbt).hasKey("linked_to_adventure")) {
					instance.setLinkedToAdventure(((NBTTagCompound) nbt).getBoolean("linked_to_adventure"));
				} else if (((NBTTagCompound) nbt).hasKey("death_dimension")) {
					instance.setDeathDimension(((NBTTagCompound) nbt).getInteger("death_dimension"));
				}
			}
		}
	}

}