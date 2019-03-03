package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import com.teamacronymcoders.survivalism.utils.helpers.HelperFluid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GUIMixingVat extends GuiContainer {

    private static final ResourceLocation mixing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/mixing_vat.png");
    private TileMixingVat te;

    public GUIMixingVat(TileMixingVat te, Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.te = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(mixing_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (te.getMain().getFluid() != null) {
            int amount = te.getMain().getFluidAmount();
            float hr = 48f / 16000f;
            float offset = amount * hr;
            int y = Math.round(71 - offset);
            int h = Math.round(offset - 1);
            HelperFluid.renderTiledFluid(26, y, 16, h, 1, te.getMain().getFluid());
        }
        if (te.getSecondary().getFluid() != null) {
            int amount = te.getSecondary().getFluidAmount();
            float hr = 48f / 16000f;
            float offset = amount * hr;
            int y = Math.round(71 - offset);
            int h = Math.round(offset - 1);
            HelperFluid.renderTiledFluid(44, y, 16, h, 1, te.getSecondary().getFluid());
        }
        if (te.getOutput().getFluid() != null) {
            int amount = te.getOutput().getFluidAmount();
            float hr = 48f / 16000f;
            float offset = amount * hr;
            int y = Math.round(72 - offset);
            int h = Math.round(offset - 1);
            HelperFluid.renderTiledFluid(134, y, 16, h, 1, te.getOutput().getFluid());
        }
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (te.getMain().getFluid() != null && this.isPointInRegion(26, 24, 15, 47, x, y)) {
            drawHoveringText(te.getMain().getFluid().getLocalizedName() + ": " + te.getMain().getFluidAmount() + "/" + te.getMain().getCapacity() + "mB", x, y);
        }
        if (te.getSecondary().getFluid() != null && this.isPointInRegion(44, 24, 15, 47, x, y)) {
            drawHoveringText(te.getSecondary().getFluid().getLocalizedName() + ": " + te.getSecondary().getFluidAmount() + "/" + te.getSecondary().getCapacity() + "mB", x, y);
        }
        if (te.getOutput().getFluid() != null && this.isPointInRegion(134, 24, 15, 47, x, y)) {
            drawHoveringText(te.getOutput().getFluid().getLocalizedName() + ": " + te.getOutput().getFluidAmount() + "/" + te.getOutput().getCapacity() + "mB", x, y);
        }
        super.renderHoveredToolTip(x, y);
    }

    public void setMain(FluidStack stack) {
        te.getMain().setFluid(stack);
    }

    public void setSecondary(FluidStack stack) {
        te.getSecondary().setFluid(stack);
    }

    public void setOutput(FluidStack stack) {
        te.getOutput().setFluid(stack);
    }
}