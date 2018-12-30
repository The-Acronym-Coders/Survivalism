package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBarrelStorage extends Container {

    private TileBarrel tile;

    public ContainerBarrelStorage(IInventory playerInv, TileBarrel tile) {
        this.tile = tile;
        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18;
                int y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18;
            int y = 58 + 70;
            this.addSlotToContainer(new Slot(playerInv, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.tile.getStorageHandler();
        int x = 9;
        int y = 6;

        int slotIndex = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (slotIndex <= 3) {
                addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
                slotIndex++;
            } else if (slotIndex >= 4 && slotIndex <= 6) {
                addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y + 12));
                slotIndex++;
            } else if (slotIndex >= 7) {
                addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y + 18));
                slotIndex++;
            }
            x += 18;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tile.canInteractWith(playerIn);
    }


}
