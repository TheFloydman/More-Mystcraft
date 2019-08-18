package thefloydman.moremystcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.GameType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCapabilityPreviousGameMode implements IStorage<ICapabilityPreviousGameMode> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityPreviousGameMode> capability, ICapabilityPreviousGameMode instance,
			EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		if (instance != null) {
			if (instance.getGameMode() != null) {
				nbt.setInteger("previous_gamemode", instance.getGameMode().getID());
				return nbt;
			}
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICapabilityPreviousGameMode> capability, ICapabilityPreviousGameMode instance,
			EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				if (((NBTTagCompound) nbt).hasKey("previous_gamemode")) {
					instance.setGameMode(GameType.getByID(((NBTTagCompound) nbt).getInteger("uuprevious_gamemodeid")));
				}
			}
		}
	}

}