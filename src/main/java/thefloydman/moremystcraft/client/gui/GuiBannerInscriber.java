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
import thefloydman.moremystcraft.inventory.ContainerBannerInscriber;
import thefloydman.moremystcraft.inventory.ContainerNexusController;
import thefloydman.moremystcraft.util.Reference;

public class GuiBannerInscriber extends GuiContainerElements {

	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/banner_inscriber.png");
	int guiWidth = 176;
	int guiHeight = 182;
	int windowCenterX = 0;
	int windowCenterY = 0;
	int guiCenterX = 0;
	int guiCenterY = 0;
	private InventoryPlayer playerInv;
	private EntityPlayer player;
	private ContainerBannerInscriber container;

	public GuiBannerInscriber(Container container, InventoryPlayer playerInv) {
		super(container);
		this.playerInv = playerInv;
		this.container = (ContainerBannerInscriber)this.inventorySlots;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	protected void _drawBackgroundLayer(final int mouseX, final int mouseY, final float f) {
		windowCenterX = this.width / 2;
		windowCenterY = this.height / 2;
		guiCenterX = windowCenterX - (guiWidth / 2);
		guiCenterY = windowCenterY - (guiHeight / 2);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiCenterX, guiCenterY, 0, 0, guiWidth, guiHeight);
	}
	
}