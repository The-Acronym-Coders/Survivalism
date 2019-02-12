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

    private static ConfigRegistry configRegistry = Survivalism.INSTANCE.getRegistry(ConfigRegistry.class, "CONFIG");

    public static void preInitLoad(FMLPreInitializationEvent event) {
        loadConfigs();
        getValues();
    }


    private static void loadConfigs() {
        ConfigEntry crtVerboseLogging = new ConfigEntry("crafttweaker", "crtVerboseLogging", Property.Type.BOOLEAN, "false", "Enables Cleaner Verbose Logging for Crafttweaker Support", false);
        ConfigEntry baseJumpValue = new ConfigEntry("crushing_vat", "baseJumpValue", Property.Type.DOUBLE, "1.0", "Base Value per Jump for the Crushing Vat", false);
        ConfigEntry TimeOrTicks = new ConfigEntry("JEI", "TimeOrTicks", Property.Type.BOOLEAN, "false", "Set to true if it should display time in HH:MM:SS instead of Ticks?");
        configRegistry.addEntry(crtVerboseLogging);
        configRegistry.addEntry(baseJumpValue);
        configRegistry.addEntry(TimeOrTicks);
    }

    private static void getValues() {
        baseJumpValue = configRegistry.getDouble("baseJumpValue", 1.0);
        crtVerboseLogging = configRegistry.getBoolean("crtVerboseLogging", false);
        timeOrTicks = configRegistry.getBoolean("TimeOrTicks", false);
    }
}
