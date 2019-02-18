package com.teamacronymcoders.survivalism.modules.recipes;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.survivalism.Survivalism;

@Module(value = Survivalism.MODID)
public class DefaultRecipesModule extends ModuleBase {

    @Override
    public String getName() {
        return "Recipes";
    }

    @Override
    public boolean getActiveDefault() {
        return false;
    }
}
