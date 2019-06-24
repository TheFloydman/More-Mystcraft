package thefloydman.moremystcraft.entity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderSuited implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ISuited.class)
	public static final Capability<ISuited> SUITED = null;

	private ISuited instance = SUITED.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(SUITED);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(SUITED) ? SUITED.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return SUITED.getStorage().writeNBT(SUITED, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		SUITED.getStorage().readNBT(SUITED, this.instance, null, nbt);
	}

}
