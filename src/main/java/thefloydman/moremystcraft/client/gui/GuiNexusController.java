package thefloydman.moremystcraft.client.gui;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.xcompwiz.mystcraft.client.gui.GuiContainerElements;
import com.xcompwiz.mystcraft.client.gui.element.GuiElement;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementBook;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementTextField;
import com.xcompwiz.mystcraft.item.ItemAgebook;
import com.xcompwiz.mystcraft.item.ItemLinkbook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import thefloydman.moremystcraft.inventory.ContainerNexusController;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.util.Reference;

public class GuiNexusController extends GuiContainerElements {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/nexus_controller.png");
	private int windowCenterX = 0;
	private int windowCenterY = 0;
	private int guiX = 0;
	private int guiY = 0;
	private InventoryPlayer playerInv;
	private EntityPlayer player;
	private ContainerNexusController container;
	private TileEntityNexusController tileEntity;
	private int firstDisplayedBook;
	private int selectedCell;
	private int selectedBook;
	private float scrollFraction;
	private boolean scrollbarEnabled;
	private int scrollBlockX = 0;
	private int scrollBlockY = 0;
	private int scrollBlockWidth;
	private int scrollBlockHeight;
	private int scrollbarHeight;
	private boolean scrollBlockBeingDragged;
	private GuiElementTextField searchBar;
	private int[] displayedBooks;
	private GuiElementArea scrollDetectionArea;
	private GuiElementBook bookElement;

	public GuiNexusController(ContainerNexusController container, InventoryPlayer playerInv) {
		super(container);
		this.xSize = 176;
		this.ySize = 218;
		this.playerInv = playerInv;
		this.container = container;
		this.tileEntity = container.tileEntity;
		this.firstDisplayedBook = 2;
		this.selectedCell = -1;
		this.container.selectedBook = -1;
		this.scrollFraction = 0.0F;
		this.scrollBlockWidth = 12;
		this.scrollBlockHeight = 15;
		this.scrollbarHeight = 52;
		this.scrollBlockBeingDragged = false;
		this.displayedBooks = new int[] { -1, -1, -1, -1 };
	}

	@Override
	public void validate() {
		super.validate();
		TextBoxHandler handler = new TextBoxHandler();
		this.searchBar = new GuiElementTextField(handler, handler, "query", 7, 65, 162, 12);
		this.searchBar.setMaxLength(32);
		this.searchBar.setFocused(true);
		this.addElement(this.searchBar);
		this.bookElement = new GuiElementBook(this.container, new LinkHandler(), 43, 81, 90, 50);
		this.addElement(this.bookElement);
		this.scrollDetectionArea = new GuiElementArea(7, 7, 144, 54);
		this.addElement(this.scrollDetectionArea);
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
		guiX = windowCenterX - (this.xSize / 2);
		guiY = windowCenterY - (this.ySize / 2);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		drawTexturedModalRect(guiX, guiY, 0, 0, this.xSize, this.ySize);

		int index = (int) Math.rint(((this.tileEntity.getFilteredBookList().size() - 4) * this.scrollFraction));
		for (int i = 0; i < 4 && i < this.tileEntity.getFilteredBookList().size(); i++) {
			this.displayedBooks[i] = this.tileEntity.getFilteredBookList().get(index + i);
		}
		int listWidth = 142;
		int listHeight = 13;
		int listX = guiX + 8;
		int listY = guiY + 8;
		for (int i = 0; i < 4 && i < this.tileEntity.getFilteredBookList().size(); i++, listY += listHeight) {
			if (this.tileEntity.getStackInSlot(this.displayedBooks[i]).getItem() instanceof ItemLinking) {
				int textureY = this.container.selectedBook == this.displayedBooks[i] ? 231 : 218;
				float red = 1.0F;
				float green = 1.0F;
				float blue = 1.0F;
				if (this.tileEntity.getStackInSlot(this.displayedBooks[i]).getItem() instanceof ItemAgebook) {
					red = 241.0F / 255.0F;
					green = 215.0F / 255.0F;
					blue = 94.0F / 255.0F;
				} else if (this.tileEntity.getStackInSlot(this.displayedBooks[i]).getItem() instanceof ItemLinkbook) {
					red = 55.0F / 255.0F;
					green = 203.0F / 255.0F;
					blue = 79.0F / 255.0F;
				}
				GlStateManager.color(red, green, blue);
				mc.renderEngine.bindTexture(TEXTURE);
				drawTexturedModalRect(listX, listY, 0, textureY, listWidth, listHeight);
				this.fontRenderer.drawString(
						((ItemLinking) this.tileEntity.getStackInSlot(this.displayedBooks[i]).getItem())
								.getLinkInfo(this.tileEntity.getStackInSlot(this.displayedBooks[i])).getDisplayName(),
						listX + 2, listY + 3, Color.BLACK.getRGB());
			}
		}
		this.scrollbarEnabled = this.tileEntity.getFilteredBookList().size() > 4;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		this.scrollBlockX = this.guiX + 156;
		this.scrollBlockY = this.guiY + 8;
		if (this.scrollbarEnabled) {
			if (this.scrollBlockBeingDragged == true) {
				this.scrollBlockY = mouseY - 7;
				if (this.scrollBlockY < this.guiY + 8) {
					this.scrollBlockY = this.guiY + 8;
				} else if (this.scrollBlockY > this.guiY + 8 + this.scrollbarHeight - this.scrollBlockHeight) {
					this.scrollBlockY = this.guiY + 8 + this.scrollbarHeight - this.scrollBlockHeight;
				}
			} else {
				this.scrollBlockY += Math.rint((this.scrollbarHeight - this.scrollBlockHeight) * this.scrollFraction);
			}
			int textureX = 176;
			drawTexturedModalRect(this.scrollBlockX, this.scrollBlockY, textureX, 0, this.scrollBlockWidth,
					this.scrollBlockHeight);
		} else {
			int textureX = 188;
			drawTexturedModalRect(this.scrollBlockX, this.scrollBlockY, textureX, 0, this.scrollBlockWidth,
					this.scrollBlockHeight);
		}
	}

	@Override
	protected void _drawForegroundLayer(final int mouseX, final int mouseY) {
	}

	@Override
	protected void _onMouseUp(final int mouseX, final int mouseY, final int clicked_id, final boolean eaten) {
		super._onMouseUp(mouseX, mouseY, clicked_id, eaten);
		this.searchBar.setFocused(true);
		this.scrollBlockBeingDragged = false;
		this.selectedCell = this.determineClickedCell(mouseX, mouseY);
		if (selectedCell < 0) {
			return;
		}
		this.container.selectedBook = this.displayedBooks[this.selectedCell] < this.tileEntity.getSizeInventory()
				? this.displayedBooks[this.selectedCell]
				: -1;
		if (this.container.selectedBook > 1) {
			this.container.enchantItem(this.player, this.container.selectedBook);
			this.mc.playerController.sendEnchantPacket(this.container.windowId, this.container.selectedBook);
		}
		this.searchBar.setFocused(true);
	}

	public class LinkHandler implements GuiElementBook.IGuiOnLinkHandler {
		@Override
		public void onLink(final GuiElement elem) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Link", (byte) 0);
			MystcraftPacketHandler.CHANNEL
					.sendToServer(new MPacketGuiMessage(GuiNexusController.this.container.windowId, nbttagcompound));
		}
	}

	protected int determineClickedCell(int mouseX, int mouseY) {
		int listWidth = 142;
		int listHeight = 13;
		int listX = guiX + 8;
		int listY = guiY + 8;
		for (int i = 0; i < 4; i++, listY += listHeight) {
			if (mouseX >= listX && mouseX <= listX + listWidth && mouseY >= listY && mouseY <= listY + listHeight)
				return i;
		}
		return -1;
	}

	@Override
	protected void _onMouseDrag(int mouseX, int mouseY, int clicked_id, long lastclick, boolean eaten) {
		if (this.mouseOnScrollBlock(mouseX, mouseY) && this.scrollbarEnabled) {
			this.scrollBlockBeingDragged = true;
			float localY = mouseY - 8 - (this.scrollBlockHeight / 2) - this.guiY;
			this.scrollFraction = MathHelper.clamp(localY / (this.scrollbarHeight - this.scrollBlockHeight), 0.0F,
					1.0F);
		}
	}

	protected boolean mouseOnScrollBlock(int mouseX, int mouseY) {
		if (mouseX >= this.scrollBlockX && mouseX <= this.scrollBlockX + this.scrollBlockWidth
				&& mouseY >= this.scrollBlockY && mouseY <= this.scrollBlockY + this.scrollBlockHeight) {
			return true;
		}
		return false;
	}

	@Override
	protected void _keyTyped(char c, int i, boolean eaten) {
		this.scrollFraction = 0.0F;
		super._keyTyped(c, i, eaten);
	}

	public class TextBoxHandler implements GuiElementTextField.IGuiTextProvider, GuiElementTextField.IGuiOnTextChange {
		public String getText(GuiElementTextField caller) {
			return GuiNexusController.this.container.getQuery();
		}

		public void onTextChange(GuiElementTextField caller, String text) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("query", text);
			MystcraftPacketHandler.CHANNEL
					.sendToServer(new MPacketGuiMessage(GuiNexusController.this.container.windowId, nbt));
			GuiNexusController.this.container.processMessage(GuiNexusController.this.mc.player, nbt);
		}
	}

	private class GuiElementArea extends GuiElement {

		public GuiElementArea(int guiLeft, int guiTop, int xSize, int ySize) {
			super(guiLeft, guiTop, xSize, ySize);
		}

		@Override
		public void _handleMouseInput() {
			int input = Mouse.getEventDWheel();

			if (GuiNexusController.this.scrollbarEnabled && input != 0) {
				input = input > 0 ? 1 : -1;
				float outsideFraction = 1.0F
						/ ((float) GuiNexusController.this.tileEntity.getFilteredBookList().size() - 3.0F);
				float insideFraction = (outsideFraction + (2.0F * outsideFraction)) / 2.0F;
				if (GuiNexusController.this.scrollFraction == 0.0F || GuiNexusController.this.scrollFraction == 1.0F) {
					GuiNexusController.this.scrollFraction -= (float) input * insideFraction;
				} else {
					GuiNexusController.this.scrollFraction -= (float) input * outsideFraction;
				}

				if (GuiNexusController.this.scrollFraction > (outsideFraction
						* (GuiNexusController.this.tileEntity.getFilteredBookList().size() - 4))) {
					GuiNexusController.this.scrollFraction = 1.0F;
				}
				if (GuiNexusController.this.scrollFraction < outsideFraction) {
					GuiNexusController.this.scrollFraction = 0.0F;
				}
			}
		}

		@Override
		protected boolean _onKeyPress(char c, int i) {
			GuiNexusController.this.scrollFraction = 0.0F;
			return false;
		}

	}

}
