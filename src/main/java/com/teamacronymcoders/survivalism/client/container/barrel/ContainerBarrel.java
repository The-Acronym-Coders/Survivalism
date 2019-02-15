package com.teamacronymcoders.survivalism.client.container.barrel;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.utils.network.MessageUpdateBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;

public class ContainerBarrel extends Container {

    protected EntityPlayer player;
    protected TileBarrelBase tile;
    boolean firstSend = false;
    private FluidStack input;
    private FluidStack output;

    public ContainerBarrel(EntityPlayer player, TileBarrelBase tile) {
        this.tile = tile;
        this.player = player;
    }

    public TileBarrelBase getTile() {
        return tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !tile.isInvalid() && player.getDistanceSq(tile.getPos()) < 64;
    }

    @Override
    public final void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (tile.getWorld().isRemote) {
            return;
        }
        if (!firstSend) {
            firstSend = true;
            if (tile instanceof TileBarrelBrewing) {
                input = ((TileBarrelBrewing) tile).getInput().getFluid();
                output = ((TileBarrelBrewing) tile).getOutput().getFluid();
            } else if (tile instanceof TileBarrelSoaking) {
                input = ((TileBarrelSoaking) tile).getInput().getFluid();
            } else if (tile instanceof TileBarrelStorage) {
                input = ((TileBarrelStorage) tile).getInput().getFluid();
            }
            sendMessage();
        }
        if (tile instanceof TileBarrelBrewing) {
            input = checkFluid(input, ((TileBarrelBrewing) tile).getInput().getFluid());
            output = checkFluid(output, ((TileBarrelBrewing) tile).getOutput().getFluid());
        } else if (tile instanceof TileBarrelSoaking) {
            input = checkFluid(input, ((TileBarrelSoaking) tile).getInput().getFluid());
        } else if (tile instanceof TileBarrelStorage) {
            input = checkFluid(input, ((TileBarrelStorage) tile).getInput().getFluid());
        }
    }

    private FluidStack checkFluid(FluidStack stack, FluidStack tank) {
        boolean sendUpdate = false;
        if (stack == null && tank == null) {
            return null;
        }
        if (stack == null && tank != null) {
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
        Survivalism.INSTANCE.getPacketHandler().sendToPlayer(new MessageUpdateBarrel(tile), (EntityPlayerMP) player);
    }

}
