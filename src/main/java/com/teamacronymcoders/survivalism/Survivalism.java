package com.teamacronymcoders.survivalism;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBrewing;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelSoaking;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelStorage;
import com.teamacronymcoders.survivalism.common.blocks.old.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import com.teamacronymcoders.survivalism.utils.network.SurvivalismPacketHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

@Mod(modid = Survivalism.MODID, name = Survivalism.NAME, version = Survivalism.VERSION, acceptedMinecraftVersions = Survivalism.MCVERSION, dependencies = Survivalism.DEPS)
public class Survivalism extends BaseModFoundation<Survivalism> {

    public static final String MODID = "survivalism";
    public static final SurvivalismTab TAB = new SurvivalismTab();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    public static final String NAME = "Survivalism";
    public static final String VERSION = "1.12.2-1.0.0";
    public static final String MCVERSION = "1.12,";
    public static final String DEPS = "before:patchouli;";
    private static final String COMMON = "com.teamacronymcoders.survivalism.common.CommonProxy";
    private static final String CLIENT = "com.teamacronymcoders.survivalism.client.ClientProxy";
    @Mod.Instance(Survivalism.MODID)
    public static Survivalism INSTANCE;
    @SidedProxy(serverSide = COMMON, clientSide = CLIENT)
    public static CommonProxy proxy;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public Logger logger;

    public Survivalism() {
        super(MODID, NAME, VERSION, TAB);
    }

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        proxy.preInit(event);
        logger = event.getModLog();
        SurvivalismPacketHandler.registerMessages();
    }

    @Override
    public void beforeModuleHandlerInit(FMLPreInitializationEvent event) {
        SurvivalismConfigs.preInitLoad(event);
    }

    @Override
    public void registerBlocks(BlockRegistry registry) {
        registry.register(new BlockBarrelBrewing());
        registry.register(new BlockBarrelSoaking());
        registry.register(new BlockBarrelStorage());
//        registry.register(new BlockCrushingVat());
        
//        GameRegistry.registerTileEntity(TileCrushingVat.class, new ResourceLocation(Survivalism.MODID, "_crushingvat"));
    }

    @Override
    public void registerItems(ItemRegistry registry) {}

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
        proxy.init(event);
    }

    @Override
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        if (Loader.isModLoaded("crafttweaker")) {
            LATE_ADDITIONS.forEach(CraftTweakerAPI::apply);
        }
    }

    @Override
    public Survivalism getInstance() {
        return this;
    }
}
