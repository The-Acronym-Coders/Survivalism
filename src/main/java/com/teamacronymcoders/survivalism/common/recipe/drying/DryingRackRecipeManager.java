package com.teamacronymcoders.survivalism.common.recipe.drying;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.teamacronymcoders.survivalism.common.tiles.TileDryingRack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DryingRackRecipeManager {
    private static final Map<ResourceLocation, DryingRecipe> DRYING = new HashMap<>();

    public static List<DryingRecipe> getDryingRecipes() {
        return ImmutableList.copyOf(DRYING.values());
    }

    public static DryingRecipe getDryingRecipe(ResourceLocation id) {
        return DRYING.get(id);
    }

    public static void register(DryingRecipe recipe) {
        Preconditions.checkNotNull(recipe.getId(), "Cannot register Drying Recipe with null name.");
        Preconditions.checkNotNull(recipe.getInput(), "Cannot register Drying Recipe with null ingredient input.");
        Preconditions.checkNotNull(recipe.getOutput(), "Cannot register Drying Recipe with null ItemStack output.");
        Preconditions.checkArgument(!DRYING.containsKey(recipe.getId()), String.format("Cannot use duplicate ID %s for a drying recipe.", recipe.getId()));
        DRYING.put(recipe.getId(), recipe);
    }

    @Nullable
    public static DryingRecipe getDryingRecipe(TileDryingRack dryingRack) {
        for (DryingRecipe r : DRYING.values()) {
            if (r.matches(dryingRack)) {
                return r;
            }
        }
        return null;
    }
}
