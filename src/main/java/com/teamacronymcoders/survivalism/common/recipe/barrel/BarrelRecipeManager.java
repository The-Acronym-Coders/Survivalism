package com.teamacronymcoders.survivalism.common.recipe.barrel;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarrelRecipeManager {

    private static final Map<ResourceLocation, BrewingRecipe> BREWING = new HashMap<>();
    private static final Map<ResourceLocation, SoakingRecipe> SOAKING = new HashMap<>();

    public static List<BrewingRecipe> getBrewingRecipes() {
        return ImmutableList.copyOf(BREWING.values());
    }

    public static List<SoakingRecipe> getSoakingRecipes() {
        return ImmutableList.copyOf(SOAKING.values());
    }

    public static BrewingRecipe getBrewingRecipe(ResourceLocation id) {
        return BREWING.get(id);
    }

    public static SoakingRecipe getSoakingRecipe(ResourceLocation id) {
        return SOAKING.get(id);
    }

    public static void register(BrewingRecipe recipe) {
        Preconditions.checkNotNull(recipe.getID(), "Cannot register Brewing Recipe with null name.");
        Preconditions.checkNotNull(recipe.getInputItems(), "Cannot register Brewing Recipe with null item input.");
        Preconditions.checkNotNull(recipe.getInput(), "Cannot register Brewing Recipe with null input.");
        Preconditions.checkNotNull(recipe.getOutput(), "Cannot register Brewing Recipe with null output.");
        Preconditions.checkArgument(!BREWING.containsKey(recipe.getID()), String.format("Cannot use duplicate ID %s for a brewing recipe.", recipe.getID()));
        BREWING.put(recipe.getID(), recipe);
    }

    public static void register(SoakingRecipe recipe) {
        Preconditions.checkNotNull(recipe.getID(), "Cannot register Soaking Recipe with null name.");
        Preconditions.checkNotNull(recipe.getInputItem(), "Cannot register Soaking Recipe with null item input.");
        Preconditions.checkNotNull(recipe.getInput(), "Cannot register Soaking Recipe with null input.");
        Preconditions.checkNotNull(recipe.getOutput(), "Cannot register Soaking Recipe with null output.");
        Preconditions.checkArgument(!SOAKING.containsKey(recipe.getID()), String.format("Cannot use duplicate ID %s for a soaking recipe.", recipe.getID()));
        SOAKING.put(recipe.getID(), recipe);
    }

    @Nullable
    public static BrewingRecipe getBrewingRecipe(TileBarrel barrel) {
        for (BrewingRecipe r : BREWING.values()) {
            if (r.matches(barrel)) {
                return r;
            }
        }
        return null;
    }

    @Nullable
    public static SoakingRecipe getSoakingRecipe(TileBarrel barrel) {
        for (SoakingRecipe r : SOAKING.values()) {
            if (r.matches(barrel)) {
                return r;
            }
        }
        return null;
    }
}
