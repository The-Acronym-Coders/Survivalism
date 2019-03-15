package com.teamacronymcoders.survivalism.compat.jei.vats.mixing;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MixingRecipeCategory implements IRecipeCategory<MixingRecipeWrapper> {

    public static final String NAME = "survivalism.mixing";
    @Nonnull
    private final String localized;
    private final IDrawable background;
    private final String localizedOutputError;

    public MixingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Survivalism.MODID, "textures/gui/mixing_vat_no_inv.png"), 20, 12, 134, 70);
        this.localized = I18n.format("container.jei." + NAME + ".name");
        this.localizedOutputError = I18n.format("survivalism.itemstack.no.input");
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return localized;
    }

    @Override
    public String getModName() {
        return Survivalism.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MixingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        // Init Slots
        recipeLayout.getItemStacks().init(0, true, 59, 27);
        recipeLayout.getFluidStacks().init(0, true, 6, 11, 16, 47, SurvivalismConfigs.mixingInputTankSize, true, null);
        recipeLayout.getFluidStacks().init(1, true, 24, 11, 16, 47, SurvivalismConfigs.mixingSecondaryTankSize, true, null);
        recipeLayout.getFluidStacks().init(2, true, 114, 11, 16, 47, SurvivalismConfigs.mixingOutputTankSize, true, null);

        // Set Slots
        recipeLayout.getFluidStacks().set(0, recipeWrapper.recipe.getMain());
        if (recipeWrapper.recipe.getSecondary() != null) {
            recipeLayout.getFluidStacks().set(1, recipeWrapper.recipe.getSecondary());
        }
        if (recipeWrapper.recipe.getCatalyst() != Ingredient.EMPTY) {
            List<ItemStack> stacks =  new ArrayList<>(Arrays.asList(recipeWrapper.recipe.getCatalyst().getMatchingStacks()));
            recipeLayout.getItemStacks().set(0, stacks);
        } else {
            ItemStack stack = new ItemStack(Blocks.BARRIER);
            stack.setStackDisplayName(localizedOutputError);
            recipeLayout.getItemStacks().set(0, stack);
        }
        if (recipeWrapper.recipe.getOutput() != null) {
            recipeLayout.getFluidStacks().set(2, recipeWrapper.recipe.getOutput());
        }
    }
}
