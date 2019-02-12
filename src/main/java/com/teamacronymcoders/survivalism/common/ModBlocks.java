package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
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

    @GameRegistry.ObjectHolder("survivalism:barrel")
    public static BlockBarrel blockBarrel;

    @GameRegistry.ObjectHolder("survivalism:crushing_vat")
    public static BlockCrushingVat blockCrushingVat;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        NonNullList<ItemStack> items = NonNullList.create();
        blockBarrel.getSubBlocks(CreativeTabs.SEARCH, items);
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            ModelLoader.setCustomModelResourceLocation(item.getItem(), i, new ModelResourceLocation("survivalism:barrel", "inventory"));
        }
        items.clear();
        blockCrushingVat.getSubBlocks(CreativeTabs.SEARCH, items);
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            ModelLoader.setCustomModelResourceLocation(item.getItem(), i, new ModelResourceLocation("survivalism:crushing_vat", "inventory"));
        }
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockBarrel), 6, new ModelResourceLocation("survivalism:barrel", "inventory"));
    }

}
