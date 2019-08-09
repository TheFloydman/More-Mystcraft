package thefloydman.moremystcraft.capability;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCapabilityHub implements IStorage<ICapabilityHub> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityHub> capability, ICapabilityHub instance, EnumFacing side) {
		NBTTagList list = new NBTTagList();
		if (instance != null) {
			if (instance.getUUIDs().size() > 0) {
				for (UUID id : instance.getUUIDs()) {
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setTag("uuid", NBTUtil.createUUIDTag(id));
					list.appendTag(nbt);
				}
				return list;
			}
		}
		return list;
	}

	@Override
	public void readNBT(Capability<ICapabilityHub> capability, ICapabilityHub instance, EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				for (NBTBase tag : (NBTTagList) nbt) {
					if (((NBTTagCompound) tag).hasKey("uuid")) {
						instance.addUUID(NBTUtil.getUUIDFromTag(((NBTTagCompound) tag).getCompoundTag("uuid")));
					}
				}
			}
		}
	}

}