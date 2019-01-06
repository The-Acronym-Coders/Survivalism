package com.teamacronymcoders.survivalism.client.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelSurvivalism implements IModel {
    private final IModel highPoly;
    private final IModel lowPoly;
    private IBakedModel lowPolyBakedModel = null;
    private IBakedModel highPolyBakedModel = null;

    public ModelSurvivalism(IModel highPoly, IModel lowPoly) {
        this.highPoly = highPoly;
        this.lowPoly = lowPoly;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        if (highPoly == null) {
            highPolyBakedModel = highPoly.bake(state, format, bakedTextureGetter);
        }

        if (lowPoly == null) {
            lowPolyBakedModel = lowPoly.bake(state, format, bakedTextureGetter);
        }
        return new BakedModelSurvivalism(highPolyBakedModel, lowPolyBakedModel, bakedTextureGetter);
    }
}
