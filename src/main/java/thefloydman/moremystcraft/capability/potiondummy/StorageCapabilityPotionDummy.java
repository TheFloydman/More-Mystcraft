package thefloydman.moremystcraft.capability.potiondummy;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCapabilityPotionDummy implements IStorage<ICapabilityPotionDummy> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityPotionDummy> capability, ICapabilityPotionDummy instance, EnumFacing side) {
		return new NBTTagString(instance.getParent().toString());
	}

	@Override
	public void readNBT(Capability<ICapabilityPotionDummy> capability, ICapabilityPotionDummy instance, EnumFacing side,
			NBTBase nbt) {
		instance.setParent(UUID.fromString(((NBTTagString) nbt).getString()));
	}

}
