package thefloydman.moremystcraft.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thefloydman.moremystcraft.capability.uuid.ProviderCapabilityUUID;
import thefloydman.moremystcraft.util.JourneyUtils;
import thefloydman.moremystcraft.util.Reference;

public class ItemJourneyCloth extends ItemBlock {

	public ItemJourneyCloth(Block block, JourneyUtils.PatternType type) {
		super(block);
		this.setRegistryName(Reference.forMoreMystcraft("journey_cloth_" + type.name().toLowerCase()));
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
			@Nullable NBTTagCompound nbt) {
		if (ProviderCapabilityUUID.UUID == null) {
			return null;
		}

		return new ProviderCapabilityUUID();
	}
	
}
