package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSealable extends SlotItemHandler {
    private TileBarrelBase te;
    private Item brewing = Item.getItemFromBlock(ModBlocks.brewing);
    private Item soaking = Item.getItemFromBlock(ModBlocks.soaking);
    private Item storage = Item.getItemFromBlock(ModBlocks.storage);

    public SlotSealable(IItemHandler itemHandler, int index, int xPosition, int yPosition, TileBarrelBase te) {
        super(itemHandler, index, xPosition, yPosition);
        this.te = te;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.getItem() == brewing || stack.getItem() == soaking || stack.getItem() == storage) {
            return false;
        }

        if (te.isSealed()) {
            return false;
        }

        if (te instanceof TileBarrelSoaking && getSlotIndex() == 1) {
            return false;
        }

        return super.isItemValid(stack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !te.isSealed() && super.canTakeStack(playerIn);
    }
}
