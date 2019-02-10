package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.BarrelState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSealable extends SlotItemHandler {

    @ObjectHolder("survivalism:barrel")
    public static final Item BARREL = null;
    private TileBarrel te;

    public SlotSealable(IItemHandler itemHandler, int index, int xPosition, int yPosition, TileBarrel te) {
        super(itemHandler, index, xPosition, yPosition);
        this.te = te;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.getItem() == BARREL) {
            return false;
        }
        if (te.isSealed()) {
            return false;
        }
        if (te.getState() == BarrelState.SOAKING && getSlotIndex() == 1) {
            return false;
        }
        return super.isItemValid(stack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !te.isSealed() && super.canTakeStack(playerIn);
    }
}
