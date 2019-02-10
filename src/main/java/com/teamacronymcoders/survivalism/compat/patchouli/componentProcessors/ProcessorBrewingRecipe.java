package com.teamacronymcoders.survivalism.compat.patchouli.componentProcessors;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperPatchouli;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcessorBrewingRecipe implements IComponentProcessor {

    private ItemStack inputFluid;
    private ItemStack outputFluid;
    private BrewingRecipe recipe;
    private List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public void setup(IVariableProvider<String> iVariableProvider) {
        ResourceLocation id = new ResourceLocation(iVariableProvider.get("recipeID"));
        BarrelRecipeManager.getBrewingRecipe(id);
    }

    private void initRecipeVars() {
        inputFluid = FluidUtil.getFilledBucket(recipe.getInput());
        Set<Ingredient> keys = recipe.getInputItems().keySet();
        ingredients.addAll(keys);
        outputFluid = FluidUtil.getFilledBucket(recipe.getOutput());
    }

    @Override
    public String process(String s) {
        initRecipeVars();
        switch (s) {
            case "name":
                return recipe.getOutput().getLocalizedName();
            case "inputFluid":
                return PatchouliAPI.instance.serializeItemStack(inputFluid);
            case "amountI":
                return recipe.getInput().amount + "mb";
            case "ingredient0":
                return PatchouliAPI.instance.serializeIngredient(ingredients.get(0));
            case "ingredient1":
                return PatchouliAPI.instance.serializeIngredient(ingredients.get(1));
            case "ingredient2":
                return PatchouliAPI.instance.serializeIngredient(ingredients.get(2));
            case "outputFluid":
                return PatchouliAPI.instance.serializeItemStack(outputFluid);
            case "amountO":
                return recipe.getOutput().amount + "mb";
            case "time_label":
                return I18n.format("epicurious.patchouli.time");
            case "time":
                return HelperPatchouli.getDurationString(recipe.getTicks() / 20);
        }
        return null;
    }


}
