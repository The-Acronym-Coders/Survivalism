package com.teamacronymcoders.survivalism.api;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeHelper {

    private Logger logger = Survivalism.logger;

    public void addBrewingRecipe(@Nonnull FluidStack inputFluid, @Nonnull List<ItemStack> inputItemStacks, @Nonnull FluidStack outputFluid, int ticks) {
        if (inputFluid.amount <= 0){
            logger.error("Input Fluid: " + inputFluid.getLocalizedName() + " is either null or has an amount less than or equal to 0mb!");
        } else if (ticks <= 0) {
            logger.error("Processing Ticks can't be less than or equal to 0");
        } else if (inputItemStacks.size() <= 0 || inputItemStacks.size() > 3) {
            logger.error("ItemStack list for Brewing Recipe: " + inputFluid.getLocalizedName() + " can't be less than or equal to 0 and not greater than 3 ItemStacks big!");
        } else if (outputFluid.amount <= 0) {
            logger.error("Output Fluid: " + outputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
        } else {
            RecipeBarrel brewingRecipe = new BrewingRecipe(inputFluid, inputItemStacks, outputFluid, ticks);
            RecipeStorage.barrelRecipes.add(brewingRecipe);
        }
    }

    public void addSoakingRecipe(@Nonnull FluidStack inputFluid, @Nonnull ItemStack inputItemStack, @Nonnull ItemStack outputItemStack, int decAmount, int ticks) {
        if (inputFluid.amount <= 0){
            logger.error("Input Fluid: " + inputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
        } else if (ticks <= 0) {
            logger.error("Processing Ticks can't be less than or equal to 0");
        } else if (inputItemStack.isEmpty()) {
            logger.error("Input ItemStack can not be Empty");
        } else if (outputItemStack.isEmpty()) {
            logger.error("Output ItemStack can not be Empty");
        } else if (decAmount < 0 || decAmount > TileBarrel.getTankBase().getCapacity()) {
            logger.error("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.getTankBase().getCapacity());
        } else {
            RecipeBarrel soakingRecipe = new SoakingRecipe(inputFluid, inputItemStack, outputItemStack, decAmount, ticks);
            RecipeStorage.barrelRecipes.add(soakingRecipe);
        }
    }
}
