package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.CompostMaterialsAPI;
import com.teamacronymcoders.survivalism.Survivalism;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.survivalism.Compost")
@ZenRegister
public class CompostTweaker {
    @ZenMethod
    public static void addCompostMaterial(IIngredient ingredient) {
        Survivalism.LATE_ADDITIONS.add(new CompostAddition(ingredient));
    }

    @ZenMethod
    public static void removeCompostMaterial(IIngredient ingredient) {
        Survivalism.LATE_REMOVALS.add(new CompostRemoval(ingredient));
    }

    private static class CompostAddition implements IAction {
        Ingredient ingredient;
        String commandString;

        CompostAddition(IIngredient ingredient) {
            this.ingredient = CraftTweakerMC.getIngredient(ingredient);
            this.commandString = ingredient.toCommandString();
        }

        @Override
        public void apply() {
            CompostMaterialsAPI.addMaterialToList(ingredient);
        }

        @Override
        public String describe() {
            return "Added Matching ItemStacks From: " + commandString + " As Valid Compost Materials!";
        }
    }

    private static class CompostRemoval implements IAction {
        Ingredient ingredient;
        String commandString;

        CompostRemoval(IIngredient ingredient) {
            this.ingredient = CraftTweakerMC.getIngredient(ingredient);
            this.commandString = ingredient.toCommandString();
        }

        @Override
        public void apply() {
            CompostMaterialsAPI.removeMaterialFromList(ingredient);
        }

        @Override
        public String describe() {
            return "Removed Matching ItemStacks From: " + commandString + " As Valid Compost Materials!";
        }
    }
}
