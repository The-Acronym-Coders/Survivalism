package com.teamacronymcoders.survivalism.common.inventory;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;

public class BarrelHandler extends UpdatingItemStackHandler {
    int size;

    public BarrelHandler(int size, TileBarrelBase inv) {
        super(inv);
        this.size = size;
    }

    @Override
    public int getSlots() {
        return size;
    }
}
