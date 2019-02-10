package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.network.MessageUpdateBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;

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
			input = tile.getInput().getFluid();
			output = tile.getOutput().getFluid();
			sendMessage();
		}
		input = checkFluid(input, tile.getInput().getFluid());
		output = checkFluid(output, tile.getOutput().getFluid());
	}

	private FluidStack checkFluid(FluidStack stack, FluidStack tank) {
		boolean sendUpdate = false;
		if (stack == null && tank == null) return null;
		if (stack == null && tank != null) sendUpdate = true;
		if (stack != null && tank == null) sendUpdate = true;
		if (tank != null && stack != null) {
			if (!tank.getFluid().getName().equals(stack.getFluid().getName())) sendUpdate = true;
			if (tank.amount != stack.amount) sendUpdate = true;
		}
		if (sendUpdate) sendMessage();
		return tank == null ? null : tank.copy();
	}

	private void sendMessage() {
		Survivalism.INSTANCE.getPacketHandler().sendToPlayer(new MessageUpdateBarrel(tile), (EntityPlayerMP) player);
	}

}
