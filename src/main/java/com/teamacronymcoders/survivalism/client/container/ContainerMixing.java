package com.teamacronymcoders.survivalism.client.container;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMixing extends Container {
    boolean firstSend = false;
    private TileMixingVat tile;
    private FluidStack main;
    private FluidStack secondary;
    private FluidStack output;
    private EntityPlayer player;

    public ContainerMixing(InventoryPlayer playerInv, TileMixingVat tile) {
        this.tile = tile;
        addPlayerSlots(playerInv);
        addOwnSlots();
        this.player = playerInv.player;
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
        addSlotToContainer(new SlotItemHandler(tile.getHandler(), 0, 80, 40));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(tile.getPos()) < 64;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (tile.getWorld().isRemote) {
            return;
        }
        if (!firstSend) {
            firstSend = true;
            main = tile.getMain().getFluid();
            secondary = tile.getSecondary().getFluid();
            output = tile.getOutput().getFluid();
            sendMessage();
        }
        main = checkFluid(main, tile.getMain().getFluid());
        secondary = checkFluid(secondary, tile.getSecondary().getFluid());
        output = checkFluid(output, tile.getOutput().getFluid());
    }

    private FluidStack checkFluid(FluidStack stack, FluidStack tank) {
        boolean sendUpdate = false;
        if (stack == null && tank == null) {
            return null;
        }
        if (stack == null) {
            sendUpdate = true;
        }
        if (stack != null && tank == null) {
            sendUpdate = true;
        }
        if (tank != null && stack != null) {
            if (!tank.getFluid().getName().equals(stack.getFluid().getName())) {
                sendUpdate = true;
            }
            if (tank.amount != stack.amount) {
                sendUpdate = true;
            }
        }
        if (sendUpdate) {
            sendMessage();
        }
        return tank == null ? null : tank.copy();
    }

    private void sendMessage() {
        tile.markDirty();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stackC = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stackO = slot.getStack();
            stackC = stackO.copy();
            if (index == 0) {
                if (!this.mergeItemStack(stackO, 0, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stackO, 0, this.inventorySlots.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (stackO.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stackC;
    }
}
