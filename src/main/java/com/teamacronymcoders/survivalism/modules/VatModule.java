package com.teamacronymcoders.survivalism.modules;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockMixingVat;
import com.teamacronymcoders.survivalism.common.items.ItemMixingSpoon;

@Module(value = Survivalism.MODID)
public class VatModule extends ModuleBase {

    @Override
    public String getName() {
        return "Vats";
    }

    @Override
    public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry registry) {
        registry.register(new BlockCrushingVat());
        registry.register(new BlockMixingVat());
    }

    @Override
    public void registerItems(ConfigRegistry configRegistry, ItemRegistry registry) {
        registry.register(new ItemMixingSpoon());
    }
}
