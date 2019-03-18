package com.teamacronymcoders.survivalism.client.render;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.utils.helpers.HelperFluid;
import javafx.util.Pair;
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
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class BarrelTESR extends TileEntitySpecialRenderer<TileBarrelBase> {

    @Override
    public void render(TileBarrelBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();
        if (!te.isSealed()) {
            renderItem(te);
            renderFluid(te, x, y, z);
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderItem(TileBarrelBase te) {
        List<ItemStack> stacks = new ArrayList<>();

        if (te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            for (int i = 0; i < brewing.getInv().getSlots(); i++) {
                stacks.add(brewing.getInv().getStackInSlot(i));
            }
        } else if (te instanceof TileBarrelSoaking) {
            stacks.add(((TileBarrelSoaking) te).getInv().getStackInSlot(0));
        } else if (te instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            for (int i = 0; i < storage.getInv().getSlots(); i++) {
                stacks.add(storage.getInv().getStackInSlot(i));
            }
        }

        if (!stacks.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.translate(.5, .75, .5);
            GlStateManager.scale(.5f, .5f, .5f);
            for (ItemStack stack : stacks) {
                GlStateManager.rotate(360F/stacks.size(), 0f, 1f, 0f);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
            }

            GlStateManager.popMatrix();
        }
    }

    private void renderFluid(TileBarrelBase te, double x, double y, double z) {
        GlStateManager.pushMatrix();
        int capacity = 0;
        FluidStack fluid = null;

        if (te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            if (brewing.getOutput().getFluid() != null) {
                capacity = brewing.getOutput().getCapacity();
                fluid = brewing.getOutput().getFluid();
            } else {
                capacity = brewing.getInput().getCapacity();
                fluid = brewing.getInput().getFluid();
            }
        } else if (te instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) te;
            capacity = soaking.getInput().getCapacity();
            fluid = soaking.getInput().getFluid();
        } else if (te instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            capacity = storage.getInput().getCapacity();
            fluid = storage.getInput().getFluid();
        }

        if (fluid != null) {
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();

            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());
            TextureAtlasSprite flow =  Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getFlowing().toString());

            double posY = .1 + (.8 * ((float) fluid.amount / (float) capacity));
            float[] color = HelperFluid.getColorRGBA(fluid.getFluid().getColor(fluid));

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos( 4F/16F, posY,  4F/16F).tex(still.getInterpolatedU( 4F), still.getInterpolatedV( 4F)).color(color[0], color[1], color[2], color[3]).endVertex();
            buffer.pos(12F/16F, posY,  4F/16F).tex(still.getInterpolatedU(12F), still.getInterpolatedV( 4F)).color(color[0], color[1], color[2], color[3]).endVertex();
            buffer.pos(12F/16F, posY, 12F/16F).tex(still.getInterpolatedU(12F), still.getInterpolatedV(12F)).color(color[0], color[1], color[2], color[3]).endVertex();
            buffer.pos( 4F/16F, posY, 12F/16F).tex(still.getInterpolatedU( 4F), still.getInterpolatedV(12F)).color(color[0], color[1], color[2], color[3]).endVertex();
            tess.draw();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(12F/16F, 1F/16F, 12F/16F).tex(flow.getInterpolatedU(12F), flow.getInterpolatedV(15F)).color(color[0], color[1], color[2], color[3]).endVertex();
            buffer.pos(12F/16F, posY, 12F/16F).tex(flow.getInterpolatedU(12F), flow.getInterpolatedV( 1F)).color(color[0], color[1], color[2], color[3]).endVertex();
            buffer.pos( 4F/16F, posY, 12F/16F).tex(flow.getInterpolatedU( 4F), flow.getInterpolatedV( 1F)).color(color[0], color[1], color[2], color[3]).endVertex();
            buffer.pos( 4F/16F, 1F/16F, 12F/16F).tex(flow.getInterpolatedU( 4F), flow.getInterpolatedV(15F)).color(color[0], color[1], color[2], color[3]).endVertex();
            tess.draw();
            }
            GlStateManager.popMatrix();
    }
}
