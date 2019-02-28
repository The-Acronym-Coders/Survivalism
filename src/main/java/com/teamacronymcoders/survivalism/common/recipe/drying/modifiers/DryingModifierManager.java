package com.teamacronymcoders.survivalism.common.recipe.drying.modifiers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DryingModifierManager {
    private static final Map<IBlockState, Double> STATE_MODIFIERS = new HashMap<>();
    private static final Map<Biome, Double> BIOME_MODIFIERS = new HashMap<>();

    public static List<Map.Entry<IBlockState, Double>> getStateModifiers() {
        return ImmutableList.copyOf(STATE_MODIFIERS.entrySet());
    }

    public static List<Map.Entry<Biome, Double>> getBiomeModifiers() {
        return ImmutableList.copyOf(BIOME_MODIFIERS.entrySet());
    }

    public static boolean containsStateValue(IBlockState state) {
        return STATE_MODIFIERS.containsKey(state);
    }

    public static boolean containsBiomeValue(Biome biome) {
        return BIOME_MODIFIERS.containsKey(biome);
    }

    public static void register(IBlockState state, double modifier) {
        Preconditions.checkNotNull(state, "Cannot register Block Modifier with null Block Item.");
        STATE_MODIFIERS.put(state, modifier);
    }

    public static void register(Biome biome, double modifier) {
        Preconditions.checkNotNull(biome, "Cannot register Biome Modifier with null Block Item.");
        BIOME_MODIFIERS.put(biome, modifier);
    }

    public static Double getModifier(IBlockState state) {
        return STATE_MODIFIERS.getOrDefault(state, 1.0d);
    }

    public static Double getModifier(Biome biome) {
        return BIOME_MODIFIERS.getOrDefault(biome, 1.0d);
    }
}
