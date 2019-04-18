package com.teamacronymcoders.survivalism.modules;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.ModItems;
import com.teamacronymcoders.survivalism.common.blocks.BlockCompostBin;
import com.teamacronymcoders.survivalism.common.items.ItemCompost;
import com.teamacronymcoders.survivalism.common.tiles.TileCompostBin;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Module(value = Survivalism.MODID)
@Mod.EventBusSubscriber
public class CompostModule extends ModuleBase {

    @Override
    public String getName() {
        return "Compost";
    }

    @Override
    public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry blockRegistry) {
        blockRegistry.register(new BlockCompostBin());
        GameRegistry.registerTileEntity(TileCompostBin.class, new ResourceLocation(Survivalism.MODID, "_compost_bin"));
    }

    @Override
    public void registerItems(ConfigRegistry configRegistry, ItemRegistry itemRegistry) {
        itemRegistry.register(new ItemCompost());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModBlocks.blockCompostBin.initModel();
        ModelLoader.setCustomModelResourceLocation(ModItems.compost, 0, new ModelResourceLocation("survivalism:compost", "inventory"));
    }
}
