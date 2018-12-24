package com.teamacronymcoders.survivalism.utils.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHelper {

    public static FluidStack getFluidStackFromHandler(ItemStack fluidContainer) {
        if (isFluidHandler(fluidContainer)) {
            IFluidTankProperties[] props = fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).getTankProperties();
            return props.length <= 0 ? null : props[0].getContents();
        }
        return null;
    }

    public static FluidStack getFluidStackFromHandler(TileEntity fluidTile) {
        if (isFluidHandler(fluidTile)) {
            IFluidTankProperties[] props = fluidTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties();
            return props.length <= 0 ? null : props[0].getContents();
        }
        return null;
    }

    private static boolean isFluidHandler(ItemStack stack) {
        return !stack.isEmpty() & stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    }

    private static boolean isFluidHandler(TileEntity fluidTile) {
        return fluidTile != null & fluidTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }

    public static boolean isFluidStacksSame(FluidStack stack1, FluidStack stack2) {
        if (stack1.getFluid() == stack2.getFluid()) {
            return true;
        }
        return false;
    }

}
