package com.teamacronymcoders.survivalism.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class BakedModelSurvivalism implements IBakedModel {
    private final IBakedModel bakedModel;
    private final Function<ResourceLocation, TextureAtlasSprite> spriteFunction;

    public BakedModelSurvivalism(IBakedModel model, Function<ResourceLocation, TextureAtlasSprite> spriteFunction1) {
        bakedModel = model;
        spriteFunction = spriteFunction1;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> bakedQuads = bakedModel.getQuads(state, side, rand);
        return bakedQuads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return bakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return bakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return bakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return bakedModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return bakedModel.getOverrides();
    }
}
