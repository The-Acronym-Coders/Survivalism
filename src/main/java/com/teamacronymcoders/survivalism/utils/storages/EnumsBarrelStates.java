package com.teamacronymcoders.survivalism.utils.storages;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumsBarrelStates implements IStringSerializable {
    STORAGE,
    BREWING,
    SOAKING;

    public static final EnumsBarrelStates[] VALUES = values();

    @Override
    public String getName() {
        return this.toString().toLowerCase(Locale.US);
    }
}
