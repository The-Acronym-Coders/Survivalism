package com.teamacronymcoders.survivalism.compat.crafttweaker.vats;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipeManager;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.survivalism.MixingVat")
@ZenRegister
public class MixingRecipeTweaker {
    @ZenMethod
    public static void addMixingRecipe(String id, ILiquidStack main, ILiquidStack output, int clicks, @Optional ILiquidStack secondary, @Optional IIngredient catalyst) {
        Survivalism.LATE_ADDITIONS.add(new AddMixingRecipe(id, main, output, clicks, secondary, catalyst));
    }

    private static class AddMixingRecipe implements IAction {
        ResourceLocation id;
        FluidStack main;
        FluidStack secondary = null;
        FluidStack output;
        Ingredient catalyst = Ingredient.EMPTY;
        String catalystId = "null";
        int clicks;

        AddMixingRecipe(String id, ILiquidStack main, ILiquidStack output, int clicks, ILiquidStack secondary, IIngredient catalyst) {
            this.id = new ResourceLocation("Crafttweaker", id);
            this.main = CraftTweakerMC.getLiquidStack(main);
            this.output = CraftTweakerMC.getLiquidStack(output);
            this.clicks = clicks;
            if (CraftTweakerMC.getLiquidStack(secondary) != null) {
                this.secondary = CraftTweakerMC.getLiquidStack(secondary);
            }
            if (CraftTweakerMC.getIngredient(catalyst) != Ingredient.EMPTY) {
                this.catalyst = CraftTweakerMC.getIngredient(catalyst);
                this.catalystId = catalyst.toCommandString();
            }
        }

        @Override
        public void apply() {
            if (secondary != null && catalyst != Ingredient.EMPTY) {
                MixingRecipeManager.register(new MixingRecipe(id, main, secondary, catalyst, output, clicks));
            } else if (secondary != null) {
                MixingRecipeManager.register(new MixingRecipe(id, main, secondary, output, clicks));
            } else if (catalyst != Ingredient.EMPTY) {
                MixingRecipeManager.register(new MixingRecipe(id, main, catalyst, output, clicks));
            }
        }

        @Override
        public String describe() {
            return "Registered Mixing Recipe: " + main.getLocalizedName() + " + " + secondary.getLocalizedName() + ":" + catalystId + " = " + output.getLocalizedName();
        }
    }
}
