package com.teamacronymcoders.survivalism.compat.jei.crushing;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipeManager;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrushingRecipeCategory implements IRecipeCategory<CrushingRecipeWrapper> {
    public static final String NAME = "survivalism.crushing";
    static int recipeWidth;
    @Nonnull
    private final String localized;
    private final String localizedOutputError;
    private final IDrawable background;
    private final Object2DoubleMap<Ingredient> doubleMap = CrushingRecipeManager.getBOOTS();

    public CrushingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Survivalism.MODID, "textures/gui/barrel_soaking_no_inv.png"), 26, 12, 124, 90);
        this.localized = I18n.format("container.jei." + NAME + ".name");
        this.localizedOutputError = I18n.format("survivalism.itemstack.no.output");
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
    public void setRecipe(IRecipeLayout recipeLayout, CrushingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        // Init Slots
        recipeLayout.getItemStacks().init(0, true, 17, 27);
        recipeLayout.getFluidStacks().init(0, true, 54, 12, 16, 47, SurvivalismConfigs.crushingTankSize, true, null);
        recipeLayout.getItemStacks().init(1, false, 89, 27);

        // Set Slots
        List<ItemStack> stackList = new ArrayList<>(Arrays.asList(recipeWrapper.recipe.getInput().getMatchingStacks()));
        recipeLayout.getItemStacks().set(0, stackList);
        recipeLayout.getFluidStacks().set(0, recipeWrapper.recipe.getOutput());
        if (!recipeWrapper.recipe.getOutputStack().isEmpty()) {
            recipeLayout.getItemStacks().set(1, recipeWrapper.recipe.getOutputStack());
        } else {
            ItemStack stack = new ItemStack(Blocks.BARRIER);
            stack.setStackDisplayName(localizedOutputError);
            recipeLayout.getItemStacks().set(1, stack);
        }
    }
}
