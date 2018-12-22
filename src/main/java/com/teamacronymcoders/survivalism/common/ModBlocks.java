package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    @GameRegistry.ObjectHolder("survivalism:barrel")
    public static BlockBarrel blockBarrel;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockBarrel.initModel();
    }

}
