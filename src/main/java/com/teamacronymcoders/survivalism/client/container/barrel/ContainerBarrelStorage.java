package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerBarrelStorage extends ContainerBarrel {

    public ContainerBarrelStorage(InventoryPlayer playerInv, TileBarrelStorage tile) {
        super(playerInv.player, tile);
        addOwnSlots();
        addPlayerSlots(playerInv);
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
        TileBarrelStorage storage = (TileBarrelStorage) this.tile;
        IItemHandler itemHandler = storage.getInv();
        addSlotToContainer(new SlotSealable(itemHandler, 0, 62, 17, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 1, 80, 17, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 2, 98, 17, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 3, 62, 35, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 4, 80, 35, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 5, 98, 35, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 6, 62, 53, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 7, 80, 53, tile));
        addSlotToContainer(new SlotSealable(itemHandler, 8, 98, 53, tile));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 9) {
                if (!this.mergeItemStack(itemstack1, 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
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
