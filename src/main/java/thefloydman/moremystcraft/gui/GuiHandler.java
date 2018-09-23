package thefloydman.moremystcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;

import thefloydman.moremystcraft.client.gui.GuiBookLocked;
import thefloydman.moremystcraft.inventory.ContainerBookLocked;

public class GuiHandler implements IGuiHandler {
	public Object getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (id == ModGUIs.BOOK_DISPLAY_LOCKED.ordinal()) {
			final TileEntityBookRotateable tileentity2 = (TileEntityBookRotateable) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerBookLocked(player.inventory, tileentity2);
		}
		return null;
	}

	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (id == ModGUIs.BOOK_DISPLAY_LOCKED.ordinal()) {
			final TileEntityBookRotateable tileentity2 = (TileEntityBookRotateable) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new GuiBookLocked(player.inventory, tileentity2);
		}
		return null;
	}
}