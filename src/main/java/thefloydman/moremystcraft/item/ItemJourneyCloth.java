package thefloydman.moremystcraft.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thefloydman.moremystcraft.capability.ProviderCapabilityUUID;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.Reference;

public class ItemJourneyCloth extends ItemBlock {

	public ItemJourneyCloth(Block block, JourneyClothUtils.Type type) {
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
