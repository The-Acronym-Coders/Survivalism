package com.teamacronymcoders.survivalism.common.tiles;

import net.minecraft.item.ItemStack;

public interface IUpdatingInventory {
    
    /**
     * Method called when a slot is updated
     * @param slot Slot being updated
     * @param stack stack being updated
     */
    void updateSlot(int slot, ItemStack stack);
}
