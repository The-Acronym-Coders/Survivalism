package com.teamacronymcoders.survivalism.client.container;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import com.teamacronymcoders.survivalism.utils.network.MessageUpdateCrushingVat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrushing extends Container {

    boolean firstSend = false;
    private TileCrushingVat tile;
    private FluidStack stack;
    private EntityPlayer player;

    public ContainerCrushing(InventoryPlayer playerInv, TileCrushingVat tile) {
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
        addSlotToContainer(new SlotItemHandler(tile.getInputInv(), 0, 44, 40));
        addSlotToContainer(new SlotItemHandler(tile.getOutputInv(), 0, 116, 40) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
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
            sendMessage();
        }
        if (stack == null && tile.getTank().getFluid() == null) {
            return;
        }
        if (stack == null && tile.getTank().getFluid() != null) {
            stack = tile.getTank().getFluid().copy();
            sendMessage();
        } else if (stack != null && tile.getTank().getFluid() == null) {
            stack = null;
            sendMessage();
        } else if (stack.getFluid() != tile.getTank().getFluid().getFluid()) {
            stack = tile.getTank().getFluid().copy();
            sendMessage();
        } else if (stack.amount != tile.getTank().getFluidAmount()) {
            stack = tile.getTank().getFluid().copy();
            sendMessage();
        }
    }

    private void sendMessage() {
        Survivalism.INSTANCE.getPacketHandler().sendToPlayer(new MessageUpdateCrushingVat(tile), (EntityPlayerMP) player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 0, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 0, false)) {
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
