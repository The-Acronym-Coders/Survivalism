package com.teamacronymcoders.survivalism.compat;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.RecipeHelper;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
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
import java.util.ArrayList;
import java.util.List;

@ModOnly("crafttweaker")
@ZenClass("mods.survivalism.Barrel")
@ZenRegister
public class BarrelRecipeTweaker {

    @ZenMethod
    public static void addBrewingRecipe(@Nonnull ILiquidStack inputFluid, @Nonnull IItemStack[] inputItemStacks, @Nonnull ILiquidStack outputFluid, int ticks) {
        Survivalism.LATE_ADDITIONS.add(new addBrewingRecipe(inputFluid, inputItemStacks, outputFluid, ticks));
    }

    @ZenMethod
    public static void addSoakingRecipe(@Nonnull ILiquidStack inputFluid, @Nonnull IItemStack inputItemStack, @Nonnull IItemStack outputItemStack, int decAmount, int ticks) {
        Survivalism.LATE_ADDITIONS.add(new addSoakingRecipe(inputFluid, inputItemStack, outputItemStack, decAmount, ticks));
    }


    private static class addBrewingRecipe implements IAction {
        FluidStack inputFluid;
        List<ItemStack> inputItemStacks = new ArrayList<>();
        FluidStack outputFluid;
        int ticks;

        private addBrewingRecipe(@Nonnull ILiquidStack inputFluid, @Nonnull IItemStack[] inputItemStacks, @Nonnull ILiquidStack outputFluid, int ticks) {
            this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
            for (IItemStack inputItemStack : inputItemStacks) {
                this.inputItemStacks.add(CraftTweakerMC.getItemStack(inputItemStack));
            }
            this.outputFluid = CraftTweakerMC.getLiquidStack(outputFluid);
            this.ticks = ticks;
        }

        @Override
        public void apply() {
            if (inputFluid.amount <= 0) {
                CraftTweakerAPI.logError("Input Fluid: " + inputFluid.getLocalizedName() + " is either null or has an amount less than or equal to 0mb!");
            } else if (ticks <= 0) {
                CraftTweakerAPI.logError("Processing Ticks can't be less than or equal to 0");
            } else if (inputItemStacks.size() <= 0 || inputItemStacks.size() > 3) {
                CraftTweakerAPI.logError("ItemStack list for Brewing Recipe: " + inputFluid.getLocalizedName() + " can't be less than or equal to 0 and not greater than 3 ItemStacks big!");
            } else if (outputFluid.amount <= 0) {
                CraftTweakerAPI.logError("Output Fluid: " + outputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
            } else {
                RecipeHelper.addCRTBrewing(inputFluid, inputItemStacks, outputFluid, ticks);
            }
        }

        @Override
        public String describe() {
            return "Adding Brewing Recipe: \n" + "Input Fluid: " + inputFluid.getLocalizedName() + "\n" + "Input ItemStacks: " + HelperString.formatItemStacks(inputItemStacks) + "\n" + "Output Fluid: " + outputFluid.getLocalizedName();
        }
    }

    private static class addSoakingRecipe implements IAction {
        FluidStack inputFluid;
        ItemStack inputItemStack;
        ItemStack outputItemStack;
        int decAmount;
        int ticks;

        private addSoakingRecipe(@Nonnull ILiquidStack inputFluid, @Nonnull IItemStack inputItemStack, @Nonnull IItemStack outputItemStack, int decAmount, int ticks) {
            this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
            this.inputItemStack = CraftTweakerMC.getItemStack(inputItemStack);
            this.outputItemStack = CraftTweakerMC.getItemStack(outputItemStack);
            this.decAmount = decAmount;
            this.ticks = ticks;
        }

        @Override
        public void apply() {
            if (inputFluid.amount <= 0) {
                CraftTweakerAPI.logError("Input Fluid: " + inputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
            } else if (ticks <= 0) {
                CraftTweakerAPI.logError("Processing Ticks can't be less than or equal to 0");
            } else if (inputItemStack.isEmpty()) {
                CraftTweakerAPI.logError("Input ItemStack can not be Empty");
            } else if (outputItemStack.isEmpty()) {
                CraftTweakerAPI.logError("Output ItemStack can not be Empty");
            } else if (decAmount < 0 || decAmount > TileBarrel.TANK_CAPACITY) {
                CraftTweakerAPI.logError("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.TANK_CAPACITY);
            } else {
                RecipeHelper.addCRTSoaking(inputFluid, inputItemStack, outputItemStack, decAmount, ticks);
            }
        }

        @Override
        public String describe() {
            return "Adding Soaking Recipe: \n" + "Input Fluid: " + inputFluid.getLocalizedName() + "\n" + "Input ItemStack: " + inputItemStack.getDisplayName() + "\n" + "Output ItemStack: " + outputItemStack.getDisplayName();
        }
    }
}
