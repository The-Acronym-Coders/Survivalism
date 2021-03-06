package com.teamacronymcoders.survivalism.compat.jei.vats.crushing.multiplier;

import com.teamacronymcoders.survivalism.Survivalism;
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

public class MultiplierValueCategory implements IRecipeCategory<MultiplierValueWrapper> {
    public static final String NAME = "survivalism.multiplier";
    @Nonnull
    private final String localized;
    private final IDrawable background;

    public MultiplierValueCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Survivalism.MODID, "textures/gui/crushing_multiplier.png"), 48, 36, 80, 30);
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
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MultiplierValueWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 31, 0);
        // Set Slots
        ItemStack[] stacks = recipeWrapper.value.getKey().getMatchingStacks();
        List<ItemStack> stackList = new ArrayList<>(Arrays.asList(stacks));
        recipeLayout.getItemStacks().set(0, stackList);
    }
}
