package com.teamacronymcoders.survivalism.common.tiles.barrels;

import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.SurvivalismStorage;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class TileBarrelSoaking extends TileBarrelBase implements ITickable, IHasGui {

    protected int ticks = 0;
    protected FluidTank input = new FluidTank(SurvivalismStorage.TANK_CAPACITY);
    protected SoakingRecipe recipe;
    private int prevInputAmount = 0;

    public TileBarrelSoaking() {
        super(2);
        input.setCanDrain(false);
    }

    @Override
    public void update() {
        super.update();
        processSoaking();
        updateClientInputFluid(getInput());
    }

    // NBT
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        input.readFromNBT(compound.getCompoundTag("inputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", input.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }


    // Capabilities
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(input);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    // Process
    protected void processSoaking() {
        FluidStack fluid = input.getFluid();
        if (fluid != null && fluid.amount > 0) {
            if (recipe == null || !recipe.matches(this)) {
                recipe = BarrelRecipeManager.getSoakingRecipe(this);
            }
            if (recipe == null) {
                return;
            }
            if (ticks++ >= recipe.getTicks()) {
                ItemStack curOutput = inv.getStackInSlot(1);
                if (!curOutput.isEmpty() && !ItemHandlerHelper.canItemStacksStack(curOutput, recipe.getOutput())) {
                    return;
                }
                if (!curOutput.isEmpty() && curOutput.getCount() + recipe.getOutput().getCount() > curOutput.getMaxStackSize()) {
                    return;
                }
                ticks = 0;
                if (HelperMath.tryPercentage(recipe.getFluidUseChance())) {
                    input.getFluid().amount -= recipe.getInput().amount;
                }
                inv.getStackInSlot(0).shrink(1);
                inv.insertItem(1, recipe.getOutput().copy(), false);
            }
        }
    }


    // Getters/Setters
    public FluidTank getInput() {
        return input;
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
