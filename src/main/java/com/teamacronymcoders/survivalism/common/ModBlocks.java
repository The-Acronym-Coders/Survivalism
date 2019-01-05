package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.teamacronymcoders.survivalism.Survivalism.MODID;

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
