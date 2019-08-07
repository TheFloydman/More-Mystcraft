package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StoragePotionDummy implements IStorage<IPotionDummyCapability> {

	@Override
	public NBTBase writeNBT(Capability<IPotionDummyCapability> capability, IPotionDummyCapability instance, EnumFacing side) {
		return new NBTTagString(instance.getParent().toString());
	}

	@Override
	public void readNBT(Capability<IPotionDummyCapability> capability, IPotionDummyCapability instance, EnumFacing side,
			NBTBase nbt) {
		instance.setParent(UUID.fromString(((NBTTagString) nbt).getString()));
	}

}
