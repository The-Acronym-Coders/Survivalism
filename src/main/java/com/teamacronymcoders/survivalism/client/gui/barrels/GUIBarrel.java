package com.teamacronymcoders.survivalism.client.gui.barrels;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GUIBarrel extends GuiContainer {

    public GUIBarrel(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
