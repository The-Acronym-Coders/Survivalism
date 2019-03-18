package com.teamacronymcoders.survivalism.client.gui.barrels;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.gui.helper.GUIHelper;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
import com.teamacronymcoders.survivalism.utils.network.MessageBarrelButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIBarrelBrewing extends GUIBarrel {
    private static final ResourceLocation brewing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_brewing.png");
    private static final int WIDTH = 180;
    private static final int HEIGHT = 165;

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
        StringBuilder string = new StringBuilder();
        String input;
        int xPos;
        if (te.isSealed()) {
            if (te.getRecipe() != null) {
                string.append("(").append(te.getRecipe().getInput().getLocalizedName()).append(") - ");
                string.append("Sealed");
                string.append(" - (").append(te.getRecipe().getOutput().getLocalizedName()).append(")");
                input = string.toString();
                xPos = (guiLeft + (WIDTH / 2) - mc.fontRenderer.getStringWidth(input)/2);
                mc.fontRenderer.drawString(string.toString(), xPos, guiTop + 5, 4210752);
            } else {
                string.append("Sealed");
                input = string.toString();
                xPos = (guiLeft + (WIDTH / 2) - mc.fontRenderer.getStringWidth(input)/2);
                mc.fontRenderer.drawString(string.toString(), xPos, guiTop + 5, 4210752);
            }
        } else {
            string.append("Un-Sealed");
            input = string.toString();
            xPos = (guiLeft + (WIDTH / 2) - mc.fontRenderer.getStringWidth(input)/2);
            mc.fontRenderer.drawString(string.toString(), xPos, guiTop + 5, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (te.getInput().getFluid() != null) {
            // Input
            int inputTank = te.getInput().getFluidAmount();
            float hr = 48f / SurvivalismConfigs.brewingInputSize;
            float offset = inputTank * hr;
            int y = Math.round(65 - offset);
            int h = Math.round(offset - 1);
            FluidHelper.renderTiledFluid(44, y, 16, h, 1, te.getInput().getFluid());
        }
        if (te.getOutput().getFluid() != null) {
            // Output
            float hr = 48f / SurvivalismConfigs.brewingOutputSize;
            int outputTank = te.getOutput().getFluidAmount();
            float offset = outputTank * hr;
            int y = Math.round(65 - offset);
            int h = Math.round(offset - 1);
            FluidHelper.renderTiledFluid(116, y, 16, h, 1, te.getOutput().getFluid());
        }
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (te.getInput().getFluid() != null && this.isPointInRegion(44, 18, 16, 47, x, y)) {
            List<String> strings = new ArrayList<>();
            GUIHelper.addPotionTooltip(strings, te.getInput().getFluid(), te.getInput().getCapacity());
            drawHoveringText(strings, x, y);
        }
        if (te.getOutput().getFluid() != null && this.isPointInRegion(116, 18, 16, 47, x, y)) {
            List<String> strings = new ArrayList<>();
            GUIHelper.addPotionTooltip(strings, te.getOutput().getFluid(), te.getOutput().getCapacity());
            drawHoveringText(strings, x, y);
        }
        super.renderHoveredToolTip(x, y);
    }
}
