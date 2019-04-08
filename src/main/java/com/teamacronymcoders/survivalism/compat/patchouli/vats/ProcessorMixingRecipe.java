package com.teamacronymcoders.survivalism.compat.patchouli.vats;

import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipeManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorMixingRecipe implements IComponentProcessor {
    private ItemStack inputFluid;
    private ItemStack secondaryFluid;
    private ItemStack outputFluid;
    private MixingRecipe recipe;

    @Override
    public void setup(IVariableProvider<String> iVariableProvider) {
        ResourceLocation id = new ResourceLocation(iVariableProvider.get("recipeID"));
        recipe = MixingRecipeManager.getRecipeByID(id);
    }

    private void initRecipeVars() {
        inputFluid = FluidUtil.getFilledBucket(recipe.getMain());
        if (recipe.getSecondary() != null) {
            secondaryFluid = FluidUtil.getFilledBucket(recipe.getSecondary());
        }
        outputFluid = FluidUtil.getFilledBucket(recipe.getOutput());
    }

    @Override
    public String process(String s) {
        initRecipeVars();
        switch (s) {
            case "name":
                return recipe.getOutput().getLocalizedName();
            case "input":
                return PatchouliAPI.instance.serializeItemStack(inputFluid);
            case "inputAmount":
                return I18n.format("survivalism.patchouli.amount", recipe.getMain().amount);
            case "secondary":
                return PatchouliAPI.instance.serializeItemStack(secondaryFluid);
            case "secondaryAmount":
                return I18n.format("survivalism.patchouli.amount", recipe.getSecondary().amount);
            case "catalyst":
                if (recipe.getCatalyst() != null) {
                    return PatchouliAPI.instance.serializeIngredient(recipe.getCatalyst());
                } else {
                    ItemStack stack = new ItemStack(Item.getItemFromBlock(Blocks.BARRIER));
                    stack.setStackDisplayName(I18n.format("survivalism.itemstack.no.output"));
                    return PatchouliAPI.instance.serializeItemStack(stack);
                }
            case "output":
                return PatchouliAPI.instance.serializeItemStack(outputFluid);
            case "outputAmount":
                return I18n.format("survivalism.patchouli.amount", recipe.getOutput().amount);
            case "clicks":
                return I18n.format("survivalism.patchouli.clicks", recipe.getClicks());
        }
        return null;
    }
}
