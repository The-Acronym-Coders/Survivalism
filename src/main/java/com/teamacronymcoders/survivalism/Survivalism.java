package com.teamacronymcoders.survivalism;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import com.teamacronymcoders.survivalism.utils.network.SurvivalismPacketHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Mod(modid = Survivalism.MODID, name = Survivalism.NAME, version = Survivalism.VERSION, acceptedMinecraftVersions = Survivalism.MCVERSION, dependencies = Survivalism.DEPS)
public class Survivalism extends BaseModFoundation<Survivalism> {
	
	static { FluidRegistry.enableUniversalBucket(); } 
	
    public static final String MODID = "survivalism";
    public static final SurvivalismTab TAB = new SurvivalismTab();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    public static final String NAME = "Survivalism";
    public static final String VERSION = "1.12.2-1.0.0";
    public static final String MCVERSION = "1.12,";
    public static final String DEPS = "" +
            "required-after:patchouli@[1.0-13,);";
    public static final Random RANDOM = new Random();
    private static final String COMMON = "com.teamacronymcoders.survivalism.common.CommonProxy";
    private static final String CLIENT = "com.teamacronymcoders.survivalism.client.ClientProxy";
    @Mod.Instance(Survivalism.MODID)
    public static Survivalism INSTANCE;
    @SidedProxy(serverSide = COMMON, clientSide = CLIENT)
    public static CommonProxy proxy;
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
        SurvivalismPacketHandler.registerMessages();
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
