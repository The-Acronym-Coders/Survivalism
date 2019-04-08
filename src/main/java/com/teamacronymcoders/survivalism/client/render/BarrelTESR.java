package com.teamacronymcoders.survivalism.client.render;

import com.teamacronymcoders.survivalism.common.tiles.barrels.*;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static com.teamacronymcoders.survivalism.utils.helpers.RenderHelper.colEnd;
import static com.teamacronymcoders.survivalism.utils.helpers.RenderHelper.posTex;

public class BarrelTESR extends TileEntitySpecialRenderer<TileBarrelBase> {
    
    @Override
    public void render(TileBarrelBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();
        if(!te.isSealed()) {
            renderItem(te);
            renderFluid(te);
        }
        
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
    
    private void renderItem(TileBarrelBase te) {
        List<ItemStack> stacks = new ArrayList<>();
        
        if(te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            for(int i = 0; i < brewing.getInv().getSlots(); i++) {
                stacks.add(brewing.getInv().getStackInSlot(i));
            }
        } else if(te instanceof TileBarrelSoaking) {
            stacks.add(((TileBarrelSoaking) te).getInv().getStackInSlot(0));
        } else if(te instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            for(int i = 0; i < storage.getInv().getSlots(); i++) {
                stacks.add(storage.getInv().getStackInSlot(i));
            }
        }
        
        if(!stacks.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(.5, .75, .5);
            for(ItemStack stack : stacks) {
                if (stack.getItem() instanceof ItemBlock) {
                    GlStateManager.scale(.25f, .25f, .25f);
                } else {
                    GlStateManager.scale(.5f, .5f, .5f);
                }
                GlStateManager.rotate(360F / stacks.size(), 0f, 1f, 0f);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
                if (stack.getItem() instanceof ItemBlock) {
                    GlStateManager.scale(4f, 4f, 4f);
                } else {
                    GlStateManager.scale(2f, 2f, 2f);
                }
            }
            GlStateManager.popMatrix();
        }
    }
    
    private void renderFluid(TileBarrelBase te) {
        GlStateManager.pushMatrix();
        int capacity = 0;
        FluidStack fluid = null;
        
        if(te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            if(brewing.getOutput().getFluid() != null) {
                capacity = brewing.getOutput().getCapacity();
                fluid = brewing.getOutput().getFluid();
            } else {
                capacity = brewing.getInput().getCapacity();
                fluid = brewing.getInput().getFluid();
            }
        } else if(te instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) te;
            capacity = soaking.getInput().getCapacity();
            fluid = soaking.getInput().getFluid();
        } else if(te instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            capacity = storage.getInput().getCapacity();
            fluid = storage.getInput().getFluid();
        }
        
        if(fluid != null) {
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buff = tess.getBuffer();
            
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());
            
            float posY = 3+ (((float) fluid.amount / (float) capacity) * 11f);
            float[] color = FluidHelper.getColorRGBA(fluid.getFluid().getColor(fluid));
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(770, 771);
            buff.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX_COLOR);
            
            colEnd(posTex(buff, still, 8, posY, 8), color);
            
            colEnd(posTex(buff, still, 8, posY, 15), color);
            colEnd(posTex(buff, still, 10, posY, 15), color);
            colEnd(posTex(buff, still, 10, posY, 14), color);
            colEnd(posTex(buff, still, 12, posY, 14), color);
            colEnd(posTex(buff, still, 12, posY, 13), color);
            colEnd(posTex(buff, still, 13, posY, 13), color);
            colEnd(posTex(buff, still, 13, posY, 12), color);
            colEnd(posTex(buff, still, 14, posY, 12), color);
            colEnd(posTex(buff, still, 14, posY, 10), color);
            colEnd(posTex(buff, still, 15, posY, 10), color);
            
            colEnd(posTex(buff, still, 15, posY, 8), color);
            colEnd(posTex(buff, still, 15, posY, 6), color);
            colEnd(posTex(buff, still, 14, posY, 6), color);
            colEnd(posTex(buff, still, 14, posY, 4), color);
            colEnd(posTex(buff, still, 13, posY, 4), color);
            colEnd(posTex(buff, still, 13, posY, 3), color);
            colEnd(posTex(buff, still, 12, posY, 3), color);
            colEnd(posTex(buff, still, 12, posY, 2), color);
            colEnd(posTex(buff, still, 10, posY, 2), color);
            colEnd(posTex(buff, still, 10, posY, 1), color);
            colEnd(posTex(buff, still, 8, posY, 1), color);
            
            colEnd(posTex(buff, still, 8, posY, 1), color);
            colEnd(posTex(buff, still, 6, posY, 1), color);
            colEnd(posTex(buff, still, 6, posY, 2), color);
            colEnd(posTex(buff, still, 4, posY, 2), color);
            colEnd(posTex(buff, still, 4, posY, 3), color);
            colEnd(posTex(buff, still, 3, posY, 3), color);
            colEnd(posTex(buff, still, 3, posY, 4), color);
            colEnd(posTex(buff, still, 2, posY, 4), color);
            colEnd(posTex(buff, still, 2, posY, 6), color);
            colEnd(posTex(buff, still, 1, posY, 6), color);
            
            colEnd(posTex(buff, still, 1, posY, 8), color);
            colEnd(posTex(buff, still, 1, posY, 10), color);
            colEnd(posTex(buff, still, 2, posY, 10), color);
            colEnd(posTex(buff, still, 2, posY, 12), color);
            colEnd(posTex(buff, still, 3, posY, 12), color);
            colEnd(posTex(buff, still, 3, posY, 13), color);
            colEnd(posTex(buff, still, 4, posY, 13), color);
            colEnd(posTex(buff, still, 4, posY, 14), color);
            colEnd(posTex(buff, still, 6, posY, 14), color);
            colEnd(posTex(buff, still, 6, posY, 15), color);
            colEnd(posTex(buff, still, 8, posY, 15), color);
            
            tess.draw();
            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            
            
        }
        GlStateManager.popMatrix();
    }
}
