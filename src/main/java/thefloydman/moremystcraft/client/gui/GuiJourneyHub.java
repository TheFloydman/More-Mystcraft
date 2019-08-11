package thefloydman.moremystcraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.capability.ICapabilityHub;
import thefloydman.moremystcraft.capability.ProviderCapabilityHub;
import thefloydman.moremystcraft.inventory.ContainerJourneyHub;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;
import thefloydman.moremystcraft.util.Reference;

public class GuiJourneyHub extends GuiContainer {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/journey_hub.png");
	private int centerX = 0;
	private int centerY = 0;
	private int guiX = 0;
	private int guiY = 0;
	private int boxXLocal = 160;
	private int boxYLocal = 147;
	private int boxXGlobal = 0;
	private int boxYGlobal = 0;
	private int boxWidth = 7;
	private int boxHeight = 7;
	private int textureXCheck = 176;
	private int textureYCheck = 0;
	private int textureXOverlay = 183;
	private int textureYOverlay = 0;
	private ContainerJourneyHub container;
	private TileEntitySingleItem tileEntity;

	public GuiJourneyHub(ContainerJourneyHub container) {
		super(container);
		this.xSize = 176;
		this.ySize = 163;
		this.container = container;
		this.tileEntity = container.tileEntity;
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
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;
		this.guiX = centerX - (this.xSize / 2);
		this.guiY = centerY - (this.ySize / 2);
		this.boxXGlobal = this.guiX + this.boxXLocal;
		this.boxYGlobal = this.guiY + this.boxYLocal;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, this.xSize, this.ySize);
		if (mouseOverBox(mouseX, mouseY)) {
			this.drawTexturedModalRect(this.boxXGlobal + 1, this.boxYGlobal + 1, this.textureXOverlay,
					this.textureYOverlay, this.boxWidth, this.boxHeight);
		}
		ICapabilityHub cap = this.tileEntity.getItem().getCapability(ProviderCapabilityHub.HUB, null);
		if (cap != null) {
			if (!cap.getPerPlayer()) {
				this.drawTexturedModalRect(this.boxXGlobal + 1, this.boxYGlobal + 1, this.textureXCheck,
						this.textureYCheck, this.boxWidth, this.boxHeight);
			}
		}
		int difference = 0;
		for (UUID id : cap.getUUIDs()) {
			GlStateManager.pushMatrix();
			float scale = 0.5F;
			GlStateManager.scale(scale, scale, 1.0F);
			this.fontRenderer.drawString("Dim: 2  |  Pos: 200, 70, 500", (int) ((this.guiX + 8) / scale), (int) ((this.guiY + 8 + difference) / scale), Color.WHITE.getRGB());
			GlStateManager.popMatrix();
			difference += 5;
		}
	}

	protected boolean mouseOverBox(int x, int y) {
		if (x >= this.boxXGlobal + 1 && x <= this.boxXGlobal + this.boxWidth && y >= this.boxYGlobal + 1
				&& y <= this.boxYGlobal + this.boxHeight) {
			return true;
		}
		return false;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			if (mouseOverBox(mouseX, mouseY)) {
				this.container.enchantItem(this.mc.player, -1);
				this.mc.playerController.sendEnchantPacket(container.windowId, -1);
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

}