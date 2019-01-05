package com.teamacronymcoders.survivalism;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import com.teamacronymcoders.base.util.files.BaseFileUtils;
import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;


import java.util.LinkedList;
import java.util.List;

@Mod(modid = Survivalism.MODID, name = Survivalism.NAME, version = Survivalism.VERSION, acceptedMinecraftVersions = Survivalism.MCVERSION, dependencies = Survivalism.DEPS)
public class Survivalism extends BaseModFoundation<Survivalism> {
    public static final String MODID = "survivalism";
    public static final SurvivalismTab TAB = new SurvivalismTab();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    static final String NAME = "Survivalism";
    static final String VERSION = "1.12.2-1.0.0";
    static final String MCVERSION = "1.12,";
    static final String DEPS = "" +
            "required-after:patchouli@[1.0-13,);";
    private static final String COMMON = "com.teamacronymcoders.survivalism.common.CommonProxy";
    private static final String CLIENT = "com.teamacronymcoders.survivalism.client.ClientProxy";
    @Mod.Instance(Survivalism.MODID)
    public static Survivalism INSTANCE;
    @SidedProxy(serverSide = COMMON, clientSide = CLIENT)
    private static CommonProxy proxy;
    public Logger logger;
//    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    public Survivalism() {
        super(MODID, NAME, VERSION, TAB);
    }

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        proxy.preInit(event);
        logger = event.getModLog();
    }

    @Override
    public void beforeModuleHandlerInit(FMLPreInitializationEvent event) {
        SurvivalismConfigs.preInitLoad(event);
    }

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
