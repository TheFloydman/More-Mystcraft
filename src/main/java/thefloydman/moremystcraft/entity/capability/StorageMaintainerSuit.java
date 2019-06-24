package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageMaintainerSuit implements IStorage<IMaintainerSuit> {

	@Override
	public NBTBase writeNBT(Capability<IMaintainerSuit> capability, IMaintainerSuit instance, EnumFacing side) {
		return new NBTTagString(instance.getSuit().toString());
	}

	@Override
	public void readNBT(Capability<IMaintainerSuit> capability, IMaintainerSuit instance, EnumFacing side,
			NBTBase nbt) {
		instance.setSuit(UUID.fromString(((NBTTagString) nbt).getString()));
	}

}
