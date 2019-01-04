package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GUIBarrel extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 163;

    private static final ResourceLocation brewing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_brewing.png");
    private static final ResourceLocation soaking_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking.png");
    private static final ResourceLocation storage_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_storage.png");

    private ResourceLocation true_background = null;

    public GUIBarrel(TileBarrel te, ContainerBarrel storage) {
        super(storage);
        if (te.checkState(StorageEnumsBarrelStates.STORAGE)) {
            true_background = storage_background;
        } else if (te.checkState(StorageEnumsBarrelStates.BREWING)) {
            true_background = brewing_background;
        } else if (te.checkState(StorageEnumsBarrelStates.SOAKING)) {
            true_background = soaking_background;
        }
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        int x = 1;
        int y = 1;
        buttonList.add(new GuiButton(1, x, y, ""));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(true_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
