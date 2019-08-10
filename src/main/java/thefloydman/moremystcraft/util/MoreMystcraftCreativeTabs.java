package thefloydman.moremystcraft.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.init.MoreMystcraftBlocks;

public class MoreMystcraftCreativeTabs {

	public static final CreativeTabs MORE_MYSTCRAFT = new CreativeTabs(Reference.MOD_ID) {

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(MoreMystcraftBlocks.JOURNEY_CLOTH_HAND_ITEM);
		}
	};

}
