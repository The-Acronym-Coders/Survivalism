package com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class PotionBrewing {
    private static final int baseAmount = 1000;
    private static final ResourceLocation awkward = new ResourceLocation(Survivalism.MODID, "awkward_potion");
    private static final FluidStack awkward_fluidstack = new FluidStack(FluidRegistry.getFluid("potion"), 1000);
    private static final List<PFHolder> brewingComponents = new ArrayList<>();

    public static void registerPotionSupport() {
        // Register Potion PFHolders
        registerAwkwardType();
        registerBaseTypes();
        registerNegativeTypes();
        registerStrongTypes();
        registerLongTypes();

        // Generates Brewing Recipes for the PFHolders
        registerPotionRecipes();
        if (Loader.isModLoaded("thermalfoundation")) {
            registerSplashAndLingeringRecipes();
        }
    }

    private static void registerAwkwardType() {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.fromItem(Items.NETHER_WART));
        PotionHelper.addPotionToFluidStack(awkward_fluidstack, PotionTypes.AWKWARD);
        BarrelRecipeManager.register(new BrewingRecipe(awkward, FluidRegistry.getFluidStack("water", 1000), ingredientList, awkward_fluidstack, 1800));
    }

    private static void registerBaseTypes() {
        // Base Types
        brewingComponents.add(new PFHolder(PotionTypes.WATER, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.WEAKNESS));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.SUGAR), PotionTypes.SWIFTNESS));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.RABBIT_FOOT), PotionTypes.LEAPING));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.BLAZE_POWDER), PotionTypes.STRENGTH));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.SPECKLED_MELON), PotionTypes.HEALING));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.SPIDER_EYE), PotionTypes.POISON));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.MAGMA_CREAM), PotionTypes.FIRE_RESISTANCE));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())), PotionTypes.WATER_BREATHING));
        brewingComponents.add(new PFHolder(PotionTypes.AWKWARD, Ingredient.fromItem(Items.GOLDEN_CARROT), PotionTypes.NIGHT_VISION));
    }

    private static void registerNegativeTypes() {
        // Base Conversions
        brewingComponents.add(new PFHolder(PotionTypes.SWIFTNESS, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.SLOWNESS));
        brewingComponents.add(new PFHolder(PotionTypes.LEAPING, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.SLOWNESS));
        brewingComponents.add(new PFHolder(PotionTypes.HEALING, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.HARMING));
        brewingComponents.add(new PFHolder(PotionTypes.POISON, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.HARMING));
        brewingComponents.add(new PFHolder(PotionTypes.NIGHT_VISION, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.INVISIBILITY));

        // End Conversions
        brewingComponents.add(new PFHolder(PotionTypes.LONG_SWIFTNESS, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.LONG_SLOWNESS));
        brewingComponents.add(new PFHolder(PotionTypes.LONG_LEAPING, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.LONG_SLOWNESS));
        brewingComponents.add(new PFHolder(PotionTypes.STRONG_HEALING, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.STRONG_HARMING));
        brewingComponents.add(new PFHolder(PotionTypes.STRONG_POISON, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.STRONG_HARMING));
        brewingComponents.add(new PFHolder(PotionTypes.LONG_NIGHT_VISION, Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), PotionTypes.LONG_INVISIBILITY));
    }

    private static void registerStrongTypes() {
        brewingComponents.add(new PFHolder(PotionTypes.SWIFTNESS, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_SWIFTNESS));
        brewingComponents.add(new PFHolder(PotionTypes.LEAPING, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_LEAPING));
        brewingComponents.add(new PFHolder(PotionTypes.STRENGTH, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_STRENGTH));
        brewingComponents.add(new PFHolder(PotionTypes.HEALING, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_HEALING));
        brewingComponents.add(new PFHolder(PotionTypes.HARMING, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_HARMING));
        brewingComponents.add(new PFHolder(PotionTypes.POISON, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_POISON));
        brewingComponents.add(new PFHolder(PotionTypes.REGENERATION, Ingredient.fromItem(Items.GLOWSTONE_DUST), PotionTypes.STRONG_REGENERATION));
    }

    private static void registerLongTypes() {
        brewingComponents.add(new PFHolder(PotionTypes.SWIFTNESS, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_SWIFTNESS));
        brewingComponents.add(new PFHolder(PotionTypes.SLOWNESS, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_SLOWNESS));
        brewingComponents.add(new PFHolder(PotionTypes.LEAPING, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_LEAPING));
        brewingComponents.add(new PFHolder(PotionTypes.STRENGTH, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_STRENGTH));
        brewingComponents.add(new PFHolder(PotionTypes.POISON, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_POISON));
        brewingComponents.add(new PFHolder(PotionTypes.REGENERATION, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_REGENERATION));
        brewingComponents.add(new PFHolder(PotionTypes.FIRE_RESISTANCE, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_FIRE_RESISTANCE));
        brewingComponents.add(new PFHolder(PotionTypes.WATER_BREATHING, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_WATER_BREATHING));
        brewingComponents.add(new PFHolder(PotionTypes.NIGHT_VISION, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_NIGHT_VISION));
        brewingComponents.add(new PFHolder(PotionTypes.INVISIBILITY, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_INVISIBILITY));
        brewingComponents.add(new PFHolder(PotionTypes.WEAKNESS, Ingredient.fromItem(Items.REDSTONE), PotionTypes.LONG_WEAKNESS));
    }

    private static void registerPotionRecipes() {
        for (PFHolder values : brewingComponents) {
            List<Ingredient> ingredientList = new ArrayList<>();
            ingredientList.add(values.with);
            PotionType typeIn = values.from;
            PotionType typeOut = values.to;
            FluidStack stackIn = PotionHelper.getPotion(baseAmount, typeIn);
            FluidStack stackOut = PotionHelper.getPotion(baseAmount, typeOut);
            ResourceLocation rl = new ResourceLocation(Survivalism.MODID, typeIn.getRegistryName().getPath() + "_" + typeOut.getRegistryName().getPath());
            BarrelRecipeManager.register(new BrewingRecipe(rl, stackIn, ingredientList, stackOut, 1800));
        }
    }

    private static void registerSplashAndLingeringRecipes() {
        List<PotionType> types = new ArrayList<>();
        for (PFHolder values : brewingComponents) {
            List<Ingredient> splashList = new ArrayList<>();
            splashList.add(Ingredient.fromItem(Items.GUNPOWDER));

            List<Ingredient> lingeringList = new ArrayList<>();
            lingeringList.add(Ingredient.fromItem(Items.DRAGON_BREATH));

            PotionType typeIn = values.from;
            PotionType typeOut = values.to;

            if (typeIn == PotionTypes.AWKWARD) {
                continue;
            }

            FluidStack base;
            FluidStack splash;
            FluidStack lingering;

            ResourceLocation rlS = new ResourceLocation(Survivalism.MODID, typeIn.getRegistryName().getPath() + "_" + typeOut.getRegistryName().getPath() + "_splash");
            ResourceLocation rlL = new ResourceLocation(Survivalism.MODID, typeIn.getRegistryName().getPath() + "_" + typeOut.getRegistryName().getPath() + "_lingering");
            ResourceLocation rlSR = new ResourceLocation(Survivalism.MODID, typeIn.getRegistryName().getPath() + "_" + typeOut.getRegistryName().getPath() + "_splash_reversed");
            ResourceLocation rlLR = new ResourceLocation(Survivalism.MODID, typeIn.getRegistryName().getPath() + "_" + typeOut.getRegistryName().getPath() + "_lingering_reversed");


            if (!types.contains(typeIn)) {
                base = PotionHelper.getPotion(baseAmount, typeIn);
                splash = PotionHelper.getSplashPotion(baseAmount, typeIn);
                lingering = PotionHelper.getLingeringPotion(baseAmount, typeIn);
                BarrelRecipeManager.register(new BrewingRecipe(rlS, base, splashList, splash, 1800));
                BarrelRecipeManager.register(new BrewingRecipe(rlL, splash, lingeringList, lingering, 1800));
                types.add(typeIn);
            }

            if (!types.contains(typeOut)) {
                base = PotionHelper.getPotion(baseAmount, typeOut);
                splash = PotionHelper.getSplashPotion(baseAmount, typeOut);
                lingering = PotionHelper.getLingeringPotion(baseAmount, typeOut);
                BarrelRecipeManager.register(new BrewingRecipe(rlSR, base, splashList, splash, 1800));
                BarrelRecipeManager.register(new BrewingRecipe(rlLR, splash, lingeringList, lingering, 1800));
                types.add(typeOut);
            }
        }
    }
}
