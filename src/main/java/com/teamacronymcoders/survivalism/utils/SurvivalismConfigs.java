package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SurvivalismConfigs {
    public static boolean crtVerboseLogging;
    public static double baseJumpValue;
    public static boolean timeOrTicks;
    public static int rainFillRate;
    public static boolean canBarrelsFillInRain;

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
        configRegistry.addEntry(crtVerboseLogging);
        configRegistry.addEntry(baseJumpValue);
        configRegistry.addEntry(timeOrTicks);
        configRegistry.addEntry(rainFillRate);
        configRegistry.addEntry(canBarrelsFillInRain);
    }

    private static void getValues() {
        baseJumpValue = configRegistry.getDouble("baseJumpValue", 1.0);
        crtVerboseLogging = configRegistry.getBoolean("crtVerboseLogging", false);
        timeOrTicks = configRegistry.getBoolean("timeOrTicks", false);
        rainFillRate = configRegistry.getInt("rainFillRate", 5);
        canBarrelsFillInRain = configRegistry.getBoolean("canBarrelsFillInRain", true);
    }
}
