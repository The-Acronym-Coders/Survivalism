package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.helpers.HelperFluid;
import com.teamacronymcoders.survivalism.utils.network.MessageBarrelButton;
import com.teamacronymcoders.survivalism.utils.storages.BarrelState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GUIBarrel extends GuiContainer {

    private static final ResourceLocation brewing_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_brewing.png");
    private static final ResourceLocation soaking_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking.png");
    private static final int WIDTH = 180;
    private static final int HEIGHT = 163;
    private static final int buttonW = 18;
    private static final int buttonH = 19;
    private static final ResourceLocation storage_background = new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_storage.png");

    private ResourceLocation true_background = null;

    private TileBarrel te;
    private BarrelState state;

    public GUIBarrel(TileBarrel te, Container container) {
        super(container);
        this.te = te;
        this.state = te.getState();
        if (state == BarrelState.STORAGE) {
            true_background = storage_background;
        } else if (state == BarrelState.BREWING) {
            true_background = brewing_background;
        } else {
            true_background = soaking_background;
        }
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
        buttonList.add(new GuiButtonExt(0, guiLeft - buttonH - 5, guiTop, buttonW, buttonH, "") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                super.drawButton(mc, mouseX, mouseY, partialTicks);
                ResourceLocation tex = null;
                if (state == BarrelState.BREWING) {
                    tex = new ResourceLocation("textures/items/brewing_stand.png");
                } else if (state == BarrelState.SOAKING) {
                    tex = new ResourceLocation("textures/items/bucket_water.png");
                } else {
                    tex = new ResourceLocation("textures/items/minecart_chest.png");
                }

                if (tex != null) {
                    mc.getTextureManager().bindTexture(tex);
                    BufferBuilder buff = Tessellator.getInstance().getBuffer();
                    buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buff.pos(x + 1, y + buttonH - 1, 0).tex(0, 1).endVertex();
                    buff.pos(x + buttonW - 1, y + buttonH - 1, 0).tex(1, 1).endVertex();
                    buff.pos(x + buttonW - 1, y + 1, 0).tex(1, 0).endVertex();
                    buff.pos(x + 1, y + 1, 0).tex(0, 0).endVertex();
                    Tessellator.getInstance().draw();
                }
            }
        });
        buttonList.add(new GuiButtonExt(1, guiLeft + 61, guiTop - 18, 54, buttonH, te.isSealed() ? "Unseal" : "Seal"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            Survivalism.INSTANCE.getPacketHandler().sendToServer(new MessageBarrelButton(0));
        } else if (button.id == 1) {
            Survivalism.INSTANCE.getPacketHandler().sendToServer(new MessageBarrelButton(1));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(true_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        if (te.isSealed()) {
            mc.fontRenderer.drawString("Sealed", guiLeft + 72, guiTop + 5, 4210752);
        } else {
            mc.fontRenderer.drawString("Un-Sealed", guiLeft + 63, guiTop + 5, 4210752);
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (state == BarrelState.SOAKING) {
            if (te.getInput().getFluid() != null) {
                int amount = te.getInput().getFluidAmount();
                float hr = 48f / 16000f;
                float offset = amount * hr;
                int y = Math.round(72 - offset);
                int h = Math.round(offset - 1);
                HelperFluid.renderTiledFluid(80, y, 16, h, 1, te.getInput().getFluid());
            }
        } else if (state == BarrelState.BREWING) {
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
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (state == BarrelState.SOAKING) {
            if (te.getInput().getFluid() != null && this.isPointInRegion(79, 24, 16, 47, x, y)) {
                drawHoveringText(te.getInput().getFluid().getLocalizedName(), x, y);
            }
        } else if (state == BarrelState.BREWING) {
            if (te.getInput().getFluid() != null && this.isPointInRegion(44, 18, 16, 47, x, y)) {
                drawHoveringText(te.getInput().getFluid().getLocalizedName(), x, y);
            }
            if (te.getOutput().getFluid() != null && this.isPointInRegion(116, 18, 16, 47, x, y)) {
                drawHoveringText(te.getOutput().getFluid().getLocalizedName(), x, y);
            }
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
