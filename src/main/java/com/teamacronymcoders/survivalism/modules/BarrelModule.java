package com.teamacronymcoders.survivalism.modules.recipes;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBrewing;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelSoaking;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelStorage;

@Module(value = Survivalism.MODID)
public class BarrelModule extends ModuleBase {

    @Override
    public String getName() {
        return "Barrels";
    }

    @Override
    public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry registry) {
        registry.register(new BlockBarrelBrewing());
        registry.register(new BlockBarrelSoaking());
        registry.register(new BlockBarrelStorage());
    }


}
