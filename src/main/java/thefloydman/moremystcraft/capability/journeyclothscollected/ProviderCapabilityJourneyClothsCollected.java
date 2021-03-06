package thefloydman.moremystcraft.capability.journeyclothscollected;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderCapabilityJourneyClothsCollected implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityJourneyClothsCollected.class)
	public static final Capability<CapabilityJourneyClothsCollected> JOURNEY_CLOTH = null;

	private CapabilityJourneyClothsCollected instance = JOURNEY_CLOTH.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == null || JOURNEY_CLOTH == null) {
			return false;
		}
		return capability.equals(JOURNEY_CLOTH);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == null || JOURNEY_CLOTH == null) {
			return null;
		}
		return capability.equals(JOURNEY_CLOTH) ? JOURNEY_CLOTH.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return JOURNEY_CLOTH.getStorage().writeNBT(JOURNEY_CLOTH, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		JOURNEY_CLOTH.getStorage().readNBT(JOURNEY_CLOTH, this.instance, null, nbt);
	}

}
