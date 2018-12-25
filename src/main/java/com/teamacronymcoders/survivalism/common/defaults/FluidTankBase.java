package com.teamacronymcoders.survivalism.common.defaults;

import net.minecraftforge.fluids.*;

import javax.annotation.Nullable;

public class FluidTankBase extends FluidTank {

    protected FluidStack stack;
    protected int capacity;


    public FluidTankBase(int capacity) {
        this(null, capacity);
    }

    public FluidTankBase (@Nullable FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
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
