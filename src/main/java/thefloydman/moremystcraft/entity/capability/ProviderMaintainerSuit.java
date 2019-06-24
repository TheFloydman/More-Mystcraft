package thefloydman.moremystcraft.entity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderMaintainerSuit implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IMaintainerSuit.class)
	public static final Capability<IMaintainerSuit> MAINTAINER_SUIT = null;

	private IMaintainerSuit instance = MAINTAINER_SUIT.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(MAINTAINER_SUIT);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(MAINTAINER_SUIT) ? MAINTAINER_SUIT.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return MAINTAINER_SUIT.getStorage().writeNBT(MAINTAINER_SUIT, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		MAINTAINER_SUIT.getStorage().readNBT(MAINTAINER_SUIT, this.instance, null, nbt);
	}

}
