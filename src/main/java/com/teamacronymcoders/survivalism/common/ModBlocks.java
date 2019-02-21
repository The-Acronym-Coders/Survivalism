package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBrewing;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelSoaking;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelStorage;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModBlocks {

    @GameRegistry.ObjectHolder("survivalism:barrel_brewing")
    public static BlockBarrelBrewing brewing;

    @GameRegistry.ObjectHolder("survivalism:barrel_soaking")
    public static BlockBarrelSoaking soaking;

    @GameRegistry.ObjectHolder("survivalism:barrel_storage")
    public static BlockBarrelStorage storage;

    @GameRegistry.ObjectHolder("survivalism:crushing_vat")
    public static BlockCrushingVat blockCrushingVat;



    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        brewing.initModels();
        soaking.initModels();
        storage.initModels();
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockCrushingVat), 0, new ModelResourceLocation("survivalism:crushing_vat", "inventory"));
    }
}
