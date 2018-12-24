package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.defaults.FluidTankBase;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.storages.EnumsBarrelStates;
import com.teamacronymcoders.survivalism.utils.storages.ItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileBarrel extends TileEntity implements ITickable {

    IBlockState currentState;

    static FluidTank tank;
    static ItemHandler itemHandler;

    int durationTicks;

    public TileBarrel() {
        tank = new FluidTank(16000);
        itemHandler = new ItemHandler(3, 64);
        currentState = this.world.getBlockState(this.pos);
    }

    @Override
    public void update() {
        EnumsBarrelStates currentBarrelState = currentState.getValue(BlockBarrel.BARREL_STATE);
        if (currentBarrelState.getName().equals(EnumsBarrelStates.STORAGE.getName())) {
            return;
        } else if (currentBarrelState.getName().equals(EnumsBarrelStates.BREWING.getName())) {
            FluidStack fluidStack = getFluidStack();
            for (RecipeBarrel recipe : RecipeStorage.barrelRecipes) {
                if (recipe instanceof BrewingRecipe) {
                    FluidStack recipeInputFS = ((BrewingRecipe) recipe).getInputFluid();
                    if (fluidStack.getFluid().equals(recipeInputFS.getFluid())) {
                        if (itemHandler.getStacks().equals(((BrewingRecipe) recipe).getInputItemStacks())) {
                            durationTicks = ((BrewingRecipe) recipe).getTicks();
                            int currentTicks = 0;
                            for (int x = 0; x < durationTicks; ++x) {
                                currentTicks++;
                            }
                            if (currentTicks == durationTicks) {
                                fluidStack = ((BrewingRecipe) recipe).getOutputFluid();
                                durationTicks = 0;
                            }
                        }
                    }
                }
            }
        } else if (currentBarrelState.getName().equals(EnumsBarrelStates.SOAKING.getName())) {
            FluidStack fluidStack = getFluidStack();
            ItemStack stack = itemHandler.getStackInSlot(0);
            for (RecipeBarrel recipe : RecipeStorage.barrelRecipes) {
                if (recipe instanceof SoakingRecipe) {
                    FluidStack recipeInputFS = ((SoakingRecipe) recipe).getInputFluid();
                    ItemStack recipeInputIS = ((SoakingRecipe) recipe).getInputItemStack();
                    if (fluidStack.getFluid().equals(recipeInputFS.getFluid())) {
                        if (stack.getItem().equals(recipeInputIS.getItem())) {
                            durationTicks = ((SoakingRecipe) recipe).getTicks();
                            int currentTicks = 0;
                            for (int x = 0; x < durationTicks; ++x) {
                                currentTicks++;
                            }
                            if (currentTicks == durationTicks) {
                                fluidStack.amount -= ((SoakingRecipe) recipe).getDecAmount();
                                itemHandler.setStackInSlot(0, ((SoakingRecipe) recipe).getOutputItemStack());
                                durationTicks = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    public static FluidTank getTankBase() {
        return tank;
    }

    public static FluidStack getFluidStack() {
        return tank.getFluid();
    }

    /**
     * Stolen from HeatedTank
     * Sorry Skysom ;(
     */
    private void ensureStateIs(EnumsBarrelStates expectedTankState) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        EnumsBarrelStates currentBarrelState = currentState.getValue(BlockBarrel.BARREL_STATE);
        if (currentBarrelState != expectedTankState) {
            world.setBlockState(this.getPos(), currentState.withProperty(BlockBarrel.BARREL_STATE, expectedTankState));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank = tank.readFromNBT(compound.getCompoundTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("tank", tank.writeToNBT(compound));
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
}
