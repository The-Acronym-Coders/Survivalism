package com.teamacronymcoders.survivalism.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class UpdatingItemStackHandler extends ItemStackHandler {

    private IUpdatingInventory inv;

    public UpdatingItemStackHandler(IUpdatingInventory inv) {
        this.inv = inv;
    }

    public UpdatingItemStackHandler(int size, IUpdatingInventory inv) {
        super(size);
        this.inv = inv;
    }

    public UpdatingItemStackHandler(NonNullList<ItemStack> stacks, IUpdatingInventory inv) {
        super(stacks);
        this.inv = inv;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        inv.updateSlot(slot, getStackInSlot(slot));
    }
}
