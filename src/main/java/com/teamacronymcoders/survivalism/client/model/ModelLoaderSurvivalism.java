//package com.teamacronymcoders.survivalism.client.model;
//
//import net.minecraft.client.resources.IResourceManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.model.ICustomModelLoader;
//import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.ModelLoaderRegistry;
//
//import java.util.Objects;
//
//import static com.teamacronymcoders.survivalism.Survivalism.MODID;
//
//public class ModelLoaderSurvivalism implements ICustomModelLoader {
//    private IModel highPolyBarrel;
//    private IModel lowPolyBarrel;
//    private IModel highPolyBarrelSealed;
//    private IModel lowPolyBarrelSealed;
//    private IModel highPolyCrushingVat;
//    private IModel lowPolyCrushingVat;
//
//    @Override
//    public void onResourceManagerReload(IResourceManager resourceManager) {
//        highPolyBarrel = null;
//        lowPolyBarrel = null;
//        highPolyBarrelSealed = null;
//        lowPolyBarrelSealed = null;
//        highPolyCrushingVat = null;
//        lowPolyCrushingVat = null;
//    }
//
//    @Override
//    public boolean accepts(ResourceLocation modelLocation) {
//        return modelLocation.getResourceDomain().equalsIgnoreCase(MODID) && (modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("barrel") || modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("crushing_vat") || modelLocation.getResourcePath().replace("models/", "").equalsIgnoreCase("barrel_sealed"));
//    }
//
//    @Override
//    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
//        if (highPolyBarrel == null || lowPolyBarrel == null) {
//            if (Objects.equals(modelLocation.getResourcePath(), "barrel")) {
//                highPolyBarrel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_barrel"));
//                lowPolyBarrel = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_barrel"));
//                return new ModelSurvivalism(highPolyBarrelSealed, lowPolyBarrelSealed);
//            }
//        }
//
//        if (highPolyBarrelSealed == null || lowPolyBarrelSealed == null) {
//            if (Objects.equals(modelLocation.getResourcePath(), "barrel_sealed")) {
//                highPolyBarrelSealed = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_barrel_sealed"));
//                lowPolyBarrelSealed = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_barrel_sealed"));
//                return new ModelSurvivalism(highPolyBarrelSealed, lowPolyBarrelSealed);
//            }
//        }
//
//        if (highPolyCrushingVat == null || lowPolyCrushingVat == null) {
//            if (Objects.equals(modelLocation.getResourcePath(), "crushing_vat")) {
//                highPolyCrushingVat = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/smooth_crushing_vat"));
//                lowPolyCrushingVat = ModelLoaderRegistry.getModel(new ResourceLocation(MODID, "models/chunky_crushing_vat"));
//                return new ModelSurvivalism(highPolyCrushingVat, lowPolyCrushingVat);
//            }
//        }
//        return new ModelSurvivalism(highPolyBarrel, lowPolyBarrel);
//    }
//}
