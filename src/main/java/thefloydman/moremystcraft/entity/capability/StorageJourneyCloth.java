package thefloydman.moremystcraft.entity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageJourneyCloth implements IStorage<IJourneyClothCapability> {

	@Override
	public NBTBase writeNBT(Capability<IJourneyClothCapability> capability, IJourneyClothCapability instance,
			EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setUniqueId("uuid", instance.getUUID());
		nbt.setInteger("dim", instance.getCurrentDimension());
		nbt.setTag("pos", NBTUtil.createPosTag(instance.getCurrentPos()));
		return nbt;
	}

	@Override
	public void readNBT(Capability<IJourneyClothCapability> capability, IJourneyClothCapability instance,
			EnumFacing side, NBTBase nbt) {
		instance.setUUID(((NBTTagCompound) nbt).getUniqueId("uuid"));
		instance.setCurrentDimension(((NBTTagCompound) nbt).getInteger("dim"));
		instance.setCurrentPos(NBTUtil.getPosFromTag(((NBTTagCompound) nbt).getCompoundTag("pos")));
	}

}