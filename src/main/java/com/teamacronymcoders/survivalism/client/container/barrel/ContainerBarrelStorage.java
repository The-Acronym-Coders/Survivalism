package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBarrelStorage extends ContainerBarrel {

    private TileBarrel tile;

    public ContainerBarrelStorage(IInventory playerInv, TileBarrel tile) {
        this.tile = tile;
        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlotToContainer(new Slot(playerInv, (col + row) * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 72 + 70;
            this.addSlotToContainer(new Slot(playerInv, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.tile.getItemHandler();
        int x = 9;
        int y = 6;

        addSlotToContainer(new SlotItemHandler(itemHandler, 0, 62, 17));
        addSlotToContainer(new SlotItemHandler(itemHandler, 1, 80, 17));
        addSlotToContainer(new SlotItemHandler(itemHandler, 2, 98, 17));
        addSlotToContainer(new SlotItemHandler(itemHandler, 3, 62, 35));
        addSlotToContainer(new SlotItemHandler(itemHandler, 4, 80, 35));
        addSlotToContainer(new SlotItemHandler(itemHandler, 5, 98, 35));
        addSlotToContainer(new SlotItemHandler(itemHandler, 6, 62, 53));
        addSlotToContainer(new SlotItemHandler(itemHandler, 7, 80, 53));
        addSlotToContainer(new SlotItemHandler(itemHandler, 8, 98, 53));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tile.canInteractWith(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileBarrel.STORAGE_SIZE) {
                if (!this.mergeItemStack(itemstack1, TileBarrel.STORAGE_SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileBarrel.STORAGE_SIZE, false)) {
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
