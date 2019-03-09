package com.teamacronymcoders.survivalism.compat.crafttweaker.drying;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.drying.modifiers.DryingModifierManager;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBiome;
import crafttweaker.mc1120.item.VanillaIngredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.survivalism.DryingModifiers")
@ZenRegister
public class DryingModifierTweaker {

    @ZenMethod
    public static void addStateModifier(IBlockState state, double modifier) {
        Survivalism.LATE_ADDITIONS.add(new AddStateModifier(state, modifier));
    }

    @ZenMethod
    public static void addBiomeModifier(IBiome biome, double modifier) {
        Survivalism.LATE_ADDITIONS.add(new AddBiomeModifier(biome, modifier));
    }

    private static class AddStateModifier implements IAction {
        net.minecraft.block.state.IBlockState state;
        String stateID;
        double modifier;

        AddStateModifier(IBlockState state, double modifier) {
            this.state = CraftTweakerMC.getBlockState(state);
            this.stateID = state.toCommandString();
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
            return "Registered Modifier Value: " + stateID+ " -> " + modifier;
        }
    }

    private static class AddBiomeModifier implements IAction {
        Biome biome;
        Double modifier;

        AddBiomeModifier(IBiome biome, double modifier) {
            this.biome = CraftTweakerMC.getBiome(biome);
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
