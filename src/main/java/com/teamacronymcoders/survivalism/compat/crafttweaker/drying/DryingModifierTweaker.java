package com.teamacronymcoders.survivalism.compat.crafttweaker.drying;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.drying.modifiers.DryingModifierManager;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBiome;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

// TODO: Finish Implementing
// TODO: Wait for Kindlich to Add IBiome -> Biome stuff and IBlockState.toCommandString Method

@ZenClass("mods.survivalism.dryingModifiers")
@ZenRegister
public class DryingModifierTweaker {

    @ZenMethod
    public static void addBlockModifier(IBlockState state, double modifier) {
        Survivalism.LATE_ADDITIONS.add(new AddStateModifier(state, modifier));
    }

    @ZenMethod
    public static void addBiomeModifier(IBiome biome, double modifier) {
        Survivalism.LATE_ADDITIONS.add(new AddBiomeModifier(biome, modifier));
    }

    private static class AddStateModifier implements IAction {
        net.minecraft.block.state.IBlockState state;
        double modifier;

        AddStateModifier(IBlockState state, double modifier) {
            this.state = CraftTweakerMC.getBlockState(state);
            this.modifier = modifier;
        }

        @Override
        public void apply() {
            if (!DryingModifierManager.containsStateValue(state)) {
                DryingModifierManager.register(state, modifier);
            } else {
                CraftTweakerAPI.logError("State Modifier is already registered!");
            }
        }

        @Override
        public String describe() {
            return "Registered Modifier Value: " + state.getBlock().getLocalizedName() + " -> " + modifier;
        }
    }

    private static class AddBiomeModifier implements IAction {
        Biome biome;
        Double modifier;

        AddBiomeModifier(IBiome biome, double modifier) {
            this.biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biome.getName()));
            this.modifier = modifier;
        }

        @Override
        public void apply() {
            if (!DryingModifierManager.containsBiomeValue(biome)) {
                DryingModifierManager.register(biome, modifier);
            } else {
                CraftTweakerAPI.logError("Biome Modifier is already registered!");
            }
        }

        @Override
        public String describe() {
            return "Registered Modifier Value: " + biome.getBiomeName() + " -> " + modifier;
        }
    }

}
