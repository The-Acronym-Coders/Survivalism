package com.teamacronymcoders.survivalism.client.render;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import static com.teamacronymcoders.survivalism.utils.helpers.RenderHelper.colEnd;
import static com.teamacronymcoders.survivalism.utils.helpers.RenderHelper.posTex;

public class MixingTESR extends TileEntitySpecialRenderer<TileMixingVat> {

    @Override
    public void render(TileMixingVat te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderItem(te);
        if (te.getMain().getFluid() != null || te.getSecondary().getFluid() != null || te.getOutput().getFluid() != null) {
            renderFluid(te);
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderItem(TileMixingVat te) {
        ItemStack stack = te.getHandler().getStackInSlot(0);

        if (!stack.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(.5, .325, .5);

            if (stack.getItem() instanceof ItemBlock) {
                GlStateManager.scale(.25f, .25f, .25f);
            } else {
                GlStateManager.scale(.5f, .5f, .5f);
            }
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

    private void renderFluid(TileMixingVat te) {
        GlStateManager.pushMatrix();
        int capacity1 = 0;
        int capacity2 = 0;
        int capacity3 = 0;

        FluidStack fluid1 = null;
        FluidStack fluid2 = null;
        FluidStack fluid3 = null;

        if (te.getMain().getFluid() != null) {
            capacity1 = te.getMain().getCapacity();
            fluid1 = te.getMain().getFluid();
        }

        if (te.getSecondary().getFluid() != null) {
            capacity2 = te.getSecondary().getCapacity();
            fluid2 = te.getSecondary().getFluid();
        }

        if (te.getOutput().getFluid() != null) {
            capacity3 = te.getOutput().getCapacity();
            fluid3 = te.getOutput().getFluid();
        }

        if (fluid3 == null) {
            if (fluid1 != null) {
                renderFluid1(fluid1, capacity1);
            }

            if (fluid2 != null) {
                renderFluid2(fluid2, capacity2);
            }
        } else {
            renderFluid3(fluid3, capacity3);
        }
        GlStateManager.popMatrix();
    }

    private void renderFluid1(FluidStack fluid, int capacity) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());

        float posY = 3 + (((float) fluid.amount / (float) capacity) * 11f);
        float[] color = FluidHelper.getColorRGBA(fluid.getFluid().getColor(fluid));
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(770, 771);
        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        colEnd(posTex(buff, still, 3, posY, 8), color);
        colEnd(posTex(buff, still, 8, posY, 8), color);
        colEnd(posTex(buff, still, 8, posY, 3), color);
        colEnd(posTex(buff, still, 3, posY, 3), color);

        tess.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    private void renderFluid2(FluidStack fluid, int capacity) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());

        float posY = 3 + (((float) fluid.amount / (float) capacity) * 11f);
        float[] color = FluidHelper.getColorRGBA(fluid.getFluid().getColor(fluid));
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(770, 771);
        buff.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX_COLOR);

        colEnd(posTex(buff, still, 8, posY, 8), color);

        tess.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    private void renderFluid3(FluidStack fluid, int capacity) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());

        float posY = 3 + (((float) fluid.amount / (float) capacity) * 11f);
        float[] color = FluidHelper.getColorRGBA(fluid.getFluid().getColor(fluid));
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(770, 771);
        buff.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX_COLOR);

        colEnd(posTex(buff, still, 8, posY, 8), color);

        tess.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }
}
