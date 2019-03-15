package com.teamacronymcoders.survivalism.compat.jei;

import com.teamacronymcoders.survivalism.Survivalism;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DryingRackRecipeCategory implements IRecipeCategory<DryingRackRecipeWrapper> {

    public static final String NAME = "survivalism.drying";
    @Nonnull
    private final String localized;
    private final IDrawable background;
    private final String localizedOutputError;

    public DryingRackRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Survivalism.MODID, "textures/gui/drying_rack.png"), 38, 12, 100, 70);
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
    public void setRecipe(IRecipeLayout recipeLayout, DryingRackRecipeWrapper recipeWrapper, IIngredients ingredients) {
        // Init Slots
        recipeLayout.getItemStacks().init(0, true, 20, 24);
        recipeLayout.getItemStacks().init(1, false, 62, 24);

        // Set Slots
        List<ItemStack> stacks =  new ArrayList<>(Arrays.asList(recipeWrapper.recipe.getInput().getMatchingStacks()));
        recipeLayout.getItemStacks().set(0, stacks);
        recipeLayout.getItemStacks().set(1, recipeWrapper.recipe.getOutput());
    }
}
