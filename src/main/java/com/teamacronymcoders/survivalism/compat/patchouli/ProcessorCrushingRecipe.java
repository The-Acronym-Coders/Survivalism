package com.teamacronymcoders.survivalism.compat.patchouli;

import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipeManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorCrushingRecipe implements IComponentProcessor {

    private Ingredient ingredient;
    private ItemStack fluid;
    private ItemStack output;
    private CrushingRecipe recipe;

    @Override
    public void setup(IVariableProvider<String> iVariableProvider) {
        ResourceLocation id = new ResourceLocation(iVariableProvider.get("recipeID"));
        this.recipe = CrushingRecipeManager.getRecipeByID(id);
    }

    private void initRecipeVars() {
        ingredient = recipe.getInput();
        fluid = FluidUtil.getFilledBucket(recipe.getOutput());
        if (recipe.getOutputStack() != ItemStack.EMPTY || recipe.getOutputStack() != null) {
            output = recipe.getOutputStack();
        } else {
            ItemStack stack = new ItemStack(Blocks.BARRIER);
            stack.setStackDisplayName("No Output");
            output = stack;
        }
    }

    @Override
    public String process(String s) {
        initRecipeVars();
        switch (s) {
            case "name":
                return recipe.getOutput().getLocalizedName();
            case "input":
                return PatchouliAPI.instance.serializeIngredient(ingredient);
            case "fluid":
                return PatchouliAPI.instance.serializeItemStack(fluid);
            case "output":
                return PatchouliAPI.instance.serializeItemStack(output);
            case "amount":
                return recipe.getOutput().amount + "mb";
            case "chance":
                if (recipe.getOutputStack() != ItemStack.EMPTY) {
                    if (recipe.getItemChance() <= 0 || recipe.getItemChance() >= 1) {
                        return "100%";
                    }
                    return recipe.getItemChance() * 100 + "%";
                }
            case "jumps":
                return "Jumps: " + recipe.getJumps();
        }
        return null;
    }
}
