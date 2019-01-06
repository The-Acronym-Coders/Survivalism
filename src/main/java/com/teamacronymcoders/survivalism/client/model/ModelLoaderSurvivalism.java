package com.teamacronymcoders.survivalism.client.model;

import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.Objects;

import static com.teamacronymcoders.survivalism.Survivalism.MODID;
import static com.teamacronymcoders.survivalism.utils.SurvivalismConfigs.blastProcessing;

public class ModelLoaderSurvivalism implements ICustomModelLoader {
    private IModel highPolyBarrel;
    private IModel lowPolyBarrel;
    private IModel highPolyBarrelSealed;
    private IModel lowPolyBarrelSealed;
    private IModel highPolyCrushingVat;
    private IModel lowPolyCrushingVat;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        highPoly = null;
        lowPoly = null;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equalsIgnoreCase(MODID) && (modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("barrel") || modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("crushing_vat") || modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("barrel_sealed"));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        if (highPoly == null || lowPoly == null) {
            if (Objects.equals(modelLocation.getResourcePath(), "barrel")) {
                highPoly = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_barrel"));
                lowPoly = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_barrel"));
            } else if (Objects.equals(modelLocation.getResourcePath(), "barrel_sealed")) {
                highPoly = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_barrel_sealed"));
                lowPoly = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_barrel_sealed"));
            } else if (Objects.equals(modelLocation.getResourcePath(), "crushing_vat")) {
                highPoly = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_crushing_vat"));
                lowPoly = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_crushing_vat"));
            }
        }
        return new ModelSurvivalism(highPoly, lowPoly);
    }
}
