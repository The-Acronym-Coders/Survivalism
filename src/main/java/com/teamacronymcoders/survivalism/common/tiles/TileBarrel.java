package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.defaults.FluidTankBase;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
import com.teamacronymcoders.survivalism.utils.storages.EnumsBarrelStates;
import com.teamacronymcoders.survivalism.utils.storages.ItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileBarrel extends TileEntity implements ITickable {

    private static FluidTankBase tank;
    private static ItemHandler itemHandler;
    List<RecipeBarrel> barrelRecipes;
    private int durationTicks;
    private boolean sealed = false;

    public TileBarrel() {
        tank = new FluidTankBase(16000);
        itemHandler = new ItemHandler(3, 64);
        barrelRecipes = RecipeStorage.getBarrelRecipes();

    }

    //////////////////////
    // Setters Getters //
    ////////////////////
    public static FluidTankBase getTankBase() {
        return tank;
    }

    @Override
    public void update() {
        if (checkState(EnumsBarrelStates.BREWING) && isSealed()) {
            FluidStack fluidStack = getFluidStack();
            for (RecipeBarrel recipe : barrelRecipes) {
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
        } else if (checkState(EnumsBarrelStates.SOAKING) && isSealed()) {
            FluidStack fluidStack = getFluidStack();
            ItemStack stack = itemHandler.getStackInSlot(0);
            for (RecipeBarrel recipe : barrelRecipes) {
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

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank = (FluidTankBase) tank.readFromNBT(compound.getCompoundTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
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

    private FluidStack getFluidStack() {
        return tank.getFluid();
    }

    public boolean isSealed() {
        return sealed;
    }

    public void setSealed(boolean sealed) {
        this.sealed = sealed;
    }


    //////////////////////
    // Utility Methods //
    ////////////////////

    /**
     * Stolen from HeatedTank
     * Sorry Skysom ;(
     */
    private void ensureStateIs(EnumsBarrelStates expectedBarrelState) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        EnumsBarrelStates currentBarrelState = currentState.getValue(BlockBarrel.BARREL_STATE);
        if (currentBarrelState != expectedBarrelState) {
            world.setBlockState(this.getPos(), currentState.withProperty(BlockBarrel.BARREL_STATE, expectedBarrelState));
        }
    }

    private boolean checkState(EnumsBarrelStates expectedBarrelState) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        EnumsBarrelStates currentBarrelState = currentState.getValue(BlockBarrel.BARREL_STATE);
        return currentBarrelState == expectedBarrelState;
    }

    public void cycleStates(IBlockState state) {
        EnumsBarrelStates currentState = state.getValue(BlockBarrel.BARREL_STATE);
        if (currentState == EnumsBarrelStates.STORAGE) {
            world.setBlockState(this.getPos(), state.withProperty(BlockBarrel.BARREL_STATE, EnumsBarrelStates.BREWING));
        } else if (currentState == EnumsBarrelStates.BREWING) {
            world.setBlockState(this.getPos(), state.withProperty(BlockBarrel.BARREL_STATE, EnumsBarrelStates.SOAKING));
        } else if (currentState == EnumsBarrelStates.SOAKING) {
            world.setBlockState(this.getPos(), state.withProperty(BlockBarrel.BARREL_STATE, EnumsBarrelStates.STORAGE));
        }
    }

    public boolean insertFluid(EntityPlayer player, EnumHand hand) {
        if (player != null) {
            ItemStack stack = player.getHeldItem(hand);
            FluidStack fluidStack = FluidHelper.getFluidStackFromHandler(stack);
            tank.fill(fluidStack, true);
        }
        return false;
    }
}
