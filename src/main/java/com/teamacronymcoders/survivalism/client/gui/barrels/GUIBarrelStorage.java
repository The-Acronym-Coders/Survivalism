package com.teamacronymcoders.survivalism.client.gui.barrels;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.gui.helper.GUIHelper;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
import com.teamacronymcoders.survivalism.utils.network.MessageBarrelButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIBarrelStorage extends GUIBarrel {
    private static final ResourceLocation storage_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_storage.png");
    private static final int WIDTH = 180;
    private static final int HEIGHT = 163;

    private TileBarrelStorage te;

    public GUIBarrelStorage(TileBarrelStorage te, Container inventorySlotsIn) {
        super(inventorySlotsIn);
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
        mc.getTextureManager().bindTexture(storage_background);
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
            int amount = te.getInput().getFluidAmount();
            float hr = 48f / SurvivalismConfigs.storageTankSize;
            float offset = amount * hr;
            int y = Math.round(72 - offset);
            int h = Math.round(offset - 1);
            FluidHelper.renderTiledFluid(134, y - 2, 16, h, 1, te.getInput().getFluid());
        }
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (te.getInput().getFluid() != null && this.isPointInRegion(134, 22, 16, 47, x, y)) {
            List<String> strings = new ArrayList<>();
            GUIHelper.addPotionTooltip(strings, te.getInput().getFluid(), te.getInput().getCapacity());
            drawHoveringText(strings, x, y);
        }
        super.renderHoveredToolTip(x, y);
    }

    public void setInput(FluidStack stack) {
        te.getInput().setFluid(stack);
    }
}
