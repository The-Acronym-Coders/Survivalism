package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    @GameRegistry.ObjectHolder("survivalism:barrel")
    public static BlockBarrel blockBarrel;

    @GameRegistry.ObjectHolder("survivalism:crushing_vat")
    public static BlockCrushingVat blockCrushingVat;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockBarrel.initModel();
        blockCrushingVat.initModel();

    }

}
