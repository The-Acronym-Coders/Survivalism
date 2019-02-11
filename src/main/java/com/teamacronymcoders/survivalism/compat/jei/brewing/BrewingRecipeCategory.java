package com.teamacronymcoders.survivalism.compat.jei.brewing;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.gui.GUIBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.*;

public class BrewingRecipeCategory implements IRecipeCategory<BrewingRecipeWrapper> {

    public static final String NAME = "survivalism.brewing";

    private final IDrawable background;
    private final IDrawable icon;
    IGuiHelper helper;

    public BrewingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(GUIBarrel.brewing_background, 30, 13, 116, 63);
        this.icon = helper.createDrawable(new ModelResourceLocation("survivalism:barrel", "inventory"), 16, 16, 16, 16);
        this.helper = helper;
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return I18n.format("container.jei." + NAME + ".name");
    }

    @Override
    public String getModName() {
        return Survivalism.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BrewingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        float hr = 48f / 16000f;
        int outputTank = recipeWrapper.recipe.getOutput().amount;
        float offset = outputTank * hr;
        int y = Math.round(65 - offset);
        int h = Math.round(offset - 1);

        recipeLayout.getFluidStacks().init(0, true, 14, y - 13, 16, h, TileBarrel.TANK_CAPACITY, true, null);
        recipeLayout.getFluidStacks().set(0, recipeWrapper.recipe.getInput());

        Set<Ingredient> ingredientSet = recipeWrapper.recipe.getInputItems().keySet();
        Ingredient[] array = ingredientSet.toArray(new Ingredient[0]);

        int[] val = new int[]{3, 23, 43};

        for (int i = 0; i < array.length; i++) {
            ItemStack[] stacks = array[i].getMatchingStacks();
            List<ItemStack> fxStacks = new LinkedList<>();
            for (ItemStack stack : stacks) {
                int count = recipeWrapper.recipe.getInputItems().get(array[i]);
                stack.setCount(count);
                fxStacks.add(stack);
            }
            recipeLayout.getItemStacks().init(i, true, 49, val[i]);
            recipeLayout.getItemStacks().set(i, fxStacks);
        }

        recipeLayout.getFluidStacks().init(1, false, 86, y - 13, 16, h, TileBarrel.TANK_CAPACITY, true, null);
        recipeLayout.getFluidStacks().set(1, recipeWrapper.recipe.getOutput());
    }
}
