package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.common.blocks.BlockCompostBin;
import com.teamacronymcoders.survivalism.common.blocks.BlockDryingRack;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBrewing;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelSoaking;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelStorage;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockMixingVat;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModBlocks {

    @GameRegistry.ObjectHolder("survivalism:barrel_brewing")
    public static BlockBarrelBrewing blockBarrelBrewing;

    @GameRegistry.ObjectHolder("survivalism:barrel_soaking")
    public static BlockBarrelSoaking blockBarrelSoaking;

    @GameRegistry.ObjectHolder("survivalism:barrel_storage")
    public static BlockBarrelStorage blockBarrelStorage;

    @GameRegistry.ObjectHolder("survivalism:crushing_vat")
    public static BlockCrushingVat blockCrushingVat;

    @GameRegistry.ObjectHolder("survivalism:mixing_vat")
    public static BlockMixingVat blockMixingVat;

    @GameRegistry.ObjectHolder("survivalism:drying_rack")
    public static BlockDryingRack blockDryingRack;

    @GameRegistry.ObjectHolder("survivalism:compost_bin")
    public static BlockCompostBin blockCompostBin;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        blockBarrelBrewing.initModels();
        blockBarrelSoaking.initModels();
        blockBarrelStorage.initModels();
        blockCrushingVat.initModel();
        blockMixingVat.initModel();
        blockDryingRack.initModel();
    }

}
