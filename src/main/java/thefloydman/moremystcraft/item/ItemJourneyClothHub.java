package thefloydman.moremystcraft.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thefloydman.moremystcraft.capability.ProviderUUIDCapability;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.Reference;

public class ItemJourneyClothHub extends ItemBlock {

	public ItemJourneyClothHub(Block block) {
		super(block);
		this.setRegistryName(Reference.forMoreMystcraft("journey_cloth_hub"));
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
			@Nullable NBTTagCompound nbt) {
		if (ProviderUUIDCapability.UUID == null) {
			return null;
		}

		return new ProviderUUIDCapability();
	}
	
}
