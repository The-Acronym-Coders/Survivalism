package com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;

public class PFHolder {
    PotionType from;
    Ingredient with;
    PotionType to;

    public PFHolder(PotionType from, Ingredient with, PotionType to) {
        this.from = from;
        this.with = with;
        this.to = to;
    }

    public PotionType getFrom() {
        return from;
    }

    public Ingredient getWith() {
        return with;
    }

    public PotionType getTo() {
        return to;
    }
}
