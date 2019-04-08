package com.teamacronymcoders.survivalism.utils.helpers;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class RenderHelper {

    public static float s(float num) {
        return num / 16f;
    }

    public static BufferBuilder posTex(BufferBuilder buff, TextureAtlasSprite sprite, float x, float y, float z) {
        return buff.pos(s(x), s(y), s(z)).tex(sprite.getInterpolatedU(x), sprite.getInterpolatedV(z));
    }

    public static void colEnd(BufferBuilder buff, float[] color) {
        buff.color(color[0], color[1], color[2], color[3]).endVertex();
    }

}
