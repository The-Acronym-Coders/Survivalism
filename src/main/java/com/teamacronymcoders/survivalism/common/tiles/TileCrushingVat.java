package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipeManager;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileCrushingVat extends TileEntity implements IUpdatingInventory {

    protected ItemStack failedMatch;
    protected VatRecipe curRecipe;
    protected FluidTank tank = new FluidTank(TileBarrel.TANK_CAPACITY);
    protected ItemStackHandler inputInv = new UpdatingItemStackHandler(1, this);
    protected ItemStackHandler outputInv = new UpdatingItemStackHandler(1, this);
    protected double jumps = 0.0D;
    protected int fluidLastJump;

    public void onJump(EntityLivingBase jumper) {
        boolean dirty = false;

        if (jumper == null || inputInv.getStackInSlot(0).isEmpty()) {
            curRecipe = null;
            jumps = 0;
            return;
        }

        if (fluidLastJump != tank.getFluidAmount()) {
            fluidLastJump = tank.getFluidAmount();
            dirty = true;
        }

        if ((curRecipe == null || !curRecipe.matches(jumper, inputInv.getStackInSlot(0))) && inputInv.getStackInSlot(0) != failedMatch) {
            curRecipe = VatRecipeManager.getRecipe(jumper, inputInv.getStackInSlot(0));
            if (curRecipe == null) {
                failedMatch = inputInv.getStackInSlot(0);
                return;
            } else {
                failedMatch = ItemStack.EMPTY;
            }
        } else if (curRecipe == null) {
            return;
        }
        jumps += SurvivalismConfigs.baseJumpValue * VatRecipeManager.getBootsMultiplier(jumper);
        if (jumps >= curRecipe.getJumps() && canInsertResults()) {
            if (HelperMath.tryPercentage(curRecipe.getItemChance())) {
                outputInv.insertItem(0, curRecipe.getOutputStack().copy(), false);
            }
            tank.fill(curRecipe.getOutput(), true);
            jumps = 0;
            dirty = true;
            inputInv.getStackInSlot(0).shrink(1);
        }
        if (dirty) {
            markDirty();
        }
    }

    protected boolean canInsertResults() {
        if (tank.getFluid() == null && (curRecipe.getOutputStack().isEmpty() || outputInv.getStackInSlot(0).isEmpty())) {
            return true;
        }
        Fluid inTank = tank.getFluid().getFluid();
        FluidStack output = curRecipe.getOutput();
        if (!(inTank.getName().equals(output.getFluid().getName()) && tank.getFluidAmount() + output.amount <= tank.getCapacity())) {
            return false;
        }
        ItemStack out = curRecipe.getOutputStack();
        ItemStack cur = outputInv.getStackInSlot(0);
        if (cur.isEmpty()) {
            return true;
        }
        if (!out.isEmpty() && !ItemHandlerHelper.canItemStacksStack(cur, out)) {
            return false;
        }
        if (!out.isEmpty() && cur.getCount() + out.getCount() > cur.getMaxStackSize()) {
            return false;
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("tank"));
        inputInv.deserializeNBT(compound.getCompoundTag("inputItems"));
        outputInv.deserializeNBT(compound.getCompoundTag("outputItems"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        compound.setTag("inputItems", inputInv.serializeNBT());
        compound.setTag("outputItems", outputInv.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing.getAxis() != Axis.Y) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputInv);
            } else if (facing == EnumFacing.DOWN) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputInv);
            }
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != EnumFacing.UP) {
                return true;
            }
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return true;
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    public ItemStackHandler getInputInv() {
        return inputInv;
    }

    public ItemStackHandler getOutputInv() {
        return outputInv;
    }

    public FluidTank getTank() {
        return tank;
    }
}
