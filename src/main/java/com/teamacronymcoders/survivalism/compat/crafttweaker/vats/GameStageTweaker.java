package com.teamacronymcoders.survivalism.compat.crafttweaker.vats;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.compat.gamestages.CrushingHandler;
import com.teamacronymcoders.survivalism.compat.gamestages.MixingHandler;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("gamestages")
@ZenClass("mods.survivalism.GameStage")
@ZenRegister
public class GameStageTweaker {

    @ZenMethod
    public static void addGeneralCrushingRequirements(String... requirements) {
        CrushingHandler.addGeneralRequirements(requirements);
    }

    @ZenMethod
    public static void addCrushingRecipeRequirements(String id, String[] requirements) {
        Survivalism.LATE_ADDITIONS.add(new AddCrushingRequirementToItem(new ResourceLocation(id), requirements));
    }

    @ZenMethod
    public static void addMixingGeneralRequirements(String... requirements) {
        MixingHandler.addGeneralRequirements(requirements);
    }

    @ZenMethod
    public static void addMixingRecipeRequirements(String id, String[] requirements) {
        Survivalism.LATE_ADDITIONS.add(new AddMixingRequirementToRecipe(new ResourceLocation(id), requirements));
    }

    private static class AddCrushingRequirementToItem implements IAction {
        ResourceLocation id;
        String[] requirements;

        AddCrushingRequirementToItem(ResourceLocation id, String[] requirements) {
            this.id = id;
            this.requirements = requirements;
        }

        @Override
        public void apply() {
            CrushingHandler.addRecipeRequirements(id, requirements);
        }

        @Override
        public String describe() {
            return "Added Requirements to Recipe ID: " + id.toString();
        }
    }

    private static class AddMixingRequirementToRecipe implements IAction {
        ResourceLocation id;
        String[] requirements;

        AddMixingRequirementToRecipe(ResourceLocation id, String[] requirements) {
            this.id = id;
            this.requirements = requirements;
        }

        @Override
        public void apply() {
            MixingHandler.addRecipeRequirements(id, requirements);
        }

        @Override
        public String describe() {
            return "Added Requirements to Recipe ID: " + id.toString();
        }
    }

}
