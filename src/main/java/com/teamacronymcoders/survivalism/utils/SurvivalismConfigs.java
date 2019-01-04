package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SurvivalismConfigs {

    public static boolean blastProcessing;

    private static ConfigRegistry configRegistry = Survivalism.INSTANCE.getRegistry(ConfigRegistry.class, "CONFIG");

    public static void preInitLoad(FMLPreInitializationEvent event) {
        loadConfigs();
        getValues();
    }

    private static void loadConfigs() {
        ConfigEntry blastProcessingEntry = new ConfigEntry("general", "enableBlastProcessing", Property.Type.BOOLEAN, "false", "Enable Blast Processing", false);
        configRegistry.addEntry(blastProcessingEntry);
    }

    private static void getValues() {
        blastProcessing = configRegistry.getBoolean("enableBlastProcessing", false);
    }
}
