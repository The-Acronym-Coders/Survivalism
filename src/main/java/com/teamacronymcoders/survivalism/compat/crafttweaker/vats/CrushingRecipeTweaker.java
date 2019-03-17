package com.teamacronymcoders.survivalism.compat.crafttweaker.vats;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipeManager;
import com.teamacronymcoders.survivalism.compat.gamestages.CrushingHandler;
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
public class CrushingRecipeTweaker {

    @ZenMethod
    public static void addCrushingRecipe(String identifier, IIngredient input, ILiquidStack output, int jumps, @Optional IItemStack outputStack, @Optional float itemChance) {
        Survivalism.LATE_ADDITIONS.add(new AddRecipe(identifier, input, output, outputStack, itemChance, jumps));
    }

    @ZenMethod
    public static void addCrushingMultiplierBoots(IIngredient boots, double multiplier) {
        Survivalism.LATE_ADDITIONS.add(new AddBoots(boots, multiplier));
    }

    @ZenMethod
    public static void addGeneralRequirements(String... requirements) {
        CrushingHandler.addGeneralRequirements(requirements);
    }

    @ZenMethod
    public static void addIngredientRequirements(IIngredient input, String... requirements) {
        Survivalism.LATE_ADDITIONS.add(new AddRequirementToItem(input, requirements));
    }

    private static class AddRecipe implements IAction {
        String name;
        Ingredient input;
        FluidStack output;
        ItemStack outputStack;
        float itemChance;
        int jumps;

        private AddRecipe(String identifier, IIngredient input, ILiquidStack output, IItemStack outputStack, float itemChance, int jumps) {
            this.name = identifier;
            this.input = CraftTweakerMC.getIngredient(input);
            this.output = CraftTweakerMC.getLiquidStack(output);
            this.outputStack = CraftTweakerMC.getItemStack(outputStack);
            this.itemChance = itemChance;
            this.jumps = jumps;
        }

        @Override
        public void apply() {
            ResourceLocation name = new ResourceLocation(CraftTweaker.MODID, this.name);
            CrushingRecipe recipe;
            if (outputStack != ItemStack.EMPTY && itemChance != 0.0f) {
                recipe = new CrushingRecipe(name, input, output, outputStack, itemChance, jumps);
            } else if (outputStack != ItemStack.EMPTY) {
                recipe = new CrushingRecipe(name, input, output, outputStack, jumps);
            } else {
                recipe = new CrushingRecipe(name, input, output, jumps);
            }
            CrushingRecipeManager.register(recipe);
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
            if (boots == Ingredient.EMPTY) {
                CraftTweakerAPI.logError("Cannot register boot multiplier for nothing.");
            }
            CrushingRecipeManager.registerBoots(boots, multiplier);
        }

        @Override
        public String describe() {
            return "Added Multiplier: " + multiplier + " To Boots: " + desc;
        }
    }

    private static class AddRequirementToItem implements IAction {
        Ingredient input;
        String[] requirements;

        AddRequirementToItem(IIngredient input, String[] requirements) {
            this.input = CraftTweakerMC.getIngredient(input);
            this.requirements = requirements;
        }

        @Override
        public void apply() {
            for (ItemStack stack : input.getMatchingStacks()) {
                CrushingHandler.addRequirementToItemStack(stack, requirements);
            }
        }

        @Override
        public String describe() {
            return null;
        }
    }
}
