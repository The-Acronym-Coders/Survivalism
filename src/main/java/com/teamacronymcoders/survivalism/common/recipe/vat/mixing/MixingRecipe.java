package com.teamacronymcoders.survivalism.common.recipe.vat.mixing;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class MixingRecipe {
    protected final ResourceLocation id;
    protected final FluidStack main;
    protected final FluidStack secondary;
    protected final Ingredient catalyst;
    protected final FluidStack output;

    public MixingRecipe(ResourceLocation id, FluidStack main, FluidStack secondary, Ingredient catalyst, FluidStack output) {
        this.id = id;
        this.main = main;
        this.secondary = secondary;
        this.catalyst = catalyst;
        this.output = output;
    }

    public MixingRecipe(ResourceLocation id, FluidStack main, FluidStack secondary, FluidStack output) {
        this(id, main, secondary, Ingredient.EMPTY, output);
    }

    public MixingRecipe(ResourceLocation id, FluidStack main, Ingredient catalyst, FluidStack output) {
        this(id, main, null, catalyst, output);
    }

    public ResourceLocation getId() {
        return id;
    }

    public FluidStack getMain() {
        return main;
    }

    public FluidStack getSecondary() {
        return secondary;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public FluidStack getOutput() {
        return output;
    }

    public boolean matches(TileMixingVat vat) {


        return true;
    }
}
