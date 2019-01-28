package com.teamacronymcoders.survivalism.client;

import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }
}
