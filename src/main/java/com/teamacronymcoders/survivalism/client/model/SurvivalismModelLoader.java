package com.teamacronymcoders.survivalism.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.Objects;

import static com.teamacronymcoders.survivalism.Survivalism.MODID;
import static com.teamacronymcoders.survivalism.utils.SurvivalismConfigs.blastProcessing;

public class SurvivalismModelLoader implements ICustomModelLoader {
    private IModel baseModel;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        baseModel = null;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equalsIgnoreCase(MODID) && modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("cmodels");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        if (baseModel == null) {
            if (Objects.equals(modelLocation.getResourcePath(), "barrel")) {
                if (blastProcessing) {
                    baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_barrel"));
                } else {
                    baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_barrel"));
                }
            } else if (Objects.equals(modelLocation.getResourcePath(), "barrel_sealed")) {
                if (blastProcessing) {
                    baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_barrel_sealed"));
                } else {
                    baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_barrel_sealed"));
                }
            } else if (Objects.equals(modelLocation.getResourcePath(), "crushing_vat")) {
                if (blastProcessing) {
                    baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_crushing_vat"));
                } else {
                    baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_crushing_vat"));
                }
            }
        }
        return new CrushingVatModel(baseModel);
    }
}
