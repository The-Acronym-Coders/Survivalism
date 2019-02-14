package com.teamacronymcoders.survivalism.common.inventory;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class RangedFluidWrapper implements IFluidHandler {

    private FluidTank input;
    private FluidTank output;

    public RangedFluidWrapper(FluidTank input, FluidTank output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return input.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return output.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return output.drain(maxDrain, doDrain);
    }
}
