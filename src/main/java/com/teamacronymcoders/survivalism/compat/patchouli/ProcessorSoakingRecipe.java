package com.teamacronymcoders.survivalism.compat.patchouli.componentProcessors;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperPatchouli;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
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

    private void initRecipeVars() {
        ingredient = recipe.getInputItem();
        fluid = new ItemStack(Items.BUCKET, 1);
        fluid.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).fill(new FluidStack(recipe.getInput().getFluid(), 1000), true);
        stack = recipe.getOutput();
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
                return recipe.getInput().amount + "mb";
            case "ingredient":
                return PatchouliAPI.instance.serializeIngredient(ingredient);
            case "itemstack":
                return PatchouliAPI.instance.serializeItemStack(stack);
            case "decreaseAmount":
                return "Decrease Amount: " + recipe.getFluidUseChance();
            case "decreaseChance":
                if (recipe.getFluidUseChance() < 0.0f || recipe.getFluidUseChance() > 1.0f) {
                    return I18n.format("survivalism.patchouli.amount.decrease") + " " + "100%";
                }
                return I18n.format("survivalism.patchouli.chance") + " " + recipe.getFluidUseChance() * 100 + "%";
            case "time_label":
                return I18n.format("survivalism.patchouli.time");
            case "time":
                return HelperPatchouli.getDurationString(recipe.getTicks() / 20);
        }
        return null;
    }
}
