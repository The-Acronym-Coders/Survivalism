package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.BlockDryingRack;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockMixingVat;
import com.teamacronymcoders.survivalism.compat.hwyla.providers.HwylaCompatProviderBarrel;
import com.teamacronymcoders.survivalism.compat.hwyla.providers.HwylaCompatProviderCrushingVat;
import com.teamacronymcoders.survivalism.compat.hwyla.providers.HwylaCompatProviderDryingRack;
import com.teamacronymcoders.survivalism.compat.hwyla.providers.HwylaCompatProviderMixingVat;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class SurvivalismHwylaRegister implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        // Barrels
        registrar.addConfig(Survivalism.NAME, "survivalism.barrel", "Barrel", true);
        registrar.registerBodyProvider(new HwylaCompatProviderBarrel(), BlockBarrelBase.class);
        registrar.registerNBTProvider(new HwylaCompatProviderBarrel(), BlockBarrelBase.class);

        // Crushing Vat
        registrar.addConfig(Survivalism.NAME, "survivalism.crushing_vat", "Crushing Vat", true);
        registrar.registerBodyProvider(new HwylaCompatProviderCrushingVat(), BlockCrushingVat.class);
        registrar.registerNBTProvider(new HwylaCompatProviderCrushingVat(), BlockCrushingVat.class);

        // Mixing Vat
        registrar.addConfig(Survivalism.NAME, "survivalism.mixing", "Mixing Vat", true);
        registrar.registerBodyProvider(new HwylaCompatProviderMixingVat(), BlockMixingVat.class);
        registrar.registerNBTProvider(new HwylaCompatProviderMixingVat(), BlockMixingVat.class);

        // Drying Rack
        registrar.addConfig(Survivalism.NAME, "survivalism.drying_rack", "Drying Rack", true);
        registrar.registerBodyProvider(new HwylaCompatProviderDryingRack(), BlockDryingRack.class);
        registrar.registerNBTProvider(new HwylaCompatProviderDryingRack(), BlockDryingRack.class);
    }
}
