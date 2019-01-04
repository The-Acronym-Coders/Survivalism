package com.teamacronymcoders.survivalism.client;

import com.teamacronymcoders.survivalism.client.model.SurvivalismModelLoader;
import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    private ICustomModelLoader loader = new SurvivalismModelLoader();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModelLoaderRegistry.registerLoader(loader);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
    }

}
