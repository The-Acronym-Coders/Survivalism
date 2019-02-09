package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.vat.ContainerVat;
import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import com.teamacronymcoders.survivalism.utils.helpers.HelperFluid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GUICrushingVat extends GuiContainer {

    private static final ResourceLocation crushing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking.png");
    private int tooltipY;
    private TileCrushingVat te;

    public GUICrushingVat(TileCrushingVat tile, ContainerVat inventorySlotsIn) {
        super(inventorySlotsIn);
        this.te = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(crushing_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (te.getTank().getFluid() != null) {
            int amount = te.getTank().getFluidAmount();
            float hr = 48f / 16000f;
            float offset = amount * hr;
            int y = Math.round(72 - offset);
            tooltipY = y;
            int h = Math.round(offset - 1);
            HelperFluid.renderTiledFluid(80, y, 16, h, 1, te.getTank().getFluid());
        }
    }

    @Override
    protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
        if (p_191948_1_ >= 80 && p_191948_2_ <= 120 && p_191948_2_ >= tooltipY && p_191948_2_ <= 72) {
            drawHoveringText(te.getTank().getFluid().getLocalizedName(), p_191948_1_, p_191948_2_);
        }
        super.renderHoveredToolTip(p_191948_1_, p_191948_2_);
    }
}
