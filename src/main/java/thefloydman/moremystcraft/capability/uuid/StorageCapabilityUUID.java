package thefloydman.moremystcraft.capability.uuid;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCapabilityUUID implements IStorage<ICapabilityUUID> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityUUID> capability, ICapabilityUUID instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		if (instance != null) {
			if (instance.getUUID() != null) {
				nbt.setTag("uuid", NBTUtil.createUUIDTag(instance.getUUID()));
				return nbt;
			}
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICapabilityUUID> capability, ICapabilityUUID instance, EnumFacing side,
			NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				if (((NBTTagCompound) nbt).hasKey("uuid")) {
					instance.setUUID(NBTUtil.getUUIDFromTag(((NBTTagCompound) nbt).getCompoundTag("uuid")));
				}
			}
		}
	}

}