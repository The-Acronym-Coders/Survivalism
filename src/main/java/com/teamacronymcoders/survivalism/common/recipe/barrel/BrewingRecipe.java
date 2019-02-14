package com.teamacronymcoders.survivalism.common.recipe.barrel;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public class BrewingRecipe {

    protected final ResourceLocation id;
    protected final FluidStack input;
    protected final Map<Ingredient, Integer> inputItems;
    protected final FluidStack output;
    protected final int ticks;

    public BrewingRecipe(ResourceLocation id, FluidStack input, Map<Ingredient, Integer> inputItems, FluidStack output, int ticks) {
        this.id = id;
        this.input = input;
        this.inputItems = inputItems;
        this.output = output;
        this.ticks = ticks;
    }

    public ResourceLocation getID() {
        return id;
    }

    public FluidStack getInput() {
        return input;
    }

    public Map<Ingredient, Integer> getInputItems() {
        return inputItems;
    }

    public FluidStack getOutput() {
        return output;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean matches(TileBarrelBrewing barrel) {
        boolean fluid = barrel.getInput().getFluid() != null && barrel.getInput().getFluid().containsFluid(input);
        IntList usedSlots = new IntArrayList(3);
        IntList matchedInputs = new IntArrayList(3);
        int counter = 0;
        for (Ingredient i : inputItems.keySet()) {
            for (int idx = 0; idx < 3; idx++) {
                ItemStack s = barrel.getInv().getStackInSlot(idx);
                if (!matchedInputs.contains(counter) && i.apply(s) && inputItems.get(i) <= s.getCount() && !usedSlots.contains(idx)) {
                    usedSlots.add(idx);
                    matchedInputs.add(counter);
                }
            }
            counter++;
        }

        return fluid && matchedInputs.size() == inputItems.size();
    }

}
