package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.defaults.FluidTankBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class TileBarrel extends TileEntity implements ITickable {

    private FluidTankBase tankBase;
    private static ConfigEntry capacityEntry;

    public void initialize() {
        GameRegistry.registerTileEntity(TileBarrel.class, ModBlocks.blockBarrel.getRegistryName());
        config();
        init();
    }

    private void config() {
        capacityEntry = new ConfigEntry("Barrel", "Capacity", Property.Type.INTEGER, "16000");
    }

    private void init() {
        tankBase = new FluidTankBase(null, Integer.getInteger(capacityEntry.getValue()));
    }


    @Override
    public void update() {
        
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new IFluidHandler() {

                @Override
                public IFluidTankProperties[] getTankProperties() {
                    FluidTankInfo info = tankBase.getInfo();
                    return new IFluidTankProperties[] {
                            new FluidTankProperties(info.fluid, info.capacity, true, true)
                    };
                }

                @Override
                public int fill(FluidStack resource, boolean doFill) {
                    return tankBase.fill(resource, doFill);
                }

                @Nullable
                @Override
                public FluidStack drain(FluidStack resource, boolean doDrain) {
                    return tankBase.drain(resource, doDrain);
                }

                @Nullable
                @Override
                public FluidStack drain(int maxDrain, boolean doDrain) {
                    return tankBase.drain(maxDrain, doDrain);
                }
            });
        }

        return super.getCapability(capability, facing);
    }
}
