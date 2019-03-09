package com.teamacronymcoders.survivalism.modules.recipes;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.BlockDryingRack;

@Module(value = Survivalism.MODID)
public class DryingRackModule extends ModuleBase {

    @Override
    public String getName() {
        return "Drying Rack";
    }

    @Override
    public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry registry) {
        registry.register(new BlockDryingRack());
    }
}
