package com.teamacronymcoders.survivalism.common.recipe;

import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class CompostMaterialList {
    private static List<Ingredient> compost_materials = new ArrayList<>();

    public static List<Ingredient> getCompostMaterials() {
        return compost_materials;
    }
}
