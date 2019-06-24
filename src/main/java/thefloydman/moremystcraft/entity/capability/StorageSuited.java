package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageSuited implements IStorage<ISuited> {

	@Override
	public NBTBase writeNBT(Capability<ISuited> capability, ISuited instance, EnumFacing side) {
		return new NBTTagByte((byte) (instance.isSuited() ? 1 : 0));
	}

	@Override
	public void readNBT(Capability<ISuited> capability, ISuited instance, EnumFacing side, NBTBase nbt) {
		instance.setSuited(((NBTTagByte) nbt).getByte() == 0 ? false : true);
	}

}
