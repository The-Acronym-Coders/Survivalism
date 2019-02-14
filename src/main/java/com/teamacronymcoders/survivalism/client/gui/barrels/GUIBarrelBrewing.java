package com.teamacronymcoders.survivalism.client.gui.barrels;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.utils.helpers.HelperFluid;
import com.teamacronymcoders.survivalism.utils.network.MessageBarrelButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

public class GUIBarrelBrewing extends GuiContainer {
    private static final ResourceLocation brewing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_brewing.png");
    private static final int WIDTH = 180;
    private static final int HEIGHT = 163;

    private TileBarrelBrewing te;

    public GUIBarrelBrewing(TileBarrelBrewing te, Container container) {
        super(container);
        this.te = te;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new GuiButtonExt(0, guiLeft + 61, guiTop - 18, 54, 19, te.isSealed() ? "Unseal" : "Seal"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            Survivalism.INSTANCE.getPacketHandler().sendToServer(new MessageBarrelButton(0));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(brewing_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        if (te.isSealed()) {
            mc.fontRenderer.drawString("Sealed", guiLeft + 72, guiTop + 5, 4210752);
        } else {
            mc.fontRenderer.drawString("Un-Sealed", guiLeft + 63, guiTop + 5, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (te.getInput().getFluid() != null) {
            // Input
            int inputTank = te.getInput().getFluidAmount();
            float hr = 48f / 16000f;
            float offset = inputTank * hr;
            int y = Math.round(65 - offset);
            int h = Math.round(offset - 1);
            HelperFluid.renderTiledFluid(44, y, 16, h, 1, te.getInput().getFluid());
        }
        if (te.getOutput().getFluid() != null) {
            // Output
            float hr = 48f / 16000f;
            int outputTank = te.getOutput().getFluidAmount();
            float offset = outputTank * hr;
            int y = Math.round(65 - offset);
            int h = Math.round(offset - 1);
            HelperFluid.renderTiledFluid(116, y, 16, h, 1, te.getOutput().getFluid());
        }
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (te.getInput().getFluid() != null && this.isPointInRegion(44, 18, 16, 47, x, y)) {
            drawHoveringText(te.getInput().getFluid().getLocalizedName() + ": " + te.getInput().getFluidAmount(), x, y);
        }
        if (te.getOutput().getFluid() != null && this.isPointInRegion(116, 18, 16, 47, x, y)) {
            drawHoveringText(te.getOutput().getFluid().getLocalizedName() + ": " + te.getInput().getFluidAmount(), x, y);
        }
        super.renderHoveredToolTip(x, y);
    }

    public void setInput(FluidStack stack) {
        te.getInput().setFluid(stack);
    }

    public void setOutput(FluidStack stack) {
        te.getOutput().setFluid(stack);
    }
}
