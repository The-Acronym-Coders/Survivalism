package com.teamacronymcoders.survivalism.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class SoakingWrapper implements IItemHandler {

    UpdatingItemStackHandler handler;

    public SoakingWrapper (UpdatingItemStackHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == 0) {
            return handler.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0) {
            return ItemStack.EMPTY;
        }
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }
}
