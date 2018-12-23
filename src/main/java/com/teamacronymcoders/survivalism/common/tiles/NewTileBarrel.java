package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.defaults.FluidTankBase;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.storages.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class NewTileBarrel extends TileEntity implements ITickable {

    private enum BarrelStates {
        STORAGE,
        BREWING,
        SOAKING
    }

    private enum currentState = ;
    private String storage = "storage";
    private String brewing = "brewing";
    private String soaking = "soaking";

    FluidTankBase tankBase;
    ItemHandler itemHandler;

    public NewTileBarrel() {
        CurrentState = CurrentState.STORAGE;
        tankBase = new FluidTankBase(16000);
        itemHandler = new ItemHandler(3, 64);
    }

    @Override
    public void update() {
        int durationTicks;

        if (currentState.equals(storage)) {
            return;
        } else if (currentState.equals(brewing)) {
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
        } else if (currentState.equals(soaking)) {
            FluidStack fluidStack = getFluidStack();
            ItemStack stack = itemHandler.getStackInSlot(0);
            for (RecipeBarrel recipe : RecipeStorage.barrelRecipes) {
                if (recipe instanceof SoakingRecipe) {
                    FluidStack recipeInputFS = ((SoakingRecipe) recipe).getInputFluid();
                    ItemStack recipeInputIS = ((SoakingRecipe) recipe).getInputItemStack();
                    if (fluidStack.getFluid().equals(recipeInputFS.getFluid())) {
                        List<Item> items = new ArrayList<>();
                        for (ItemStack internalStack : itemHandler.getStacks()) {
                            items.add(internalStack.getItem());
                        }
                        if (items.contains(recipeInputIS.getItem())) {
                            durationTicks = ((SoakingRecipe) recipe).getTicks();
                            int currentTicks = 0;
                            for (int x = 0; x < durationTicks; ++x) {
                                currentTicks++;
                            }
                            if (currentTicks == durationTicks) {
                                fluidStack.amount -= ((SoakingRecipe) recipe).getDecAmount();
                                itemHandler.setStackInSlot(0, ((SoakingRecipe) recipe).getOutputItemStack());
                            }
                        }
                    }
                }
            }
        }
    }

    public FluidTankBase getTankBase() {
        return tankBase;
    }

    public FluidStack getFluidStack() {
        return tankBase.getFluid();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
}
