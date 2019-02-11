package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipeManager;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.survivalism.CrushingVat")
@ZenRegister
public class VatRecipeTweaker {

    @ZenMethod
    public static void addCrushingRecipe(IIngredient input, ILiquidStack output, int jumps, @Optional IItemStack outputStack, @Optional float itemChance) {
        Survivalism.LATE_ADDITIONS.add(new AddRecipe(input, output, outputStack, itemChance, jumps));
    }

    @ZenMethod
    public static void addCrushingMultiplierBoots(IIngredient boots, double multiplier) {
        Survivalism.LATE_ADDITIONS.add(new AddBoots(boots, multiplier));
    }

    private static class AddRecipe implements IAction {

        String name;
        Ingredient input;
        FluidStack output;
        ItemStack outputStack;
        float itemChance;
        int jumps;

        private AddRecipe(IIngredient input, ILiquidStack output, IItemStack outputStack, float itemChance, int jumps) {
            this.name = HelperString.cleanCommandString(input.toCommandString());
            this.input = CraftTweakerMC.getIngredient(input);
            this.output = CraftTweakerMC.getLiquidStack(output);
            this.outputStack = CraftTweakerMC.getItemStack(outputStack);
            this.itemChance = itemChance;
            this.jumps = jumps;
        }

        @Override
        public void apply() {
            ResourceLocation name = new ResourceLocation(CraftTweaker.MODID, this.name);
            VatRecipe recipe = new VatRecipe(name, input, output, outputStack, itemChance, jumps);
            VatRecipeManager.register(recipe);
        }

        @Override
        public String describe() {
            StringBuilder sb = new StringBuilder();
            sb.append("Added Crushing Recipe:").append(" ");
            sb.append("Input: ").append(name).append(" ");
            sb.append("Output: ").append(output.getLocalizedName()).append(" ");
            if (outputStack != null && !outputStack.isEmpty()) {
                sb.append("Output Stack: ").append(outputStack.getDisplayName()).append(" ");
            }
            if (itemChance > 0) {
                sb.append("Item Chance: ").append(itemChance).append(" ");
            }
            sb.append("Jumps: ").append(jumps).append(" ");
            return sb.toString();
        }
    }

    private static class AddBoots implements IAction {

        String desc;
        Ingredient boots;
        double multiplier;

        AddBoots(IIngredient boots, double multiplier) {
            this.boots = CraftTweakerMC.getIngredient(boots);
            this.multiplier = multiplier;
            this.desc = boots.toCommandString();
        }

        @Override
        public void apply() {
            if (boots != Ingredient.EMPTY) {
                CraftTweakerAPI.logError("Cannot register boot multiplier for nothing.");
            }
            VatRecipeManager.registerBoots(boots, multiplier);
        }

        @Override
        public String describe() {
            return "Added Multiplier: " + multiplier + " To Boots: " + desc;
        }
    }
}
