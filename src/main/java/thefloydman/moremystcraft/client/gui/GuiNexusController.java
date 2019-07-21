package thefloydman.moremystcraft.client.gui;

import java.awt.Color;
import java.io.IOException;

import com.xcompwiz.mystcraft.client.gui.GuiContainerElements;
import com.xcompwiz.mystcraft.client.gui.element.GuiElement;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementBook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thefloydman.moremystcraft.inventory.ContainerNexusController;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.util.Reference;

public class GuiNexusController extends GuiContainerElements {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/nexus_controller.png");
	int guiWidth = 176;
	int guiHeight = 218;
	int windowCenterX = 0;
	int windowCenterY = 0;
	int guiCenterX = 0;
	int guiCenterY = 0;
	private InventoryPlayer playerInv;
	private EntityPlayer player;
	private GuiElement rootelement;
	private ContainerNexusController container;
	private TileEntityNexusController tileEntity;
	int firstDisplayedBook;
	int selectedCell;
	int selectedBook;

	public GuiNexusController(ContainerNexusController container, InventoryPlayer playerInv) {
		super(container);
		this.playerInv = playerInv;
		this.container = container;
		this.tileEntity = container.tileEntity;
		this.firstDisplayedBook = 2;
		this.selectedCell = -1;
		this.selectedBook = -1;
	}

	@Override
	public void validate() {
		this.addElement(new GuiElementBook(this.container, new LinkHandler(), 43, 55, 90, 50));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}

	@Override
	protected void _drawBackgroundLayer(final int mouseX, final int mouseY, final float f) {
		windowCenterX = this.width / 2;
		windowCenterY = this.height / 2;
		guiCenterX = windowCenterX - (guiWidth / 2);
		guiCenterY = windowCenterY - (guiHeight / 2);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		drawTexturedModalRect(guiCenterX, guiCenterY, 0, 0, guiWidth, guiHeight);

		int listWidth = 140;
		int listHeight = 13;
		int listX = guiCenterX + 8;
		int listY = guiCenterY + 24;
		for (int i = this.firstDisplayedBook; i < this.firstDisplayedBook + 4
				&& i < this.tileEntity.inventorySize; i++, listY += listHeight) {
			if (this.tileEntity.getStackInSlot(i).getItem() instanceof ItemLinking) {
				int textureY = this.selectedBook == i ? 231 : 218;
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				mc.renderEngine.bindTexture(TEXTURE);
				drawTexturedModalRect(listX, listY, 0, textureY, listWidth, listHeight);
				fontRenderer.drawString(
						((ItemLinking) this.tileEntity.getStackInSlot(i).getItem())
								.getLinkInfo(this.tileEntity.getStackInSlot(i)).getDisplayName(),
						listX + 2, listY + 3, Color.BLACK.getRGB());
			}
		}

		int searchStringX = guiCenterX + 9;
		int searchStringY = guiCenterY + 9;

		fontRenderer.drawString("Search (nonfunctioning)", searchStringX, searchStringY, 0xFFFFFF);
	}

	@Override
	protected void _drawForegroundLayer(final int mouseX, final int mouseY) {
	}

	@Override
	protected void _onMouseUp(final int mouseX, final int mouseY, final int clicked_id, final boolean eaten) {
		super._onMouseUp(mouseX, mouseY, clicked_id, eaten);
		this.selectedCell = this.determineClickedCell(mouseX, mouseY);
		this.selectedBook = this.selectedCell + this.firstDisplayedBook;
		if (this.selectedBook > 1) {
			this.container.enchantItem(this.player, this.selectedBook);
			this.mc.playerController.sendEnchantPacket(this.container.windowId, this.selectedBook);
		}
	}

	protected boolean isCursorOnBookDisplay(final int mouseX, final int mouseY) {
		int bookLeft = guiCenterX + 43;
		int bookTop = guiCenterY + 81;
		int bookRight = guiCenterX + 132;
		int bookBottom = guiCenterY + 130;
		if (mouseX >= bookLeft && mouseX <= bookRight && mouseY >= bookTop && mouseY <= bookBottom) {
			return true;
		}
		return false;
	}

	protected boolean isCursorOnBookList(final int mouseX, final int mouseY) {
		int bookLeft = guiCenterX + 7;
		int bookTop = guiCenterY + 23;
		int bookRight = guiCenterX + 148;
		int bookBottom = guiCenterY + 76;
		if (mouseX >= bookLeft && mouseX <= bookRight && mouseY >= bookTop && mouseY <= bookBottom) {
			return true;
		}
		return false;
	}

	public class LinkHandler implements GuiElementBook.IGuiOnLinkHandler {
		@Override
		public void onLink(final GuiElement elem) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Link", (byte) 0);
			MystcraftPacketHandler.CHANNEL.sendToServer(
					(IMessage) new MPacketGuiMessage(GuiNexusController.this.container.windowId, nbttagcompound));
		}
	}

	protected int determineClickedCell(int mouseX, int mouseY) {
		int listWidth = 140;
		int listHeight = 13;
		int listX = guiCenterX + 8;
		int listY = guiCenterY + 24;
		for (int i = 0; i < 4; i++, listY += listHeight) {
			if (mouseX >= listX && mouseX <= listX + listWidth && mouseY >= listY && mouseY <= listY + listHeight)
				return i;
		}
		return -1;
	}
}
