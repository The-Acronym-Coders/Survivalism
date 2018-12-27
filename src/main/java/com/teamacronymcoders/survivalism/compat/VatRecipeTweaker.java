package com.teamacronymcoders.survivalism.compat;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;
import com.teamacronymcoders.survivalism.common.recipe.recipes.crushingVat.CrushingVatRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

@ModOnly("crafttweaker")
@ZenClass("mods.survivalism.CrushingVat")
@ZenRegister
public class VatRecipeTweaker {

    @ZenMethod
    public static void addCrushingRecipe(IItemStack inputStack, IItemStack outputStack, ILiquidStack outputFluidStack, int jumps) {
        Survivalism.LATE_ADDITIONS.add(new addCrushingRecipe(inputStack, outputStack, outputFluidStack, jumps));
    }

    private static class addCrushingRecipe implements IAction {

        @Nonnull
        ItemStack inputStack;
        ItemStack outputStack;
        FluidStack outputFluidStack;
        int jumps;

        public addCrushingRecipe(IItemStack inputStack, IItemStack outputStack, ILiquidStack outputFluid, int jumps) {
            this.inputStack = CraftTweakerMC.getItemStack(inputStack);
            this.outputStack = CraftTweakerMC.getItemStack(outputStack);
            this.outputFluidStack = CraftTweakerMC.getLiquidStack(outputFluid);
            this.jumps = jumps;
        }

        @Override
        public void apply() {
            if (jumps <= 0) {
                CraftTweakerAPI.logError("Jumps can not be lower than 1, Defaulting to 1 jumps per item processed!");
            } else {
                RecipeVat vatRecipe = new CrushingVatRecipe(inputStack, outputStack, outputFluidStack, jumps);
                RecipeStorage.getVatRecipes().add(vatRecipe);
            }
        }

        @Override
        public String describe() {
            return null;
        }
    }
}
