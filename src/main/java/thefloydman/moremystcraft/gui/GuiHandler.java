package thefloydman.moremystcraft.gui;

import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import thefloydman.moremystcraft.client.gui.GuiBookLocked;
import thefloydman.moremystcraft.client.gui.GuiJourneyHub;
import thefloydman.moremystcraft.client.gui.GuiNexusController;
import thefloydman.moremystcraft.inventory.ContainerBookLocked;
import thefloydman.moremystcraft.inventory.ContainerJourneyHub;
import thefloydman.moremystcraft.inventory.ContainerNexusController;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (id == MoreMystcraftGUIs.BOOK_DISPLAY_LOCKED.ordinal()) {
			final TileEntityBookRotateable tileEntity = (TileEntityBookRotateable) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerBookLocked(player.inventory, tileEntity);
		} else if (id == MoreMystcraftGUIs.NEXUS_CONTROLLER.ordinal()) {
			final TileEntityNexusController tileEntity = (TileEntityNexusController) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerNexusController(player.inventory, tileEntity);
		} else if (id == MoreMystcraftGUIs.JOURNEY_HUB.ordinal()) {
			final TileEntitySingleItem tileEntity = (TileEntitySingleItem) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerJourneyHub(tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (id == MoreMystcraftGUIs.BOOK_DISPLAY_LOCKED.ordinal()) {
			final TileEntityBookRotateable tileEntity = (TileEntityBookRotateable) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new GuiBookLocked(player.inventory, tileEntity);
		} else if (id == MoreMystcraftGUIs.NEXUS_CONTROLLER.ordinal()) {
			return new GuiNexusController((ContainerNexusController) getServerGuiElement(id, player, world, x, y, z),
					player.inventory);
		} else if (id == MoreMystcraftGUIs.JOURNEY_HUB.ordinal()) {
			return new GuiJourneyHub((ContainerJourneyHub) getServerGuiElement(id, player, world, x, y, z));
		}
		return null;
	}
}