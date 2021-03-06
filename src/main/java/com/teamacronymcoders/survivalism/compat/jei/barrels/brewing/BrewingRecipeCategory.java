package com.teamacronymcoders.survivalism.compat.jei.barrels.brewing;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrewingRecipeCategory implements IRecipeCategory<BrewingRecipeWrapper> {
    public static final String NAME = "survivalism.brewing";
    @Nonnull
    private final String localized;
    private final IDrawable background;

    public BrewingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_brewing_no_inv.png"), 26, 8, 124, 80);
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
    public void setRecipe(IRecipeLayout recipeLayout, BrewingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        // Init Slots
        recipeLayout.getFluidStacks().init(0, true, 18, 9, 16, 47, SurvivalismConfigs.brewingInputSize, true, null);
        recipeLayout.getFluidStacks().init(1, false, 90, 9, 16, 47, SurvivalismConfigs.brewingOutputSize, true, null);


        // Set Slots
        recipeLayout.getFluidStacks().set(0, recipeWrapper.recipe.getInput());
        recipeLayout.getFluidStacks().set(1, recipeWrapper.recipe.getOutput());


        // Triple Slot Madness~~
        Set<Ingredient> ingredientSet = new HashSet<>(recipeWrapper.recipe.getInputItems());
        Ingredient[] array = ingredientSet.toArray(new Ingredient[0]);

        int[] val = new int[]{8, 28, 48};

        for (int i = 0; i < array.length; i++) {
            List<ItemStack> stacks = Arrays.asList(array[i].getMatchingStacks());
            recipeLayout.getItemStacks().init(i, true, 53, val[i]);
            recipeLayout.getItemStacks().set(i, stacks);
        }
    }
}
