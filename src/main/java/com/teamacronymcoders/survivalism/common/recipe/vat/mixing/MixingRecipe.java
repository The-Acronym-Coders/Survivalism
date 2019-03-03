package com.teamacronymcoders.survivalism.common.recipe.vat.mixing;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class MixingRecipe {
    protected final ResourceLocation id;
    protected final FluidStack main;
    protected final FluidStack secondary;
    protected final Ingredient catalyst;
    protected final FluidStack output;
    protected final int clicks;

    //TODO: Implement Click in Recipe instead of Set Number!
    public MixingRecipe(ResourceLocation id, FluidStack main, FluidStack secondary, Ingredient catalyst, FluidStack output, int clicks) {
        this.id = id;
        this.main = main;
        this.secondary = secondary;
        this.catalyst = catalyst;
        this.output = output;
        this.clicks = clicks;
    }

    public MixingRecipe(ResourceLocation id, FluidStack main, FluidStack secondary, FluidStack output, int clicks) {
        this(id, main, secondary, Ingredient.EMPTY, output, clicks);
    }

    public MixingRecipe(ResourceLocation id, FluidStack main, Ingredient catalyst, FluidStack output, int clicks) {
        this(id, main, null, catalyst, output, clicks);
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

    public int getClicks() {
        return clicks;
    }

    public boolean matches(TileMixingVat vat) {


        return true;
    }
}
