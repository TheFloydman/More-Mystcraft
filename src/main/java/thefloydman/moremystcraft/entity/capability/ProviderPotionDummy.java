package thefloydman.moremystcraft.entity.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderPotionDummy implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IPotionDummy.class)
	public static final Capability<IPotionDummy> POTION_DUMMY = null;

	private IPotionDummy instance = POTION_DUMMY.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(POTION_DUMMY);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(POTION_DUMMY) ? POTION_DUMMY.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return POTION_DUMMY.getStorage().writeNBT(POTION_DUMMY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		POTION_DUMMY.getStorage().readNBT(POTION_DUMMY, this.instance, null, nbt);
	}

}
