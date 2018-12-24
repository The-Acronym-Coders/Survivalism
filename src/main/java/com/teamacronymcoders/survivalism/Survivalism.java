package com.teamacronymcoders.survivalism;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Survivalism.MODID, name = Survivalism.NAME, version = Survivalism.VERSION, acceptedMinecraftVersions = Survivalism.MCVERSION, dependencies = Survivalism.DEPS)
public class Survivalism extends BaseModFoundation<Survivalism> {
    public static final String MODID = "survivalism";
    public static final SurvivalismTab TAB = new SurvivalismTab();
    static final String NAME = "Survivalism";
    static final String VERSION = "1.12.2-1.0.0";
    static final String MCVERSION = "1.12,";
    static final String DEPS = "" +
            "required-after:patchouli@[1.0-13,);";
    private static final String COMMON = "com.teamacronymcoders.survivalism.common.CommonProxy";
    private static final String CLIENT = "com.teamacronymcoders.survivalism.client.ClientProxy";
    public static Logger logger;
    @Mod.Instance
    public static Survivalism INSTANCE;
    @SidedProxy(serverSide = COMMON, clientSide = CLIENT)
    private static CommonProxy proxy;

    public Survivalism() {
        super(MODID, NAME, VERSION, TAB);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        logger = event.getModLog();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public Survivalism getInstance() {
        return this;
    }
}
