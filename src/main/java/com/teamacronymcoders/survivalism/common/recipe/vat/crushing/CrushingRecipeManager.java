package com.teamacronymcoders.survivalism.common.recipe.vat.crushing;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrushingRecipeManager {

    private static final Map<ResourceLocation, CrushingRecipe> RECIPES = new HashMap<>();
    private static final Object2DoubleMap<Ingredient> BOOTS = new Object2DoubleOpenHashMap<>();

    static {
        registerBoots(Items.LEATHER_BOOTS, 1.5);
        registerBoots(Items.GOLDEN_BOOTS, 2.0);
        registerBoots(Items.IRON_BOOTS, 2.5);
        registerBoots(Items.DIAMOND_BOOTS, 3.0);
    }

    public static List<CrushingRecipe> getRecipes() {
        return ImmutableList.copyOf(RECIPES.values());
    }

    public static CrushingRecipe getRecipeByID(ResourceLocation id) {
        return RECIPES.get(id);
    }

    public static void register(CrushingRecipe recipe) {
        Preconditions.checkNotNull(recipe.getID(), "Cannot register Vat Recipe with null name.");
        Preconditions.checkNotNull(recipe.getInput(), "Cannot register Vat Recipe with null input.");
        Preconditions.checkNotNull(recipe.getOutput(), "Cannot register Vat Recipe with null fluid output.");
        Preconditions.checkArgument(!RECIPES.containsKey(recipe.getID()), String.format("Cannot use duplicate ID %s for a vat recipe.", recipe.getID()));
        RECIPES.put(recipe.getID(), recipe);
    }

    public static void registerBoots(Ingredient boots, double mult) {
        BOOTS.put(boots, mult);
    }

    public static void registerBoots(ItemStack boots, double mult) {
        registerBoots(Ingredient.fromStacks(boots), mult);
    }

    public static void registerBoots(Item boots, double mult) {
        registerBoots(Ingredient.fromItem(boots), mult);
    }

    @Nullable
    public static CrushingRecipe getRecipe(EntityLivingBase jumper, ItemStack input) {
        for (CrushingRecipe r : RECIPES.values()) {
            if (r.matches(jumper, input)) {
                return r;
            }
        }
        return null;
    }

    public static Object2DoubleMap<Ingredient> getBOOTS() {
        return BOOTS;
    }

    public static double getBootsMultiplier(EntityLivingBase jumper) {
        ItemStack boots = jumper.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (boots.isEmpty()) {
            return 1;
        }
        for (Object2DoubleMap.Entry<Ingredient> ent : BOOTS.object2DoubleEntrySet()) {
            if (ent.getKey().apply(boots)) {
                return ent.getDoubleValue();
            }
        }
        return 1.0;
    }
}
