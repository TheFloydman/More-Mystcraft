package thefloydman.moremystcraft.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.block.BlockJourneyCloth;
import thefloydman.moremystcraft.capability.ProviderCapabilityHub;
import thefloydman.moremystcraft.util.JourneyClothUtils;
import thefloydman.moremystcraft.util.Reference;

public class ItemJourneyHub extends ItemBlock {

	public ItemJourneyHub(Block block, JourneyClothUtils.Type type) {
		super(block);
		this.setRegistryName(Reference.forMoreMystcraft("journey_hub_" + type.name().toLowerCase()));
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
			@Nullable NBTTagCompound nbt) {
		if (ProviderCapabilityHub.HUB == null) {
			return null;
		}

		return new ProviderCapabilityHub();
	}

}
