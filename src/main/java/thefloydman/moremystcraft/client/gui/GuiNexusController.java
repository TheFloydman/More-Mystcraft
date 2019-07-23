package thefloydman.moremystcraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import thefloydman.moremystcraft.inventory.ContainerNexusController;
import thefloydman.moremystcraft.tileentity.TileEntityNexusController;
import thefloydman.moremystcraft.util.Reference;

public class GuiNexusController extends GuiContainerElements {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/nexus_controller.png");
	private int guiWidth = 176;
	private int guiHeight = 218;
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
	private List<Integer> filteredList;
	private List<Integer> displayedBooks;

	public GuiNexusController(ContainerNexusController container, InventoryPlayer playerInv) {
		super(container);
		this.playerInv = playerInv;
		this.container = container;
		this.tileEntity = container.tileEntity;
		this.firstDisplayedBook = 2;
		for (int i = 2; i < 6; i++) {
			this.displayedBooks.add(i);
		}
		this.selectedCell = -1;
		this.container.selectedBook = -1;
		this.scrollFraction = 0.0F;
		this.scrollBlockWidth = 12;
		this.scrollBlockHeight = 15;
		this.scrollbarHeight = 52;
		this.scrollBlockBeingDragged = false;
		this.filteredList = new ArrayList<Integer>();
		for (int i = 0; i < this.tileEntity.getShortList().size(); i++) {
			this.filteredList.add(i + 2);
		}
	}

	@Override
	public void validate() {
		TextBoxHandler handler = new TextBoxHandler();
		this.searchBar = new GuiElementTextField(handler, handler, "query", 7, 39, 162, 12);
		this.searchBar.setMaxLength(32);
		this.searchBar.setFocused(true);
		this.addElement(this.searchBar);
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
		guiX = windowCenterX - (guiWidth / 2);
		guiY = windowCenterY - (guiHeight / 2);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		drawTexturedModalRect(guiX, guiY, 0, 0, guiWidth, guiHeight);

		int index = (int) Math.rint(((this.tileEntity.getBookCount() - 4) * this.scrollFraction));
		displayedBooks = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			displayedBooks.add(this.filteredList.get(index + i));
		}
		this.firstDisplayedBook = (int) Math.rint(((this.tileEntity.getBookCount() - 4) * this.scrollFraction) + 2);
		int listWidth = 142;
		int listHeight = 13;
		int listX = guiX + 8;
		int listY = guiY + 8;
		for (int i = 0; i < 4 && i < this.filteredList.size(); i++, listY += listHeight) {
			if (this.tileEntity.getStackInSlot(this.filteredList.get(i)).getItem() instanceof ItemLinking) {
				int textureY = this.container.selectedBook == i ? 231 : 218;
				float red = 1.0F;
				float green = 1.0F;
				float blue = 1.0F;
				if (this.tileEntity.getStackInSlot(this.filteredList.get(i)).getItem() instanceof ItemAgebook) {
					red = 241.0F / 255.0F;
					green = 215.0F / 255.0F;
					blue = 94.0F / 255.0F;
				} else if (this.tileEntity.getStackInSlot(this.filteredList.get(i)).getItem() instanceof ItemLinkbook) {
					red = 55.0F / 255.0F;
					green = 203.0F / 255.0F;
					blue = 79.0F / 255.0F;
				}
				GlStateManager.color(red, green, blue);
				mc.renderEngine.bindTexture(TEXTURE);
				drawTexturedModalRect(listX, listY, 0, textureY, listWidth, listHeight);
				this.fontRenderer.drawString(
						((ItemLinking) this.tileEntity.getStackInSlot(i).getItem())
								.getLinkInfo(this.tileEntity.getStackInSlot(i)).getDisplayName(),
						listX + 2, listY + 3, Color.BLACK.getRGB());
			}
		}
		this.scrollbarEnabled = this.tileEntity.getBookCount() > 4;
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
		this.scrollBlockBeingDragged = false;
		this.selectedCell = this.determineClickedCell(mouseX, mouseY);
		if (selectedCell < 0) {
			return;
		}
		if (selectedCell < this.filteredList.size()) {
			this.container.selectedBook = this.filteredList.get(this.selectedCell) < this.tileEntity.getSizeInventory()
					? this.filteredList.get(this.selectedCell)
					: -1;
		} else {
			this.container.selectedBook = -1;
		}
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

	protected void filterBooks(String text) {
		this.filteredList = new ArrayList<Integer>();
		for (int i = 0; i < this.tileEntity.getShortList().size(); i++) {
			this.filteredList.add(i + 2);
		}
		List<ItemStack> shortList = this.tileEntity.getShortList();
		List<String> displayNames = new ArrayList<String>();
		for (ItemStack stack : shortList) {
			displayNames.add(((ItemLinking) stack.getItem()).getLinkInfo(stack).getDisplayName());
		}
		for (int i = 0; i < this.tileEntity.getShortList().size(); i++) {
			this.filteredList.set(i, i);
		}
		for (int i = 0; i < displayNames.size(); i++) {
			if (!displayNames.get(i).toLowerCase().contains(text.toLowerCase())) {
				int index = filteredList.indexOf(i);
				filteredList.remove(index);
			}
		}
	}

}
