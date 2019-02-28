package com.teamacronymcoders.survivalism.client.render;

import com.teamacronymcoders.survivalism.common.tiles.TileDryingRack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class DryingRackTESR extends TileEntitySpecialRenderer<TileDryingRack> {

    @Override
    public void render(TileDryingRack te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();
        renderItem(te);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderItem(TileDryingRack te) {
        ItemStack stack = te.getStack();
        if (!stack.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.translate(.5, .3, .5);
            GlStateManager.scale(.4f, .4f, .4f);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }
}
