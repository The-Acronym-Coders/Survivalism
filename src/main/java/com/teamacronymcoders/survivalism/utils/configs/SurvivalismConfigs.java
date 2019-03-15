package com.teamacronymcoders.survivalism.utils.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.base.util.files.BaseFileUtils;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.barrel.json.BiomeToFluid;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SurvivalismConfigs {
    private static final List<BiomeToFluid> DEFAULT_PAIRINGS = new ArrayList<>();

    //Crafttweaker
    public static boolean crtVerboseLogging;

    //JEI
    public static boolean timeOrTicks;

    // General Barrels
    public static int rainFillRate;
    public static boolean canBarrelsFillInRain;
    public static boolean shouldBarrelsRespectRainValueOfBiomes;
    public static int potionToBottleDrainAmount;

    // Brewing
    public static int brewingInputSize;
    public static int brewingOutputSize;

    // Soaking
    public static int soakingTankSize;

    // Storage
    public static int storageTankSize;

    // Crushing Vat
    public static double baseJumpValue;
    public static int crushingTankSize;

    // Mixing Vat
    public static int mixingInputTankSize;
    public static int mixingSecondaryTankSize;
    public static int mixingOutputTankSize;

    // Drying Rack
    public static boolean shouldDryingRackUseModifiers;

    private static ConfigRegistry configRegistry = Survivalism.INSTANCE.getRegistry(ConfigRegistry.class, "CONFIG");

    static {
        DEFAULT_PAIRINGS.add(new BiomeToFluid("minecraft:plains", "water", 5));
        DEFAULT_PAIRINGS.add(new BiomeToFluid("minecraft:savanna", "lava", 5));
    }

    public static void preInitLoad(FMLPreInitializationEvent event) {
        loadConfigs();
        getValues();
        //initBiomeFluidJSON(new File(configRegistry.getConfigFolder().getPath(), "jsons"));
    }

    private static void loadConfigs() {
        ConfigEntry crtVerboseLogging = new ConfigEntry("crafttweaker", "crtVerboseLogging", Property.Type.BOOLEAN, "false", "Enables Cleaner Verbose Logging for Crafttweaker Support", false);
        ConfigEntry baseJumpValue = new ConfigEntry("crushing_vat", "baseJumpValue", Property.Type.DOUBLE, "1.0", "Base Value per Jump for the Crushing Vat", true);
        ConfigEntry timeOrTicks = new ConfigEntry("jei", "TimeOrTicks", Property.Type.BOOLEAN, "false", "Set to true if it should display time in HH:MM:SS instead of Ticks?", false);
        ConfigEntry rainFillRate = new ConfigEntry("barrel_general", "rainFillRate", Property.Type.INTEGER, "5", "The rate per tick which the barrel fills with water if it's raining and the barrel is left un-sealed to an open sky", false);
        ConfigEntry canBarrelsFillInRain = new ConfigEntry("barrel_general", "canBarrelsFillInRain", Property.Type.BOOLEAN, "true", "Decides if the barrel should fill if it's raining and left open to the sky", true);
        ConfigEntry brewingInputSize = new ConfigEntry("brewing_barrel", "brewingInputSize", Property.Type.INTEGER, "16000", "Brewing Input Tank Size", true);
        ConfigEntry brewingOutputSize = new ConfigEntry("brewing_barrel", "brewingOutputSize", Property.Type.INTEGER, "16000", "Brewing Output Tank Size", true);
        ConfigEntry soakingTankSize = new ConfigEntry("soaking_barrel", "soakingTankSize", Property.Type.INTEGER, "16000", "Soaking Tank Size", true);
        ConfigEntry storageTankSize = new ConfigEntry("storage_barrel", "storageTankSize", Property.Type.INTEGER, "32000", "Storage Tank Size", true);
        ConfigEntry doesDryingRackHaveModifiers = new ConfigEntry("drying_rack", "shouldDryingRackUseModifiers", Property.Type.BOOLEAN, "true", "Enable if you want the Drying Rack to check below it for a Modifying Block", true);
        ConfigEntry mixingInputTankSize = new ConfigEntry("mixing_vat", "mixingInputTankSize", Property.Type.INTEGER, "16000", "Mixing Vat Input Tank Size", true);
        ConfigEntry mixingSecondaryTankSize = new ConfigEntry("mixing_vat", "mixingSecondaryTankSize", Property.Type.INTEGER, "16000", "Mixing Vat Secondary Tank Size", true);
        ConfigEntry mixingOutputTankSize = new ConfigEntry("mixing_vat", "mixingOutputTankSize", Property.Type.INTEGER, "32000", "Mixing Vat Output Tank Size", true);
        ConfigEntry crushingTankSize = new ConfigEntry("crushing_vat", "crushingTankSize", Property.Type.INTEGER, "16000", "Crushing Tank Capacity", true);
        ConfigEntry shouldBarrelsRespectRainValueOfBiomes = new ConfigEntry("barrel_general", "shouldBarrelsRespectRainValueOfBiomes", Property.Type.BOOLEAN, "true", "Should barrels respect that some biomes don't support rain?", true);
        ConfigEntry potionToBottleDrainAmount = new ConfigEntry("barrel_general", "potionToBottleDrainAmount", Property.Type.INTEGER, "250", "Amount of Potion Fluid to Drain per Bottle", true);

        configRegistry.addEntry(crtVerboseLogging);
        configRegistry.addEntry(baseJumpValue);
        configRegistry.addEntry(timeOrTicks);
        configRegistry.addEntry(rainFillRate);
        configRegistry.addEntry(canBarrelsFillInRain);
        configRegistry.addEntry(brewingInputSize);
        configRegistry.addEntry(brewingOutputSize);
        configRegistry.addEntry(soakingTankSize);
        configRegistry.addEntry(storageTankSize);
        configRegistry.addEntry(doesDryingRackHaveModifiers);
        configRegistry.addEntry(mixingInputTankSize);
        configRegistry.addEntry(mixingSecondaryTankSize);
        configRegistry.addEntry(mixingOutputTankSize);
        configRegistry.addEntry(crushingTankSize);
        configRegistry.addEntry(shouldBarrelsRespectRainValueOfBiomes);
        configRegistry.addEntry(potionToBottleDrainAmount);
    }

    private static void getValues() {
        baseJumpValue = configRegistry.getDouble("baseJumpValue", 1.0);
        crtVerboseLogging = configRegistry.getBoolean("crtVerboseLogging", false);
        timeOrTicks = configRegistry.getBoolean("timeOrTicks", false);
        rainFillRate = configRegistry.getInt("rainFillRate", 5);
        canBarrelsFillInRain = configRegistry.getBoolean("canBarrelsFillInRain", true);
        brewingInputSize = configRegistry.getInt("brewingInputSize", 16000);
        brewingOutputSize = configRegistry.getInt("brewingOutputSize", 16000);
        soakingTankSize = configRegistry.getInt("soakingTankSize", 16000);
        storageTankSize = configRegistry.getInt("storageTankSize", 32000);
        shouldDryingRackUseModifiers = configRegistry.getBoolean("shouldDryingRackUseModifiers", true);
        mixingInputTankSize = configRegistry.getInt("mixingInputTankSize", 16000);
        mixingSecondaryTankSize = configRegistry.getInt("mixingSecondaryTankSize", 16000);
        mixingOutputTankSize = configRegistry.getInt("mixingOutputTankSize", 32000);
        crushingTankSize = configRegistry.getInt("crushingTankSize", 16000);
        shouldBarrelsRespectRainValueOfBiomes = configRegistry.getBoolean("shouldBarrelsRespectRainValueOfBiomes", true);
        potionToBottleDrainAmount = configRegistry.getInt("potionToBottleDrainAmount", 250);
    }

    private static void initBiomeFluidJSON(File file) {
        if (!file.exists()) {
            BaseFileUtils.createFolder(file);
        }
        File json = new File(file.getPath(), "biomesfluids.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!json.exists()) {
            BaseFileUtils.writeStringToFile(gson.toJson(DEFAULT_PAIRINGS), json);
        } else {
            //List<BiomeToFluid> values = gson.fromJson(BaseFileUtils.readFileToString(json), );
            //for (BiomeToFluid btf : values) {
            //    btf.register();
            //}
        }
    }

    @Mod.EventBusSubscriber(modid = Survivalism.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Survivalism.MODID)) {
                ConfigManager.sync(Survivalism.MODID, Config.Type.INSTANCE);
            }
        }
    }


}
