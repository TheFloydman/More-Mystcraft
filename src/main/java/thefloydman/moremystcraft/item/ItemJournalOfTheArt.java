package thefloydman.moremystcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import thefloydman.moremystcraft.capability.ICapabilityMystcraftResearch;
import thefloydman.moremystcraft.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.moremystcraft.research.Knowledge;
import thefloydman.moremystcraft.util.MoreMystcraftCreativeTabs;
import thefloydman.moremystcraft.util.Reference;

public class ItemJournalOfTheArt extends Item {

	public ItemJournalOfTheArt() {
		this.setCreativeTab(MoreMystcraftCreativeTabs.MORE_MYSTCRAFT);
		this.setMaxStackSize(1);
		this.setRegistryName(Reference.forMoreMystcraft("journal_of_the_art"));
		this.setUnlocalizedName(Reference.MOD_ID + ".journal_of_the_art");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			Biome biome = world.getBiome(player.getPosition());
			ICapabilityMystcraftResearch cap = player
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			cap.learnKnowledge(new Knowledge(biome));
		}
		return super.onItemRightClick(world, player, hand);
	}

}
