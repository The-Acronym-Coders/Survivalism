package com.teamacronymcoders.survivalism.utils.storages;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum StorageEnumsBarrelStates implements IStringSerializable {
    STORAGE, BREWING, SOAKING;

    public static final StorageEnumsBarrelStates[] VALUES = values();

    @Override
    public String getName() {
        return this.toString().toLowerCase(Locale.US);
    }

    public StorageEnumsBarrelStates cycle() {
        int i = this.ordinal();
        if (i + 1 < StorageEnumsBarrelStates.values().length) {
            i++;
        } else {
            i = 0;
        }
        return StorageEnumsBarrelStates.values()[i];
    }
}
