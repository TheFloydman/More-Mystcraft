package thefloydman.moremystcraft.item;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.util.MoreMystcraftCreativeTabs;
import thefloydman.moremystcraft.util.Reference;

public class ItemMaintainerSuit extends Item {

	public ItemMaintainerSuit() {
		this.setCreativeTab(MoreMystcraftCreativeTabs.MORE_MYSTCRAFT);
		this.setMaxStackSize(1);
		this.setRegistryName(Reference.forMoreMystcraft("maintainer_suit"));
		this.setUnlocalizedName(Reference.MOD_ID + ".maintainer_suit");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack handStack = player.getHeldItem(hand);

		if (world.isRemote || handStack.getCount() > 1) {
			BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
			if (pos != null) {
				if (world.getBlockState(pos).getMaterial().isSolid()) {
					MoreMystcraftPacketHandler.spawnMaintainerSuit(pos, player.rotationYaw);
				}
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, handStack);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, ItemStack.EMPTY);
	}

}
