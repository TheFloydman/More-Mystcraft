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

		NBTTagCompound main = new NBTTagCompound();
		if (instance != null) {

			NBTTagList list = new NBTTagList();
			if (instance.getUUIDs().size() > 0) {
				for (UUID id : instance.getUUIDs()) {
					NBTTagCompound cloth = new NBTTagCompound();
					cloth.setTag("uuid", NBTUtil.createUUIDTag(id));
					cloth.setInteger("dim", instance.getClothDimension(id));
					cloth.setTag("pos", NBTUtil.createPosTag(instance.getClothPos(id)));
					list.appendTag(cloth);
				}
			}

			main.setTag("cloths", list);
			main.setInteger("timeLimit", instance.getTimeLimit());
			main.setBoolean("perPlayer", instance.getPerPlayer());
			if (instance.getLastActivatedBy() != null) {
				main.setTag("lastActivatedBy", NBTUtil.createUUIDTag(instance.getLastActivatedBy()));
			}
			main.setTag("owner", NBTUtil.createUUIDTag(instance.getOwner()));

		}
		return main;

	}

	@Override
	public void readNBT(Capability<ICapabilityHub> capability, ICapabilityHub instance, EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {

				NBTTagCompound main = (NBTTagCompound) nbt;

				NBTTagList list = main.getTagList("cloths", 10);
				instance.clearUUIDs();
				for (NBTBase tag : list) {
					NBTTagCompound cmp = (NBTTagCompound) tag;
					if (cmp.hasKey("uuid")) {
						UUID uuid = NBTUtil.getUUIDFromTag(cmp.getCompoundTag("uuid"));
						instance.addUUID(uuid);
						if (cmp.hasKey("dim")) {
							instance.setClothDimension(uuid, (cmp.getInteger("dim")));
						}
						if (cmp.hasKey("pos")) {
							instance.setClothPos(uuid, NBTUtil.getPosFromTag(cmp.getCompoundTag("pos")));
						}
					}
				}

				instance.setTimeLimit(main.getInteger("timeLimit"));
				instance.setPerPlayer(main.getBoolean("perPlayer"));
				if (((NBTTagCompound) nbt).hasKey("lastActivatedBy")) {
					instance.setLastActivatedBy(
							NBTUtil.getUUIDFromTag(((NBTTagCompound) nbt).getCompoundTag("lastActivatedBy")));
				}
				instance.setOwner(NBTUtil.getUUIDFromTag(main.getCompoundTag("owner")));
			}
		}
	}

}