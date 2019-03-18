package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.gui.helper.GUIHelper;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GUICrushingVat extends GuiContainer {

    private static final ResourceLocation crushing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking.png");
    private TileCrushingVat te;

    public GUICrushingVat(TileCrushingVat tile, Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.te = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(crushing_background);
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
        if (te.getTank().getFluid() != null) {
            int amount = te.getTank().getFluidAmount();
            float hr = 48f / 16000f;
            float offset = amount * hr;
            int y = Math.round(72 - offset);
            int h = Math.round(offset - 1);
            FluidHelper.renderTiledFluid(80, y, 16, h, 1, te.getTank().getFluid());
        }
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (te.getTank().getFluid() != null && this.isPointInRegion(79, 24, 16, 47, x, y)) {
            List<String> strings = new ArrayList<>();
            GUIHelper.addPotionTooltip(strings, te.getTank().getFluid(), te.getTank().getCapacity());
            drawHoveringText(strings, x, y);
        }
        super.renderHoveredToolTip(x, y);
    }

    public void setFluid(FluidStack stack) {
        te.getTank().setFluid(stack);
    }
}
