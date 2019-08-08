package thefloydman.moremystcraft.tileentity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderUUIDCapability implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IUUIDCapability.class)
	public static final Capability<CapabilityUUID> UUID = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(UUID);

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(UUID) ? UUID.<T>cast(UUID.getDefaultInstance()) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return UUID.getStorage().writeNBT(UUID, UUID.getDefaultInstance(), null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		UUID.getStorage().readNBT(UUID, UUID.getDefaultInstance(), null, nbt);
	}

}