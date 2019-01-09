package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrel;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

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

    public GUIBarrel(TileBarrel te, ContainerBarrel storage) {
        super(storage);
        this.te = te;
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
        buttonList.add(new GuiButton(0, 395, 225, buttonH, buttonW, "") {
            @Override
            public void drawButtonForegroundLayer(int mouseX, int mouseY) {
                if (te.checkState(StorageEnumsBarrelStates.BREWING)) {
                    mc.getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/items/brewing_stand.png"));
                    drawTexturedModalRect(x, y, 0, 0, buttonW, buttonW);
                } else if (te.checkState(StorageEnumsBarrelStates.SOAKING)) {
                    mc.getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/items/bucket_water.png"));
                    drawTexturedModalRect(x, y, 0, 0, buttonW, buttonW);
                } else if (te.checkState(StorageEnumsBarrelStates.STORAGE)) {
                    mc.getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/items/minecart_chest.png"));
                    drawTexturedModalRect(x, y, 0, 0, buttonW, buttonW);
                }
                super.drawButtonForegroundLayer(mouseX, mouseY);
            }
        });
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            if (te.checkState(StorageEnumsBarrelStates.STORAGE)) {
                te.cycleStates(StorageEnumsBarrelStates.STORAGE);
                mc.player.openGui(Survivalism.INSTANCE, BlockBarrel.GUI_ID, mc.player.world, (int) mc.player.getPositionVector().x, (int) mc.player.getPositionVector().y, (int) mc.player.getPositionVector().z);
            } else if (te.checkState(StorageEnumsBarrelStates.BREWING)) {
                te.cycleStates(StorageEnumsBarrelStates.BREWING);
                mc.player.openGui(Survivalism.INSTANCE, BlockBarrel.GUI_ID, mc.player.world, (int) mc.player.getPositionVector().x, (int) mc.player.getPositionVector().y, (int) mc.player.getPositionVector().z);
            } else if (te.checkState(StorageEnumsBarrelStates.SOAKING)) {
                te.cycleStates(StorageEnumsBarrelStates.SOAKING);
                mc.player.openGui(Survivalism.INSTANCE, BlockBarrel.GUI_ID, mc.player.world, (int) mc.player.getPositionVector().x, (int) mc.player.getPositionVector().y, (int) mc.player.getPositionVector().z);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(true_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
