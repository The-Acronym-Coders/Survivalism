package com.teamacronymcoders.survivalism.common.defaults;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;

public class FluidTankBase implements IFluidTank {

    protected FluidStack fluidStack;
    protected int capacity;

    public FluidTankBase(int capacity) {
        this(null, capacity);
    }

    public FluidTankBase(FluidStack fluidStack, int capacity) {
        this.fluidStack = fluidStack;
        this.capacity = capacity;
    }

    public FluidTankBase(Fluid fluid, int amount, int capacity) {
        this(new FluidStack(fluid, amount), capacity);
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    public int getRemainingCapacity() {
        if (fluidStack == null) {
            return getCapacity();
        }
        return fluidStack.amount >= capacity ? 0 : getCapacity() - fluidStack.amount;
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return fluidStack;
    }

    @Override
    public int getFluidAmount() {
        return fluidStack.amount;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (!doFill) {
            if (fluidStack == null) {
                return Math.min(capacity, resource.amount);
            }
            if (!fluidStack.isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(capacity - fluidStack.amount, resource.amount);
        }

        if (!fluidStack.isFluidEqual(resource)) {
            return 0;
        }

        int filled = capacity - fluidStack.amount;

        if (resource.amount < filled) {
            fluidStack.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluidStack.amount = capacity;
        }
        return filled;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (fluidStack == null) {
            return null;
        }

        int drained = maxDrain;

        if (fluidStack.amount < drained) {
            drained = fluidStack.amount;
        }

        FluidStack stack = new FluidStack(fluidStack, drained);

        if (doDrain) {
            fluidStack.amount -= drained;
            if (fluidStack.amount <= 0) {
                fluidStack = null;
            }
        }

        return stack;
    }

    @Nullable
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(fluidStack)) {
            return null;
        }
        return drain(resource.amount, doDrain);
    }

    public FluidTankBase readFromNBT(NBTTagCompound tag) {
        FluidStack fluidStack = null;
        if (!tag.hasKey("Empty")) {
            fluidStack = FluidStack.loadFluidStackFromNBT(tag);
        }
        setFluidStack(fluidStack);
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (fluidStack != null) {
            fluidStack.writeToNBT(tag);
        } else {
            tag.setString("Empty", "");
        }
        return tag;
    }
}
