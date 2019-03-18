package com.teamacronymcoders.survivalism.client.render;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
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

import java.util.ArrayList;
import java.util.List;

import static com.teamacronymcoders.survivalism.utils.helpers.RenderHelper.colEnd;
import static com.teamacronymcoders.survivalism.utils.helpers.RenderHelper.posTex;

public class CrushingTESR extends TileEntitySpecialRenderer<TileCrushingVat> {

    @Override
    public void render(TileCrushingVat te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderItem(te);
        if (te.getTank().getFluid() != null) {
            renderFluid(te);
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderItem(TileCrushingVat te) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(te.getInputInv().getStackInSlot(0));
        stacks.add(te.getOutputInv().getStackInSlot(0));

        if (!stacks.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.translate(.5, .325, .5);
            for (ItemStack stack : stacks) {
                if (stack.getItem() instanceof ItemBlock) {
                    GlStateManager.scale(.25f, .25f, .25f);
                    GlStateManager.rotate(360F / stacks.size(), 0f, 1f, 0f);
                } else {
                    GlStateManager.scale(.5f, .5f, .5f);
                    GlStateManager.rotate(270, 1f, 0f, 0f);
                }
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
            }

            GlStateManager.popMatrix();
        }
    }

    private void renderFluid(TileCrushingVat te) {
        GlStateManager.pushMatrix();
        int capacity = te.getTank().getCapacity();
        FluidStack fluid = te.getTank().getFluid();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());

        float posY = 3 + (((float) fluid.amount / (float) capacity) * 4f);
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
        GlStateManager.popMatrix();
    }
}

