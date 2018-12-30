package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class TileBarrel extends TileEntity implements ITickable {
    public static final int TANK_CAPACITY = 16000;
    private static final int STORAGE_SIZE = 9;
    private List<RecipeBarrel> barrelRecipes;
    private FluidTank inputTank;
    private FluidTank outputTank;
    private ItemHandler itemHandler;
    private int durationTicks;
    private boolean sealed = false;

    public TileBarrel() {
        inputTank = new FluidTank(TANK_CAPACITY) {
            @Override
            public boolean canFill() {
                return true;
            }

            @Override
            public boolean canDrain() {
                return false;
            }
        };
        outputTank = new FluidTank(TANK_CAPACITY) {
            @Override
            public boolean canFill() {
                return false;
            }

            @Override
            public boolean canDrain() {
                return true;
            }
        };
        itemHandler = new ItemHandler(STORAGE_SIZE, 64) {
            @Override
            protected void onContentsChanged(int slot) {
                TileBarrel.this.markDirty();
            }
        };
        barrelRecipes = RecipeStorage.getBarrelRecipes();
    }

    public void toggleSealed() {
        if (isSealed()) {
            sealed = false;
        } else if (!isSealed()) {
            sealed = true;
        }
    }

    ///////////
    // Stuff //
    ///////////

    @Override
    public void update() {
        if (checkState(EnumsBarrelStates.BREWING) && isSealed()) {
            FluidStack inputTank = getInputTank().getFluid();
            for (RecipeBarrel recipe : barrelRecipes) {
                if (recipe instanceof BrewingRecipe) {
                    FluidStack recipeInputFS = ((BrewingRecipe) recipe).getInputFluid();
                    if (inputTank != null && inputTank.getFluid().equals(recipeInputFS.getFluid())) {
                        if (itemHandler.getStacks().equals(((BrewingRecipe) recipe).getInputItemStacks())) {
                            durationTicks = ((BrewingRecipe) recipe).getTicks();
                            int currentTicks = 0;
                            for (int x = 0; x < durationTicks; ++x) {
                                currentTicks++;
                            }
                            if (currentTicks == durationTicks) {
                                getOutputTank().fillInternal(((BrewingRecipe) recipe).getOutputFluid(), true);
                                durationTicks = 0;
                            }
                        }
                    }
                }
            }
        } else if (checkState(EnumsBarrelStates.SOAKING) && isSealed()) {
            FluidStack fluidStack = getInputTank().getFluid();
            ItemStack stack = itemHandler.getStackInSlot(0);
            for (RecipeBarrel recipe : barrelRecipes) {
                if (recipe instanceof SoakingRecipe) {
                    FluidStack recipeInputFS = ((SoakingRecipe) recipe).getInputFluid();
                    ItemStack recipeInputIS = ((SoakingRecipe) recipe).getInputItemStack();
                    if (fluidStack != null && fluidStack.getFluid().equals(recipeInputFS.getFluid())) {
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
        if (compound.hasKey("inputTank")) {
            inputTank = inputTank.readFromNBT(compound.getCompoundTag("inputTank"));
        }
        if (compound.hasKey("outputTank")) {
            outputTank = outputTank.readFromNBT(compound.getCompoundTag("outputTank"));
        }
        if (compound.hasKey("inventory")) {
            itemHandler.deserializeNBT(compound);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", inputTank.writeToNBT(new NBTTagCompound()));
        compound.setTag("outputTank", outputTank.writeToNBT(new NBTTagCompound()));
        compound.setTag("inventory", itemHandler.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (Objects.equals(facing != null ? facing.getName() : null, "down")) {
                if (checkState(EnumsBarrelStates.BREWING)) {
                    return (T) outputTank;
                }
            } else {
                return (T) inputTank;
            }
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

    private boolean isSealed() {
        return sealed;
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
            ensureStateIs(EnumsBarrelStates.BREWING);
        } else if (currentState == EnumsBarrelStates.BREWING) {
            ensureStateIs(EnumsBarrelStates.SOAKING);
        } else if (currentState == EnumsBarrelStates.SOAKING) {
            ensureStateIs(EnumsBarrelStates.STORAGE);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    //////////////////////
    // Setters Getters //
    ////////////////////
    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }
}
