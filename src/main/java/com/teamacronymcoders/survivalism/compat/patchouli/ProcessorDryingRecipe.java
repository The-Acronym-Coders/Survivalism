package com.teamacronymcoders.survivalism.compat.patchouli;

import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRackRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.PatchouliHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorDryingRecipe implements IComponentProcessor {
    private DryingRecipe recipe;

    @Override
    public void setup(IVariableProvider<String> iVariableProvider) {
        ResourceLocation id = new ResourceLocation(iVariableProvider.get("recipeID"));
        DryingRackRecipeManager.getDryingRecipe(id);
    }

    @Override
    public String process(String s) {
        switch (s) {
            case "input":
                return PatchouliAPI.instance.serializeIngredient(recipe.getInput());
            case "output":
                return PatchouliAPI.instance.serializeItemStack(recipe.getOutput());
            case "time_label":
                return I18n.format("survivalism.patchouli.time");
            case "time":
                return PatchouliHelper.getDurationString(recipe.getTicks() / 20);
        }
        return null;
    }
}
