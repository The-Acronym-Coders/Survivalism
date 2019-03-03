package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SurvivalismConfigs {
    //Crafttweaker
    public static boolean crtVerboseLogging;

    //JEI
    public static boolean timeOrTicks;

    // General Barrels
    public static int rainFillRate;
    public static boolean canBarrelsFillInRain;

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
    public static boolean doesDryingRackHaveModifiers;

    private static ConfigRegistry configRegistry = Survivalism.INSTANCE.getRegistry(ConfigRegistry.class, "CONFIG");

    public static void preInitLoad(FMLPreInitializationEvent event) {
        loadConfigs();
        getValues();
    }


    private static void loadConfigs() {
        ConfigEntry crtVerboseLogging = new ConfigEntry("crafttweaker", "crtVerboseLogging", Property.Type.BOOLEAN, "false", "Enables Cleaner Verbose Logging for Crafttweaker Support", false);
        ConfigEntry baseJumpValue = new ConfigEntry("crushing_vat", "baseJumpValue", Property.Type.DOUBLE, "1.0", "Base Value per Jump for the Crushing Vat", false);
        ConfigEntry timeOrTicks = new ConfigEntry("jei", "TimeOrTicks", Property.Type.BOOLEAN, "false", "Set to true if it should display time in HH:MM:SS instead of Ticks?");
        ConfigEntry rainFillRate = new ConfigEntry("barrel_general", "rainFillRate", Property.Type.INTEGER, "5", "The rate per tick which the barrel fills with water if it's raining and the barrel is left un-sealed to an open sky", false);
        ConfigEntry canBarrelsFillInRain = new ConfigEntry("barrel_general", "canBarrelsFillInRain", Property.Type.BOOLEAN, "true", "Decides if the barrel should fill if it's raining and left open to the sky", false);
        ConfigEntry brewingInputSize = new ConfigEntry("brewing_barrel", "brewingInputSize", Property.Type.INTEGER, "16000", "Brewing Input Tank Size", false);
        ConfigEntry brewingOutputSize = new ConfigEntry("brewing_barrel", "brewingOutputSize", Property.Type.INTEGER, "16000", "Brewing Output Tank Size", false);
        ConfigEntry soakingTankSize = new ConfigEntry("soaking_barrel", "soakingTankSize", Property.Type.INTEGER, "16000", "Soaking Tank Size", false);
        ConfigEntry storageTankSize = new ConfigEntry("storage_barrel", "storageTankSize", Property.Type.INTEGER, "32000", "Storage Tank Size", false);
        ConfigEntry doesDryingRackHaveModifiers = new ConfigEntry("drying_rack", "doesDryingRackHaveModifiers", Property.Type.BOOLEAN, "true", "Enable if you want the Drying Rack to check below it for a Modifying Block", false);
        ConfigEntry mixingInputTankSize = new ConfigEntry("mixing_vat", "mixingInputTankSize", Property.Type.INTEGER, "16000", "Mixing Vat Input Tank Size", false);
        ConfigEntry mixingSecondaryTankSize = new ConfigEntry("mixing_vat", "mixingSecondaryTankSize", Property.Type.INTEGER, "16000", "Mixing Vat Secondary Tank Size", false);
        ConfigEntry mixingOutputTankSize = new ConfigEntry("mixing_vat", "mixingOutputTankSize", Property.Type.INTEGER, "32000", "Mixing Vat Output Tank Size", false);
        ConfigEntry crushingTankSize = new ConfigEntry("crushing_vat", "crushingTankSize", Property.Type.INTEGER, "16000", "Crushing Tank Capacity", false);

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
        doesDryingRackHaveModifiers = configRegistry.getBoolean("doesDryingRackHaveModifiers", true);
        mixingInputTankSize = configRegistry.getInt("mixingInputTankSize", 16000);
        mixingSecondaryTankSize = configRegistry.getInt("mixingSecondaryTankSize", 16000);
        mixingOutputTankSize = configRegistry.getInt("mixingOutputTankSize", 32000);
        crushingTankSize = configRegistry.getInt("crushingTankSize", 16000);
    }
}
