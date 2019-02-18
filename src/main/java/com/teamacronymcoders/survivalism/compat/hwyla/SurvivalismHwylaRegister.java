package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class SurvivalismHwylaRegister implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.addConfig(Survivalism.MODID, "survivalism.barrel", "Barrel", true);
        registrar.addConfig(Survivalism.MODID, "survivalism.crushing_vat", "Crushing Vat", true);
        registrar.registerBodyProvider(new HwylaCompatProviderBarrel(), BlockBarrelBase.class);
        registrar.registerBodyProvider(new HwylaCompatProviderCrushingVat(), BlockCrushingVat.class);
    }
}
