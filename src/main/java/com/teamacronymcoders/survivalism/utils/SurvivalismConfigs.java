package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SurvivalismConfigs {
    public static double baseJumpValue;

    private static ConfigRegistry configRegistry = Survivalism.INSTANCE.getRegistry(ConfigRegistry.class, "CONFIG");

    public static void preInitLoad(FMLPreInitializationEvent event) {
        loadConfigs();
        getValues();
    }

    private static void loadConfigs() {
        ConfigEntry baseJumpValue = new ConfigEntry("crushing_vat", "baseJumpValue", Property.Type.DOUBLE, "1.0", "Base Value per Jump for the Crushing Vat", false);
        configRegistry.addEntry(baseJumpValue);
    }

    private static void getValues() {
        baseJumpValue = configRegistry.getDouble("baseJumpValue", 1.0);
    }
}
