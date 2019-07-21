package thefloydman.moremystcraft.client.gui;

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
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thefloydman.moremystcraft.inventory.ContainerNexusController;
import thefloydman.moremystcraft.util.Reference;

public class GuiNexusController extends GuiContainerElements {

	ResourceLocation resLocNexusController = new ResourceLocation(Reference.MOD_ID,
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

	public GuiNexusController(Container container, InventoryPlayer playerInv) {
		super(container);
		this.playerInv = playerInv;
		this.container = (ContainerNexusController)this.inventorySlots;
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
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resLocNexusController);
		drawTexturedModalRect(guiCenterX, guiCenterY, 0, 0, guiWidth, guiHeight);

		int listOneX = guiCenterX + 7;
		int listOneY = guiCenterY + 23;
		int listTwoX = guiCenterX + 7;
		int listTwoY = guiCenterY + 41;
		int listThreeX = guiCenterX + 7;
		int listThreeY = guiCenterY + 59;
		int searchStringX = guiCenterX + 9;
		int searchStringY = guiCenterY + 9;
		
		fontRenderer.drawString("Search (nonfunctioning)", searchStringX, searchStringY, 0xFFFFFF);
	}
	
	@Override
	protected void _drawForegroundLayer(final int mouseX, final int mouseY) {
    }
	
	@Override
	protected void _onMouseUp(final int mouseX, final int mouseY, final int clicked_id, final boolean eaten) {
		if (playerInv.getItemStack().isEmpty()) {
			return;
		}
		/*if (isCursorOnBookDisplay(mouseX, mouseY) == true && playerInv.getItemStack().getItem() instanceof ItemLinking) {
			this.container.bookArray.add(0, playerInv.getItemStack());
			playerInv.setItemStack(Items.AIR.getDefaultInstance());
		}*/
		if (isCursorOnBookList(mouseX, mouseY) && playerInv.getItemStack().getItem() instanceof ItemLinking) {
			this.container.tileEntity.bookList.add(0, playerInv.getItemStack());
			this.container.tileEntity.markDirty();
			playerInv.setItemStack(Items.AIR.getDefaultInstance());
			this.container.bookSelected = true;
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
            nbttagcompound.setByte("Link", (byte)0);
            MystcraftPacketHandler.CHANNEL.sendToServer((IMessage)new MPacketGuiMessage(GuiNexusController.this.container.windowId, nbttagcompound));
        }
    }
	
}