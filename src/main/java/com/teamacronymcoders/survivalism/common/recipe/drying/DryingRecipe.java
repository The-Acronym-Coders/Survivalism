package com.teamacronymcoders.survivalism.common.recipe.drying;

import com.teamacronymcoders.survivalism.common.tiles.TileDryingRack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class DryingRecipe {
    protected final ResourceLocation id;
    protected final Ingredient input;
    protected final ItemStack output;
    protected final int ticks;

    public DryingRecipe(ResourceLocation id, Ingredient input, ItemStack output, int ticks) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.ticks = ticks;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean matches(TileDryingRack dryingRack) {
        return input.apply(dryingRack.getStack());
    }

}
