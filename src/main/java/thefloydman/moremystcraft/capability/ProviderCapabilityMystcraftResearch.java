package thefloydman.moremystcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderCapabilityMystcraftResearch implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityMystcraftResearch.class)
	public static final Capability<ICapabilityMystcraftResearch> MYSTCRAFT_RESEARCH = null;

	private ICapabilityMystcraftResearch instance = MYSTCRAFT_RESEARCH.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		if (capability == null || MYSTCRAFT_RESEARCH == null) {
			return false;
		}
		return capability.equals(MYSTCRAFT_RESEARCH);

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		if (capability == null || MYSTCRAFT_RESEARCH == null) {
			return null;
		}
		return capability.equals(MYSTCRAFT_RESEARCH) ? MYSTCRAFT_RESEARCH.<T>cast(instance) : null;

	}

	@Override
	public NBTBase serializeNBT() {

		return MYSTCRAFT_RESEARCH.getStorage().writeNBT(MYSTCRAFT_RESEARCH, instance, null);

	}

	@Override
	public void deserializeNBT(NBTBase nbt) {

		MYSTCRAFT_RESEARCH.getStorage().readNBT(MYSTCRAFT_RESEARCH, instance, null, nbt);

	}

}
