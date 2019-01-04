package com.teamacronymcoders.survivalism.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ButtonBarrelSwitch extends GuiButton {

    Minecraft minecraft;

    public ButtonBarrelSwitch(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            minecraft = mc;
            super.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture();
        super.drawButtonForegroundLayer(mouseX, mouseY);
    }
}
