package thefloydman.moremystcraft.client.gui;

import java.util.Arrays;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.xcompwiz.mystcraft.client.gui.GuiContainerElements;
import com.xcompwiz.mystcraft.client.gui.GuiElementSurfaceControlsBase;
import com.xcompwiz.mystcraft.client.gui.GuiWritingDesk;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementButtonToggle;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementPageSurface;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementPageSurface.PositionableItem;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementTextField;
import com.xcompwiz.mystcraft.data.Assets;
import com.xcompwiz.mystcraft.inventory.ContainerFolder;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;
import com.xcompwiz.mystcraft.page.Page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.inventory.ContainerSymbolRecordingDesk;

public class GuiSymbolRecordingDesk extends GuiContainerElements {
	private ContainerSymbolRecordingDesk container;
	private int mainTop;
	private static final int surfaceY = 132;
	private static final int buttonssize = 18;
	private static final int invsizeX = 176;
	private static final int invsizeY = 80;

	public class GuiElementSurfaceControls extends GuiElementSurfaceControlsBase {
		public GuiElementSurfaceControls(Minecraft mc, int guiLeft, int guiTop, int width, int height) {
			super(mc, guiLeft, guiTop, width, height);
		}

		@Nonnull
		public ItemStack getItemStack() {
			return GuiSymbolRecordingDesk.this.container.getInventoryItem();
		}

		public void copy(GuiElementPageSurface.PositionableItem collectionelement) {
			ResourceLocation symbol = Page.getSymbol(collectionelement.itemstack);
			      if (symbol == null)
			        return; 
			      if (collectionelement.count <= 0)
			        return; 
			      NBTTagCompound nbttagcompound = new NBTTagCompound();
			      nbttagcompound.setString("WriteSymbol", symbol.toString());
			      MystcraftPacketHandler.CHANNEL.sendToServer(new MPacketGuiMessage(GuiSymbolRecordingDesk.this.mc.player.openContainer.windowId, nbttagcompound));
			      GuiSymbolRecordingDesk.this.container.processMessage(GuiSymbolRecordingDesk.this.mc.player, nbttagcompound);
		}

		@Override
		public void pickup(PositionableItem arg0) {
		}

		@Override
		public void place(int arg0, boolean arg1) {
		}
	}

	public GuiSymbolRecordingDesk(ContainerSymbolRecordingDesk con) {
		super(con);
		this.container = (ContainerSymbolRecordingDesk) this.inventorySlots;
	}

	public void validate() {
		Keyboard.enableRepeatEvents(true);
		this.xSize = 176;
		this.ySize = 231;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.mainTop = this.guiTop + 132 + 1;

		GuiElementTextField txt_box = null;

		GuiElementSurfaceControls surfacemanager = new GuiElementSurfaceControls(this.mc, 0, 0, this.xSize,
				this.surfaceY);
		txt_box = new GuiElementTextField(surfacemanager, surfacemanager, "SearchBox", 40, 0, this.xSize - 40, 18);
		addElement(txt_box);

		GuiElementPageSurface surface = new GuiElementPageSurface(surfacemanager, this.mc, 0, 19, this.xSize, 114);
		surfacemanager.addListener(surface);
		addElement(surface);

		GuiElementButtonToggle guiElementButtonToggle1 = new GuiElementButtonToggle(surfacemanager, surfacemanager,
				"AZ", 0, 0, 18, 18);
		guiElementButtonToggle1.setText("AZ");
		guiElementButtonToggle1.setTooltip(Arrays.asList(new String[] { "Sort Alphabetically" }));
		addElement(guiElementButtonToggle1);
		GuiElementButtonToggle guiElementButtonToggle2 = new GuiElementButtonToggle(surfacemanager, surfacemanager,
				"ALL", 18, 0, 18, 18);
		guiElementButtonToggle2.setText("ALL");
		guiElementButtonToggle2.setTooltip(Arrays.asList(new String[] { "Show all Symbols" }));
		addElement(guiElementButtonToggle2);

		surfacemanager.addSurfaceElement(guiElementButtonToggle1);
		surfacemanager.addSurfaceElement(guiElementButtonToggle2);
	}

	protected void _drawBackgroundLayer(int mouseX, int mouseY, float f) {

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(Assets.GUIs.desk);
		drawTexturedModalRect(this.guiLeft, this.mainTop, 0, 82, 176, 80);

	}
}