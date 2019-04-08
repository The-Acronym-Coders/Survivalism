package com.teamacronymcoders.survivalism.compat.patchouli.barrel;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.PatchouliHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorSoakingRecipe implements IComponentProcessor {

    private Ingredient ingredient;
    private ItemStack fluid;
    private ItemStack stack;
    private SoakingRecipe recipe;

    @Override
    public void setup(IVariableProvider<String> iVariableProvider) {
        ResourceLocation id = new ResourceLocation(iVariableProvider.get("recipeID"));
        recipe = BarrelRecipeManager.getSoakingRecipe(id);
    }

    @Override
    public String process(String s) {
        initRecipeVars();
        switch (s) {
            case "name":
                return recipe.getOutput().getDisplayName();
            case "inputFluid":
                return PatchouliAPI.instance.serializeItemStack(fluid);
            case "amountI":
                return I18n.format("survivalism.patchouli.amount", recipe.getInput().amount);
            case "ingredient":
                return PatchouliAPI.instance.serializeIngredient(ingredient);
            case "itemstack":
                return PatchouliAPI.instance.serializeItemStack(stack);
            case "decreaseChance":
                if (recipe.getFluidUseChance() < 0.0f || recipe.getFluidUseChance() > 1.0f) {
                    return I18n.format("survivalism.patchouli.chance") + " " + "100%";
                }
                return I18n.format("survivalism.patchouli.chance") + " " + recipe.getFluidUseChance() * 100 + "%";
            case "time_label":
                return I18n.format("survivalism.patchouli.time");
            case "time":
                return PatchouliHelper.getDurationString(recipe.getTicks() / 20);
        }
        return null;
    }

    private void initRecipeVars() {
        ingredient = recipe.getInputItem();
        fluid = FluidUtil.getFilledBucket(recipe.getInput());
        stack = recipe.getOutput();
    }
}
