package thefloydman.moremystcraft.entity.capability;

import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StoragePlayerJourneyClothsCollectedCapability implements IStorage<IPlayerJourneyClothsCollectedCapability> {

	@Override
	public NBTBase writeNBT(Capability<IPlayerJourneyClothsCollectedCapability> capability,
			IPlayerJourneyClothsCollectedCapability instance, EnumFacing side) {
		NBTTagList nbt = new NBTTagList();
		List<UUID> list = instance.getActivatedCloths();
		for (UUID uuid : list) {
			if (uuid != null) {
				nbt.appendTag(NBTUtil.createUUIDTag(uuid));
			}
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<IPlayerJourneyClothsCollectedCapability> capability, IPlayerJourneyClothsCollectedCapability instance,
			EnumFacing side, NBTBase nbt) {
		for (NBTBase base : (NBTTagList) nbt) {
			instance.addCloth(NBTUtil.getUUIDFromTag((NBTTagCompound) base));
		}
	}

}