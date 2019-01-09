package com.teamacronymcoders.survivalism.client.container.barrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.*;

import javax.annotation.Nonnull;

public class SlotDisabled extends SlotItemHandler {
    
    public SlotDisabled(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }
    
    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean isEnabled() {
        return false;
    }
}
