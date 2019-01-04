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
public class CrushingVatModel implements IModel {
    private final IModel crushingVatModel;
    private IBakedModel crushingVatBakedModel = null;

    public CrushingVatModel(IModel crushingVatModel) {
        this.crushingVatModel = crushingVatModel;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        if (crushingVatBakedModel == null) {
            crushingVatBakedModel = crushingVatModel.bake(state, format, bakedTextureGetter);
        }
        return new BakedModelCrushingVat(crushingVatBakedModel, bakedTextureGetter);
    }
}
