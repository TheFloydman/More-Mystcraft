/*
 * Much of this code is copied directly from Mystcraft.
 * Do not copy it without explicit permission from XCompWiz.
 * All Rights Reserved unless otherwise explicitly stated.
 */

package thefloydman.moremystcraft.client.gui;

import com.xcompwiz.mystcraft.client.gui.GuiBook;
import com.xcompwiz.mystcraft.client.gui.GuiContainerElements;
import com.xcompwiz.mystcraft.client.gui.element.GuiElement;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementBook;
import com.xcompwiz.mystcraft.data.Assets;
import com.xcompwiz.mystcraft.inventory.ContainerBook;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;
import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thefloydman.moremystcraft.inventory.ContainerBookLocked;

public class GuiBookLocked extends GuiContainerElements implements GuiElementBook.IGuiOnLinkHandler {

	private ContainerBookLocked container;
	private boolean widget;
	private GuiElementBook bookelem;

	private GuiBookLocked(final ContainerBookLocked container) {
		super(container);
		this.widget = true;
		this.container = container;
	}

	public GuiBookLocked(final InventoryPlayer inventoryplayer, final TileEntityBookRotateable tileentity) {
		this(new ContainerBookLocked(inventoryplayer, tileentity));
	}

	@Override
	public void onLink(final GuiElement elem) {
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setByte("Link", (byte) 0);
		MystcraftPacketHandler.CHANNEL
				.sendToServer((IMessage) new MPacketGuiMessage(this.container.windowId, nbttagcompound));
	}

	@Override
	public void validate() {
		this.xSize = 327;
		this.ySize = 199;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.addElement(this.bookelem = new GuiElementBook(this.container, this, 0, 0, this.xSize, this.ySize));
		this.recalcPosition();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.container.updateSlots();
		this.recalcPosition();
	}

	private void recalcPosition() {
		if (this.bookelem.isVisible() == this.widget) {
			return;
		}
		this.widget = this.bookelem.isVisible();
		this.xSize = 176;
		this.ySize = 166;
		if (this.widget) {
			this.xSize = this.bookelem.getWidth();
			this.ySize = this.bookelem.getHeight();
		}
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.bookelem.getParent().setLeft(this.guiLeft);
		this.bookelem.getParent().setTop(this.guiTop);
		this.bookelem.getParent().setWidth(this.xSize);
		this.bookelem.getParent().setHeight(this.ySize);
	}

	@Override
	protected void _drawBackgroundLayer(final int mouseX, final int mouseY, final float f) {
		if (!this.widget) {
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			this.mc.getTextureManager().bindTexture(Assets.GUIs.slot);
			this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
			if (!this.isSlotVisible()) {
				this.drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 34, 10, 10, 18, 18);
			}
		}
	}

	private boolean isSlotVisible() {
		return false;
	}

}
