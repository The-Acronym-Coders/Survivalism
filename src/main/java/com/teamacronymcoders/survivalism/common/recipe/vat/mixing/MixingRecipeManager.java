package com.teamacronymcoders.survivalism.common.recipe.vat.mixing;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixingRecipeManager {
    private static final Map<ResourceLocation, MixingRecipe> RECIPES = new HashMap<>();

    public static List<MixingRecipe> getRecipes() {
        return ImmutableList.copyOf(RECIPES.values());
    }

    public static MixingRecipe getRecipeByID(ResourceLocation id) {
        return RECIPES.get(id);
    }

    public static void register(MixingRecipe recipe) {
        Preconditions.checkNotNull(recipe.getId(), "Cannot register Mixing Recipe with null Identifier.");
        Preconditions.checkNotNull(recipe.getMain(), "Cannot register Mixing Recipe with null Main Fluidstack.");
        Preconditions.checkNotNull(recipe.getOutput(), "Cannot register Mixing Recipe with null Output Fluidstack.");
        if (recipe.secondary == null && recipe.catalyst == Ingredient.EMPTY) {
            throw new NullPointerException("Cannot register Mixing Recipe with both null Secondary Fluidstack and Ingredient Catalyst, One must be provided!");
        }
        RECIPES.put(recipe.getId(), recipe);
    }

    @Nullable
    public static MixingRecipe getMixingRecipe(TileMixingVat vat) {
        for (MixingRecipe r : RECIPES.values()) {
            if (r.matches(vat)) {
                return r;
            }
        }
        return null;
    }
}
