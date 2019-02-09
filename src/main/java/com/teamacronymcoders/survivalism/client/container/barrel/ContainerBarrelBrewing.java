package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerBarrelBrewing extends ContainerBarrel {

	private TileBarrel tile;

	public ContainerBarrelBrewing(IInventory playerInv, TileBarrel tile) {
		this.tile = tile;
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
		IItemHandler itemHandler = this.tile.getInv();
		addSlotToContainer(new SlotSealable(itemHandler, 0, 80, 17, tile));
		addSlotToContainer(new SlotSealable(itemHandler, 1, 80, 37, tile));
		addSlotToContainer(new SlotSealable(itemHandler, 2, 80, 57, tile));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.getDistanceSq(tile.getPos()) < 64;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		//TODO change this to check if the slot is enabled / disabled
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileBarrel.STORAGE_SIZE) {
				if (!this.mergeItemStack(itemstack1, TileBarrel.STORAGE_SIZE, this.inventorySlots.size(), true)) { return ItemStack.EMPTY; }
			} else if (!this.mergeItemStack(itemstack1, 0, TileBarrel.STORAGE_SIZE, false)) { return ItemStack.EMPTY; }

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}
