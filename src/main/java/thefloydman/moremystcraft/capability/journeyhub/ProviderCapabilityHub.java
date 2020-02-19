package thefloydman.moremystcraft.capability.journeyhub;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderCapabilityHub implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityHub.class)
	public static final Capability<ICapabilityHub> HUB = null;

	private ICapabilityHub instance = HUB.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(HUB);

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(HUB) ? HUB.<T>cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return HUB.getStorage().writeNBT(HUB, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		HUB.getStorage().readNBT(HUB, instance, null, nbt);
	}

}
