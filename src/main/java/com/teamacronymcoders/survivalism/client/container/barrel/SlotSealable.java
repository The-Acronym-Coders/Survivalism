package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotSealable extends SlotItemHandler {

    private TileEntity te;


    public SlotSealable(IItemHandler itemHandler, int index, int xPosition, int yPosition, TileEntity te) {
        super(itemHandler, index, xPosition, yPosition);
        this.te = te;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if (stack.getItem().equals(Item.getItemFromBlock(ModBlocks.blockBarrel))) {
            return false;
        }

        if (te instanceof TileBarrel) {
            TileBarrel barrel = (TileBarrel) te;
            if (barrel.checkSealedState(true)) {
                return false;
            }
            if (barrel.checkBarrelState(StorageEnumsBarrelStates.SOAKING)) {
                if (getSlotIndex() == 1) {
                    return false;
                }
            }
        }
        return super.isItemValid(stack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        if (te instanceof TileBarrel) {
            TileBarrel barrel = (TileBarrel) te;
            if (barrel.checkSealedState(true)) {
                return false;
            }
        }
        return super.canTakeStack(playerIn);
    }
}
