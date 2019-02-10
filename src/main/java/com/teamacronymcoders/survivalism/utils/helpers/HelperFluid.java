package com.teamacronymcoders.survivalism.utils.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class HelperFluid {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public static FluidStack getFluidStackFromHandler(ItemStack fluidContainer) {
        if (isFluidHandler(fluidContainer)) {
            IFluidTankProperties[] props = Objects.requireNonNull(fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)).getTankProperties();
            return props.length <= 0 ? null : props[0].getContents();
        }
        return null;
    }

    public static FluidStack getFluidStackFromHandler(TileEntity fluidTile) {
        if (isFluidHandler(fluidTile)) {
            IFluidTankProperties[] props = Objects.requireNonNull(fluidTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)).getTankProperties();
            return props.length <= 0 ? null : props[0].getContents();
        }
        return null;
    }

    private static boolean isFluidHandler(ItemStack stack) {
        return !stack.isEmpty() & stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    }

    private static boolean isFluidHandler(TileEntity fluidTile) {
        return fluidTile != null & Objects.requireNonNull(fluidTile).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }

    public static boolean isFluidStacksSame(FluidStack stack1, FluidStack stack2) {
        return stack1.getFluid() == stack2.getFluid();
    }


    /**
     * The below 8 Methods are taken from Tinker's Construct with full attribution.
     * <p>
     * Disclaimer:
     * Some alterations has been made to make this code function properly with my enviroment.
     */
    public static void renderTiledFluid(int x, int y, int width, int height, float depth, FluidStack fluidStack) {
        if (fluidStack == null) {
            return;
        }
        TextureAtlasSprite fluidSprite = mc.getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
        setColorRGBA(fluidStack.getFluid().getColor(fluidStack));
        renderTiledTextureAtlas(x, y, width, height, depth, fluidSprite, fluidStack.getFluid().isGaseous(fluidStack));
    }

    private static void setColorRGBA(int color) {
        float a = alpha(color) / 255.0F;
        float r = red(color) / 255.0F;
        float g = green(color) / 255.0F;
        float b = blue(color) / 255.0F;
        GlStateManager.color(r, g, b, a);
    }

    private static void renderTiledTextureAtlas(int x, int y, int width, int height, float depth, TextureAtlasSprite sprite, boolean upsideDown) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        putTiledTextureQuads(renderer, x, y, width, height, depth, sprite, upsideDown);
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }

    private static void putTiledTextureQuads(BufferBuilder renderer, int x, int y, int width, int height, float depth, TextureAtlasSprite sprite, boolean upsideDown) {
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();

        // tile vertically
        do {
            int renderHeight = Math.min(sprite.getIconHeight(), height);
            height -= renderHeight;

            float v2 = sprite.getInterpolatedV((16f * renderHeight) / (float) sprite.getIconHeight());

            // we need to draw the quads per width too
            int x2 = x;
            int width2 = width;
            // tile horizontally
            do {
                int renderWidth = Math.min(sprite.getIconWidth(), width2);
                width2 -= renderWidth;

                float u2 = sprite.getInterpolatedU((16f * renderWidth) / (float) sprite.getIconWidth());

                if (upsideDown) {
                    renderer.pos(x2, y, depth).tex(u2, v1).endVertex();
                    renderer.pos(x2, y + renderHeight, depth).tex(u2, v2).endVertex();
                    renderer.pos(x2 + renderWidth, y + renderHeight, depth).tex(u1, v2).endVertex();
                    renderer.pos(x2 + renderWidth, y, depth).tex(u1, v1).endVertex();
                } else {
                    renderer.pos(x2, y, depth).tex(u1, v1).endVertex();
                    renderer.pos(x2, y + renderHeight, depth).tex(u1, v2).endVertex();
                    renderer.pos(x2 + renderWidth, y + renderHeight, depth).tex(u2, v2).endVertex();
                    renderer.pos(x2 + renderWidth, y, depth).tex(u2, v1).endVertex();
                }

                x2 += renderWidth;
            } while (width2 > 0);

            y += renderHeight;
        } while (height > 0);
    }

    private static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    private static int red(int c) {
        return (c >> 16) & 0xFF;
    }

    private static int green(int c) {
        return (c >> 8) & 0xFF;
    }

    private static int blue(int c) {
        return (c) & 0xFF;
    }

}
