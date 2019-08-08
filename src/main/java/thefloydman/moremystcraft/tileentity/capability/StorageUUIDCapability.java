package thefloydman.moremystcraft.tileentity.capability;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageUUIDCapability implements IStorage<IUUIDCapability> {

	@Override
	public NBTBase writeNBT(Capability<IUUIDCapability> capability, IUUIDCapability instance, EnumFacing side) {
		if (instance != null) {
			if (instance.getUUID() != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setUniqueId("uuid", instance.getUUID());
				return nbt;
			}
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setUniqueId("uuid", UUID.randomUUID());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IUUIDCapability> capability, IUUIDCapability instance, EnumFacing side,
			NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				if (((NBTTagCompound) nbt).getUniqueId("uuid") != null) {
					instance.setUUID(((NBTTagCompound) nbt).getUniqueId("uuid"));
				}
			}
			instance.setUUID(UUID.randomUUID());
		}
	}

}