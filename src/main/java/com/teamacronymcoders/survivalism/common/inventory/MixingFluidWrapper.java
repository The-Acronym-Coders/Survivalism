package com.teamacronymcoders.survivalism.common.inventory;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class MixingFluidWrapper implements IFluidHandler {

    private FluidTank main;
    private FluidTank secondary;
    private FluidTank output;

    public MixingFluidWrapper(FluidTank main, FluidTank secondary, FluidTank output) {
        this.main = main;
        this.secondary = secondary;
        this.output = output;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (main.getFluid() == null) {
            return main.fill(resource, doFill);
        }

        if (main.getFluid().containsFluid(resource) && main.getFluidAmount() + resource.amount <= main.getCapacity()) {
            return main.fill(resource, doFill);
        }

        if (secondary.getFluid() == null) {
            return secondary.fill(resource, doFill);
        }

        if (secondary.getFluid().containsFluid(resource) && secondary.getFluidAmount() + resource.amount <= secondary.getCapacity()) {
            return secondary.fill(resource, doFill);
        }

        return 0;
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
