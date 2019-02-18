package com.teamacronymcoders.survivalism.common.tiles.barrels;

import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class TileBarrelBase extends TileEntity implements ITickable, IUpdatingInventory {

    protected boolean poweredLastTick = false;

    // Update Methods;
    @Override
    public void update() {
        if (world.isRemote) {
            return;
        }

        boolean powered = world.isBlockPowered(pos);
        if (poweredLastTick != powered) {
            updateSeal(powered);
        }
        poweredLastTick = powered;
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }


    // Sealing Methods
    public boolean isSealed() {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBarrelBase) {
            return state.getValue(BlockBarrelBase.SEALED);
        }
        return false;
    }

    public void updateSeal(boolean seal) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBarrelBase) {
            world.setBlockState(pos, state.withProperty(BlockBarrelBase.SEALED, seal));
        }
    }

}
