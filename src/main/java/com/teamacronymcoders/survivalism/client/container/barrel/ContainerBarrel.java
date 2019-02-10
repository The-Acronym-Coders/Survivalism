package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.network.MessageUpdateBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class ContainerBarrel extends Container {

	protected EntityPlayer player;
	protected TileBarrel tile;
	private FluidStack input;
	private FluidStack output;

	public ContainerBarrel(EntityPlayer player, TileBarrel tile) {
		this.tile = tile;
		this.player = player;
	}

	public TileBarrel getTile() {
		return tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !tile.isInvalid() && player.getDistanceSq(tile.getPos()) < 64;
	}

	boolean firstSend = false;

	@Override
	public final void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (tile.getWorld().isRemote) return;
		if (!firstSend) {
			firstSend = true;
			sendMessage();
		}
		input = checkFluid(input, tile.getInput());
		output = checkFluid(output, tile.getOutput());
	}

	private FluidStack checkFluid(FluidStack stack, FluidTank tank) {
		if (stack == null && tank.getFluid() == null) return null;
		if (stack == null && tank.getFluid() != null) {
			sendMessage();
			return tank.getFluid().copy();
		} else if (stack != null && tank.getFluid() == null) {
			sendMessage();
			return null;
		} else if (stack.getFluid() != tank.getFluid().getFluid()) {
			sendMessage();
			return tank.getFluid().copy();
		} else if (stack.amount != tank.getFluidAmount()) {
			sendMessage();
			return tank.getFluid().copy();
		}
		return stack;
	}

	private void sendMessage() {
		Survivalism.INSTANCE.getPacketHandler().sendToPlayer(new MessageUpdateBarrel(tile), (EntityPlayerMP) player);
	}

}
