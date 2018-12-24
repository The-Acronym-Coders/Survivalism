package com.teamacronymcoders.survivalism.utils.storages;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidHandler implements IFluidHandler, IFluidTank {

    protected FluidStack stack;
    protected int capacity;

    public FluidHandler(int capacity) {
        this(null, capacity);
    }

    public FluidHandler(FluidStack stack, int capacity) {
        this.stack = stack;
        this.capacity = capacity;
    }

    public FluidHandler(Fluid fluid, int amount, int capacity) {
        this(new FluidStack(fluid, amount), capacity);
    }


    @Nullable
    @Override
    public FluidStack getFluid() {
        return stack;
    }

    @Override
    public int getFluidAmount() {
        return stack.amount;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        FluidTankInfo info = this.getInfo();
        return new IFluidTankProperties[]{
                new FluidTankProperties(info.fluid, info.capacity, true, true)
        };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (!doFill) {
            if (stack == null) {
                return Math.min(capacity, resource.amount);
            }
            if (!stack.isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(capacity - stack.amount, resource.amount);
        }

        if (!stack.isFluidEqual(resource)) {
            return 0;
        }

        int filled = capacity - stack.amount;

        if (resource.amount < filled) {
            stack.amount += resource.amount;
            filled = resource.amount;
        } else {
            stack.amount = capacity;
        }
        return filled;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(stack)) {
            return null;
        }
        return drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (stack == null) {
            return null;
        }

        int drained = maxDrain;

        if (stack.amount < drained) {
            drained = stack.amount;
        }

        FluidStack fluidStack = new FluidStack(stack, drained);

        if (doDrain) {
            stack.amount -= drained;
            if (stack.amount <= 0) {
                stack = null;
            }
        }
        return stack;
    }
}
