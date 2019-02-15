package com.teamacronymcoders.survivalism.common.tiles.barrels;

import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.common.inventory.BarrelHandler;
import com.teamacronymcoders.survivalism.utils.SurvivalismStorage;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileBarrelStorage extends TileBarrelBase implements ITickable, IHasGui {

    protected FluidTank input = new FluidTank(SurvivalismStorage.TANK_CAPACITY);
    private int prevInputAmount = 0;
    protected ItemStackHandler inv = new BarrelHandler(9, this);

    @Override
    public void update() {
        super.update();
        updateClientInputFluid(getInput());
    }

    // NBT
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        input.readFromNBT(compound.getCompoundTag("inputTank"));
        inv.deserializeNBT(compound.getCompoundTag("items"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", input.writeToNBT(new NBTTagCompound()));
        compound.setTag("items", inv.serializeNBT());
        return super.writeToNBT(compound);
    }


    // Capabilities
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(input);
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !isSealed()) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }


    // Getters/Setters
    public FluidTank getInput() {
        return input;
    }

    public ItemStackHandler getInv() {
        return inv;
    }

    // Client Update Methods
    public void updateClientInputFluid(FluidTank tank) {
        if (tank != null) {
            if (tank.getFluidAmount() >= 0) {
                if (tank.getFluidAmount() != prevInputAmount) {
                    prevInputAmount = tank.getFluidAmount();
                    world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 8);
                }
            }
        }
    }

    @Override
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return null;
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return null;
    }
}