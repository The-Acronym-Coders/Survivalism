package com.teamacronymcoders.survivalism.utils.storages;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BarrelState implements IStringSerializable {
    STORAGE(1, 9),
    BREWING(2, 3),
    SOAKING(0, 2);

    public static final BarrelState[] VALUES = values();
    int next;
    int invSize;

    BarrelState(int next, int invSize) {
        this.next = next;
        this.invSize = invSize;
    }

    @Override
    public String getName() {
        return this.toString().toLowerCase(Locale.ROOT);
    }

    public int getInvSize() {
        return invSize;
    }

    public BarrelState next() {
        return VALUES[next];
    }

}
