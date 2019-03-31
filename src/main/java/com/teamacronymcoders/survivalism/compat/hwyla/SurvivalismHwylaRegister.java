package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.BlockDryingRack;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockMixingVat;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class SurvivalismHwylaRegister implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        // Barrels
        registrar.addConfig(Survivalism.NAME, "survivalism.barrel", "Barrel", true);
        registrar.registerBodyProvider(HwylaCompatProviderBarrel.INSTANCE, BlockBarrelBase.class);
        registrar.registerNBTProvider(HwylaCompatProviderBarrel.INSTANCE, BlockBarrelBase.class);

        // Crushing Vat
        registrar.addConfig(Survivalism.NAME, "survivalism.crushing_vat", "Crushing Vat", true);
        registrar.registerBodyProvider(HwylaCompatProviderCrushingVat.INSTANCE, BlockCrushingVat.class);
        registrar.registerNBTProvider(HwylaCompatProviderCrushingVat.INSTANCE, BlockCrushingVat.class);

        // Mixing Vat
        registrar.addConfig(Survivalism.NAME, "survivalism.mixing", "Mixing Vat", true);
        registrar.registerBodyProvider(HwylaCompatProviderMixingVat.INSTANCE, BlockMixingVat.class);
        registrar.registerNBTProvider(HwylaCompatProviderMixingVat.INSTANCE, BlockMixingVat.class);

        // Drying Rack
        registrar.addConfig(Survivalism.NAME, "survivalism.drying_rack", "Drying Rack", true);
        registrar.registerBodyProvider(HwylaCompatProviderDryingRack.INSTANCE, BlockDryingRack.class);
        registrar.registerNBTProvider(HwylaCompatProviderDryingRack.INSTANCE, BlockDryingRack.class);
    }
}
