package thefloydman.moremystcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderCapabilityHub implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityHub.class)
	public static final Capability<ICapabilityHub> UUID_LIST = null;

	private ICapabilityHub instance = UUID_LIST.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(UUID_LIST);

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(UUID_LIST) ? UUID_LIST.<T>cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return UUID_LIST.getStorage().writeNBT(UUID_LIST, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		UUID_LIST.getStorage().readNBT(UUID_LIST, instance, null, nbt);
	}

}
