package com.teamacronymcoders.survivalism;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.CompostMaterialList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class CompostMaterialsAPI {
    private static List<Ingredient> compostMaterial = CompostMaterialList.getCompostMaterials();

    public static void addMaterialToList(Ingredient ingredient) {
        if (compostMaterial.contains(ingredient)) {
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                Survivalism.logger.error("Item: " + stack.getDisplayName() + " is already defined in the Compost Material List!");
            }
        } else {
            compostMaterial.add(ingredient);
        }
    }

    public static void removeMaterialFromList(Ingredient ingredient) {
        if (compostMaterial.contains(ingredient)) {
            compostMaterial.remove(ingredient);
        } else {
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                Survivalism.logger.error("Item: " + stack.getDisplayName() + " is not defined as a valid Compost Material!");
            }
        }
    }

    public static boolean doesListContainMaterial(Ingredient ingredient) {
        return compostMaterial.contains(ingredient);
    }
}
