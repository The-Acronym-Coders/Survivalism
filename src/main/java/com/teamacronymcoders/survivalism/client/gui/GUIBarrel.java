package com.teamacronymcoders.survivalism.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.helpers.HelperFluid;
import com.teamacronymcoders.survivalism.utils.storages.BarrelState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GUIBarrel extends GuiContainer {

	private static final int WIDTH = 180;
	private static final int HEIGHT = 163;

	private static final int buttonW = 18;
	private static final int buttonH = 19;

	private static final ResourceLocation brewing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_brewing.png");
	private static final ResourceLocation soaking_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking.png");
	private static final ResourceLocation storage_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_storage.png");

	private ResourceLocation true_background = null;

	private TileBarrel te;
	private BarrelState state;
	private int tooltipY;

	public GUIBarrel(TileBarrel te, Container container) {
		super(container);
		this.te = te;
		state = te.getState();
		if (state == BarrelState.STORAGE) true_background = storage_background;
		else if (state == BarrelState.BREWING) true_background = brewing_background;
		else true_background = soaking_background;
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		buttonList.add(new GuiButtonExt(0, guiLeft - buttonH - 5, guiTop, buttonW, buttonH, "") {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
				super.drawButton(mc, mouseX, mouseY, partialTicks);
				ResourceLocation tex = null;
				if (state == BarrelState.BREWING) tex = new ResourceLocation("textures/items/brewing_stand.png");
				else if (state == BarrelState.SOAKING) tex = new ResourceLocation("textures/items/bucket_water.png");
				else tex = new ResourceLocation("textures/items/minecart_chest.png");

				if (tex != null) {
					mc.getTextureManager().bindTexture(tex);
					BufferBuilder buff = Tessellator.getInstance().getBuffer();
					buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
					buff.pos(x + 1, y + buttonH - 1, 0).tex(0, 1).endVertex();
					buff.pos(x + buttonW - 1, y + buttonH - 1, 0).tex(1, 1).endVertex();
					buff.pos(x + buttonW - 1, y + 1, 0).tex(1, 0).endVertex();
					buff.pos(x + 1, y + 1, 0).tex(0, 0).endVertex();
					Tessellator.getInstance().draw();
				}
			}
		});
		buttonList.add(new GuiButtonExt(1, guiLeft + 61, guiTop - 18, 54, buttonH, "Sealed"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			//TODO: Message send to server: cycle barrel
		} else if (button.id == 1) {
			//TODO: Message send to server: seal barrel
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(true_background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (te.isSealed()) mc.fontRenderer.drawString("Sealed", guiLeft + 72, guiTop + 5, 4210752);
		else mc.fontRenderer.drawString("Un-Sealed", guiLeft + 63, guiTop + 5, 4210752);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (state == BarrelState.SOAKING) {
			if (te.getInput().getFluid() != null) {
				int amount = te.getInput().getFluidAmount();
				float hr = 48f / 16000f;
				float offset = amount * hr;
				int y = Math.round(72 - offset);
				tooltipY = y;
				int h = Math.round(offset - 1);
				HelperFluid.renderTiledFluid(80, y, 16, h, 1, te.getInput().getFluid());
			}
		} else if (state == BarrelState.BREWING) {
			if (te.getInput().getFluid() != null) {
				// Input
				int inputTank = te.getInput().getFluidAmount();
				float hr = 48f / 16000f;
				float offset = inputTank * hr;
				int y = Math.round(65 - offset);
				tooltipY = y;
				int h = Math.round(offset - 1);
				HelperFluid.renderTiledFluid(44, y, 16, h, 1, te.getInput().getFluid());
			}
			if (te.getInput().getFluid() != null) {
				// Output
				float hr = 48f / 16000f;
				int outputTank = te.getOutput().getFluidAmount();
				float offset = outputTank * hr;
				int y = Math.round(65 - offset);
				tooltipY = y;
				int h = Math.round(offset - 1);
				HelperFluid.renderTiledFluid(116, y, 16, h, 1, te.getOutput().getFluid());
			}
		}
	}

	//TODO: FIXME
	@Override
	protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
		if (p_191948_1_ >= 80 && p_191948_2_ <= 120 && p_191948_2_ >= tooltipY && p_191948_2_ <= 72) {
			drawHoveringText(te.getInput().getFluid().getLocalizedName(), p_191948_1_, p_191948_2_);
		}

		super.renderHoveredToolTip(p_191948_1_, p_191948_2_);
	}
}
