package thefloydman.moremystcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderCapabilityAdventurePanel implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityAdventurePanel.class)
	public static final Capability<ICapabilityAdventurePanel> ADVENTURE_PANEL = null;
	
	private ICapabilityAdventurePanel instance = ADVENTURE_PANEL.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability.equals(ADVENTURE_PANEL);

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability.equals(ADVENTURE_PANEL) ? ADVENTURE_PANEL.<T>cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return ADVENTURE_PANEL.getStorage().writeNBT(ADVENTURE_PANEL, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		ADVENTURE_PANEL.getStorage().readNBT(ADVENTURE_PANEL, instance, null, nbt);
	}

}
