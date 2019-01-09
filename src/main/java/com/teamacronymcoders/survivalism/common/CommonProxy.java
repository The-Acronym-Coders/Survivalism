package com.teamacronymcoders.survivalism.common;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.GUIProxy;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new BlockBarrel(),
                new BlockCrushingVat()
        );

        GameRegistry.registerTileEntity(TileBarrel.class, new ResourceLocation(Survivalism.MODID, "_barrel"));
        GameRegistry.registerTileEntity(TileCrushingVat.class, new ResourceLocation(Survivalism.MODID, "_crushingvat"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemBlock(ModBlocks.blockBarrel).setRegistryName(ModBlocks.blockBarrel.getRegistryName()),
                new ItemBlock(ModBlocks.blockCrushingVat).setRegistryName(ModBlocks.blockCrushingVat.getRegistryName())
        );
    }

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Survivalism.INSTANCE, new GUIProxy());
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

}
