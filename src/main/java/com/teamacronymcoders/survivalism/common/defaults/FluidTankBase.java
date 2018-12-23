package com.teamacronymcoders.survivalism.common.defaults;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;

public class FluidTankBase implements IFluidTank {

    protected FluidStack stack;
    protected int capacity;


    public FluidTankBase(int capacity) {
        this(null, capacity);
    }

    public FluidTankBase(FluidStack stack, int capacity) {
        this.stack = stack;
        this.capacity = capacity;
    }

    public FluidTankBase(Fluid fluid, int amount, int capacity) {
        this(new FluidStack(fluid, amount), capacity);
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return stack;
    }

    @Override
    public int getFluidAmount() {
        if (stack == null) {
            return 0;
        }
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
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (stack == null) {
            return null;
        }

        int drained = maxDrain;

        if (stack.amount < drained) {
            drained = stack.amount;
        }

        FluidStack fluidStack =  new FluidStack(stack, drained);

        if (doDrain) {
            stack.amount -= drained;
            if (stack.amount <= 0) {
                stack = null;
            }
        }
        return stack;
    }
}
