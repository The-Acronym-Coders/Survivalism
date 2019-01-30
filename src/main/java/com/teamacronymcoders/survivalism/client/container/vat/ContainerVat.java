package com.teamacronymcoders.survivalism.client.container.vat;

import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerVat extends Container {

    private TileCrushingVat tile;

    public ContainerVat(IInventory playerInv, TileCrushingVat tile) {
        this.tile = tile;
        addPlayerSlots(playerInv);
        addOwnSlots();
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInv, x, 8 + (x * 18), 142));
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + (x * 18), 84 + (y * 18)));
            }
        }
    }

    private void addOwnSlots() {
        IItemHandler inputHandler = this.tile.getInputItemHandler();
        IItemHandler outputHandler = this.tile.getOutputItemHandler();
        addSlotToContainer(new SlotItemHandler(inputHandler, 0, 44, 40));
        addSlotToContainer(new SlotItemHandler(outputHandler, 0, 116, 40));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tile.canInteractWith(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        //TODO change this to check if the slot is enabled / disabled
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 2) {
                if (!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}
