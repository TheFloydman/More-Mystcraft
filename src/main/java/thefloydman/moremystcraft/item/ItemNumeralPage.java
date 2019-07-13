package thefloydman.moremystcraft.item;

import javax.annotation.Nonnull;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.util.Reference;

public class ItemNumeralPage extends Item {

	public ItemNumeralPage(@Nonnull int numeral) {
		if (numeral > 25) numeral = 25;
		if (numeral < 0) numeral = 0;
		this.setCreativeTab((CreativeTabs) MystcraftCommonProxy.tabMystBanners);
		this.setMaxStackSize(64);
		this.setRegistryName(Reference.forMoreMystcraft("numeral_page_" + String.valueOf(numeral)));
		this.setUnlocalizedName(Reference.MOD_ID + ".numeral_page" + String.valueOf(numeral));
	}

}
