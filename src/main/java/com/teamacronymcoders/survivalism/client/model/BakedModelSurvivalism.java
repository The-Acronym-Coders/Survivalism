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
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null && lowPolyBaked != null) {
            return highPolyBaked.getQuads(state, side, rand);
        } else {
            return lowPolyBaked.getQuads(state, side, rand);
        }
    }

    @Override
    public boolean isAmbientOcclusion() {
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null && lowPolyBaked != null) {
            return highPolyBaked.isAmbientOcclusion();
        } else {
            return lowPolyBaked.isAmbientOcclusion();
        }
    }

    @Override
    public boolean isGui3d() {
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null && lowPolyBaked != null) {
            return highPolyBaked.isGui3d();
        } else {
            return lowPolyBaked.isGui3d();
        }
    }

    @Override
    public boolean isBuiltInRenderer() {
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null && lowPolyBaked != null) {
            return highPolyBaked.isBuiltInRenderer();
        } else {
            return lowPolyBaked.isBuiltInRenderer();
        }
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null && lowPolyBaked != null) {
            return highPolyBaked.getParticleTexture();
        } else {
            return lowPolyBaked.getParticleTexture();
        }
    }

    @Override
    public ItemOverrideList getOverrides() {
        if (SurvivalismConfigs.blastProcessing && highPolyBaked != null && lowPolyBaked != null) {
            return highPolyBaked.getOverrides();
        } else {
            return lowPolyBaked.getOverrides();
        }
    }
}
