package thefloydman.moremystcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderCapabilityPreviousGameMode implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityPreviousGameMode.class)
	public static final Capability<ICapabilityPreviousGameMode> PREVIOUS_GAMEMODE = null;
	
	private ICapabilityPreviousGameMode instance = PREVIOUS_GAMEMODE.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(PREVIOUS_GAMEMODE);

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(PREVIOUS_GAMEMODE) ? PREVIOUS_GAMEMODE.<T>cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return PREVIOUS_GAMEMODE.getStorage().writeNBT(PREVIOUS_GAMEMODE, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		PREVIOUS_GAMEMODE.getStorage().readNBT(PREVIOUS_GAMEMODE, instance, null, nbt);
	}

}
