package com.teamacronymcoders.survivalism.client.model;

import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
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
    private final IBakedModel highPolyBaked;
    private final IBakedModel lowPolyBaked;
    private final Function<ResourceLocation, TextureAtlasSprite> spriteFunction;

    public BakedModelSurvivalism(IBakedModel highPolyBaked, IBakedModel lowPolyBaked, Function<ResourceLocation, TextureAtlasSprite> spriteFunction1) {
        this.highPolyBaked = highPolyBaked;
        this.lowPolyBaked = lowPolyBaked;
        this.spriteFunction = spriteFunction1;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null) {
            return highPolyBaked.getQuads(state, side, rand);
        } else {
            return lowPolyBaked != null ? lowPolyBaked.getQuads(state, side, rand) : null;
        }
    }

    @Override
    public boolean isAmbientOcclusion() {
        if (SurvivalismConfigs.blastProcessing) {
            return highPolyBaked != null && highPolyBaked.isAmbientOcclusion();
        } else {
            return lowPolyBaked != null && lowPolyBaked.isAmbientOcclusion();
        }
    }

    @Override
    public boolean isGui3d() {
        if (SurvivalismConfigs.blastProcessing) {
            return highPolyBaked != null && highPolyBaked.isGui3d();
        } else {
            return lowPolyBaked != null && lowPolyBaked.isGui3d();
        }
    }

    @Override
    public boolean isBuiltInRenderer() {
        if (SurvivalismConfigs.blastProcessing) {
            return highPolyBaked != null && highPolyBaked.isBuiltInRenderer();
        } else {
            return lowPolyBaked != null && lowPolyBaked.isBuiltInRenderer();
        }
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        if (SurvivalismConfigs.blastProcessing) {
            return highPolyBaked != null ? highPolyBaked.getParticleTexture() : null;
        } else {
            return lowPolyBaked != null ? lowPolyBaked.getParticleTexture() : null;
        }
    }

    @Override
    public ItemOverrideList getOverrides() {
        if (SurvivalismConfigs.blastProcessing) {
            return highPolyBaked != null ? highPolyBaked.getOverrides() : null;
        } else {
            return lowPolyBaked != null ? lowPolyBaked.getOverrides() : null;
        }
    }
}
