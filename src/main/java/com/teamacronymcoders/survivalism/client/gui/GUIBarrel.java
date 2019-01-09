package com.teamacronymcoders.survivalism.client.gui;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrel;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.network.MessageOpenGui;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

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
        if(te.checkState(StorageEnumsBarrelStates.STORAGE)) {
            true_background = storage_background;
        } else if(te.checkState(StorageEnumsBarrelStates.BREWING)) {
            true_background = brewing_background;
        } else if(te.checkState(StorageEnumsBarrelStates.SOAKING)) {
            true_background = soaking_background;
        }
        xSize = WIDTH;
        ySize = HEIGHT;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButtonExt(0, guiLeft - buttonH - 5, guiTop, buttonW, buttonH, "") {
            
            
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                super.drawButton(mc, mouseX, mouseY, partialTicks);
                ResourceLocation tex = null;
                if(te.checkState(StorageEnumsBarrelStates.BREWING)) {
                    tex = new ResourceLocation("textures/items/brewing_stand.png");
                } else if(te.checkState(StorageEnumsBarrelStates.SOAKING)) {
                    tex = new ResourceLocation("textures/items/bucket_water.png");
                } else if(te.checkState(StorageEnumsBarrelStates.STORAGE)) {
                    tex = new ResourceLocation("textures/items/minecart_chest.png");
                }
                if(tex != null) {
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
       
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0) {
            te.cycleStates(te.getWorld().getBlockState(te.getPos()));
            Survivalism.INSTANCE.getPacketHandler().sendToServer(new MessageOpenGui(te.getPos(), BlockBarrel.GUI_ID));
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(true_background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
