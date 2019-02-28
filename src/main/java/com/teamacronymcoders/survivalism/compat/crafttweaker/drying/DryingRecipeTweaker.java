package com.teamacronymcoders.survivalism.compat.crafttweaker.drying;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRackRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRecipe;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.survivalism.drying")
@ZenRegister
public class DryingRecipeTweaker {

    @ZenMethod
    public static void addDryingRecipe(String identifier, IIngredient input, IItemStack output, int ticks) {
        Survivalism.LATE_ADDITIONS.add(new AddDryingRecipe(identifier, input, output, ticks));
    }

    private static class AddDryingRecipe implements IAction {
        String identifier;
        String inputCMDString;
        Ingredient input;
        ItemStack output;
        int ticks;

        AddDryingRecipe(String identifier, IIngredient input, IItemStack output, int ticks) {
            this.identifier = identifier;
            this.inputCMDString = input.toCommandString();
            this.input = CraftTweakerMC.getIngredient(input);
            this.output = CraftTweakerMC.getItemStack(output);
            this.ticks = ticks;
        }

        @Override
        public void apply() {
            DryingRackRecipeManager.register(new DryingRecipe(new ResourceLocation("Crafttweaker", identifier), input, output, ticks));
        }

        @Override
        public String describe() {
            return "Added Recipe for: " + inputCMDString + " -> " + output.getDisplayName() + " with Identifier: " + "Crafttweaker:" + identifier;
        }
    }
}
