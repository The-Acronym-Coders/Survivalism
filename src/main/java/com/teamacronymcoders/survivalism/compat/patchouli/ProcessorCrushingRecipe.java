package com.teamacronymcoders.survivalism.compat.patchouli.componentProcessors;

import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipeManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorCrushingRecipe implements IComponentProcessor {

    private Ingredient ingredient;
    private ItemStack fluid;
    private ItemStack output;

    private VatRecipe recipe;

    @Override
    public void setup(IVariableProvider<String> iVariableProvider) {
        ResourceLocation id = new ResourceLocation(iVariableProvider.get("recipeID"));
        this.recipe = VatRecipeManager.getRecipeByID(id);
    }

    private void initRecipeVars() {
        ingredient = recipe.getInput();
        fluid = new ItemStack(Items.BUCKET, 1);
        fluid.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).fill(new FluidStack(recipe.getOutput().getFluid(), 1000), true);
        output = recipe.getOutputStack();
    }

    @Override
    public String process(String s) {
        initRecipeVars();
        switch (s) {
            case "input":
                return PatchouliAPI.instance.serializeIngredient(ingredient);
            case "fluid":
                return PatchouliAPI.instance.serializeItemStack(fluid);
            case "output":
                return PatchouliAPI.instance.serializeItemStack(output);
            case "chance":
                if (recipe.getItemChance() <= 0 || recipe.getItemChance() >= 1) {
                    return "100%";
                }
                return recipe.getItemChance() * 100 + "%";
            case "jumps":
                return String.valueOf(recipe.getJumps());
        }
        return null;
    }
}
