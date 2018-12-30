package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBarrelBrewing extends Container {

    private TileBarrel tile;

    public ContainerBarrelBrewing(IInventory playerInv, TileBarrel tile) {
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
        IItemHandler itemHandler = this.tile.getBrewingHandler();
        int x = 9;
        int y = 6;
        addSlotToContainer(new SlotItemHandler(itemHandler, 0, x, y));
        addSlotToContainer(new SlotItemHandler(itemHandler, 1, x, y * 2));
        addSlotToContainer(new SlotItemHandler(itemHandler, 2, x, y * 3));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tile.canInteractWith(playerIn);
    }

}
