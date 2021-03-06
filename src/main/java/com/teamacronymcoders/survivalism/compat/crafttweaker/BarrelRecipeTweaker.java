package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBiome;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.survivalism.Barrel")
@ZenRegister
public class BarrelRecipeTweaker {

    @ZenMethod
    public static void addSoakingRecipe(String identifier, ILiquidStack input, IIngredient inputItem, IItemStack output, int ticks, @Optional float fluidUseChance) {
        Survivalism.LATE_ADDITIONS.add(new AddSoakingRecipe(identifier, input, inputItem, output, fluidUseChance, ticks));
    }

    @ZenMethod
    public static void addBrewingRecipe(String identifier, ILiquidStack input, IIngredient[] inputItems, ILiquidStack output, int ticks) {
        Survivalism.LATE_ADDITIONS.add(new AddBrewingRecipe(identifier, input, inputItems, output, ticks));
    }

    @ZenMethod
    public static void addBiomeFluid(IBiome biome, ILiquidStack stack) {
        Survivalism.LATE_ADDITIONS.add(new AddBiomeFluid(biome, stack));
    }

    private static class AddBrewingRecipe implements IAction {
        String name;
        FluidStack input;
        List<Ingredient> inputItems = new ArrayList<>();
        FluidStack output;
        int ticks;
        List<String> names = new ArrayList<>();

        AddBrewingRecipe(String identifier, ILiquidStack input, IIngredient[] inputItems, ILiquidStack output, int ticks) {
            this.input = CraftTweakerMC.getLiquidStack(input);
            for (int i = 0; i < inputItems.length; i++) {
                if (CraftTweakerMC.getIngredient(inputItems[i]) != null) {
                    this.inputItems.add(CraftTweakerMC.getIngredient(inputItems[i]));
                    names.add(inputItems[i].toCommandString());
                } else {
                    CraftTweakerAPI.logError("Ingredient: " + inputItems[i].toCommandString() + ", Is Null or Invalid");
                }
            }
            this.output = CraftTweakerMC.getLiquidStack(output);
            this.ticks = ticks;
            this.name = identifier;
        }

        @Override
        public void apply() {
            BarrelRecipeManager.register(new BrewingRecipe(new ResourceLocation(CraftTweaker.MODID, name), input, inputItems, output, ticks));
        }

        @Override
        public String describe() {
            StringBuilder sb = new StringBuilder();
            if (SurvivalismConfigs.crtVerboseLogging) {
                sb.append("Added Brewing Recipe: ").append("\n");
                sb.append("Input Fluidstack: ").append(input.getLocalizedName()).append(":").append(input.amount).append("\n");
                sb.append("Input Ingredients: ").append("\n");
                for (String name : names) {
                    sb.append(name).append("\n");
                }
                sb.append("Output Fluid: ").append(output.getLocalizedName()).append(":").append(output.amount).append("\n");
                sb.append("Ticks: ").append(ticks).append("\n");
            } else {
                sb.append("Added Brewing Recipe: ").append(" ");
                sb.append(input.getLocalizedName()).append(" : ");
                for (String name : names) {
                    sb.append(name).append(" : ");
                }
                sb.append(output.getLocalizedName());
            }
            return sb.toString();
        }
    }

    private static class AddSoakingRecipe implements IAction {
        String name;
        String itemDesc;
        String outDesc;
        FluidStack input;
        Ingredient inputItem;
        ItemStack output;
        float fluidUseChance;
        int ticks;

        AddSoakingRecipe(String identifier, ILiquidStack input, IIngredient inputItem, IItemStack output, float fluidUseChance, int ticks) {
            this.name = identifier;
            this.itemDesc = inputItem.toCommandString();
            this.outDesc = output.toCommandString();
            this.input = CraftTweakerMC.getLiquidStack(input);
            this.inputItem = CraftTweakerMC.getIngredient(inputItem);
            this.output = CraftTweakerMC.getItemStack(output);
            this.ticks = ticks;
            this.fluidUseChance = fluidUseChance;
        }

        @Override
        public void apply() {
            SoakingRecipe recipe;
            if (fluidUseChance != 0.0f) {
                recipe = new SoakingRecipe(new ResourceLocation(CraftTweaker.MODID, name), input, inputItem, output, fluidUseChance, ticks);
            } else {
                recipe = new SoakingRecipe(new ResourceLocation(CraftTweaker.MODID, name), input, inputItem, output, ticks);
            }
            BarrelRecipeManager.register(recipe);
        }

        @Override
        public String describe() {
            StringBuilder sb = new StringBuilder();
            if (SurvivalismConfigs.crtVerboseLogging) {
                sb.append("Added Soaking Recipe: ").append("\n");
                sb.append("Input Fluid: ").append(input.getLocalizedName()).append(":").append(input.amount).append("\n");
                sb.append("Input Ingredient: ").append(itemDesc).append("\n");
                sb.append("Output ItemStack: ").append(outDesc).append("\n");
                sb.append("Fluid Use Chance: ").append(fluidUseChance).append("\n");
                sb.append("Ticks: ").append(ticks).append("\n");
            } else {
                sb.append("Added Soaking Recipe for: ").append(" ");
                sb.append(input.getLocalizedName()).append(" : ").append(itemDesc).append(" : ").append(outDesc);
            }
            return sb.toString();
        }
    }

    private static class AddBiomeFluid implements IAction {
        Biome biome;
        FluidStack stack;

        AddBiomeFluid(IBiome biome, ILiquidStack stack) {
            this.biome = CraftTweakerMC.getBiome(biome);
            this.stack = CraftTweakerMC.getLiquidStack(stack);
        }

        @Override
        public void apply() {
            BarrelRecipeManager.register(biome, stack);
        }

        @Override
        public String describe() {
            return "Added Biome/Fluid Pairing of: " + biome.getRegistryName() + " -> " + stack.getLocalizedName() + ":" + stack.amount;
        }
    }
}
