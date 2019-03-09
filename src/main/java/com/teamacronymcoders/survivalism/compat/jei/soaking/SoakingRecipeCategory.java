package com.teamacronymcoders.survivalism.compat.jei.soaking;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoakingRecipeCategory implements IRecipeCategory<SoakingRecipeWrapper> {
    public static final String NAME = "survivalism.soaking";
    @Nonnull
    private final String localized;
    private final IDrawable background;

    public SoakingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking_no_inv.png"), 26, 12, 124, 70);
        this.localized = I18n.format("container.jei." + NAME + ".name");
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
    public void setRecipe(IRecipeLayout recipeLayout, SoakingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        // Init Slots
        recipeLayout.getItemStacks().init(0, true, 17, 27);
        recipeLayout.getFluidStacks().init(0, true, 54, 12, 16, 47, SurvivalismConfigs.soakingTankSize, true, null);
        recipeLayout.getItemStacks().init(1, false, 89, 27);

        // Set Slots
        List<ItemStack> stackList = new ArrayList<>(Arrays.asList(recipeWrapper.recipe.getInputItem().getMatchingStacks()));
        recipeLayout.getItemStacks().set(0, stackList);

        recipeLayout.getFluidStacks().set(0, recipeWrapper.recipe.getInput());
        recipeLayout.getItemStacks().set(1, recipeWrapper.recipe.getOutput());
    }
}
