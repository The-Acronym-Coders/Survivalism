package com.teamacronymcoders.survivalism;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.command.CommandSubBase;
import com.teamacronymcoders.survivalism.common.CommonProxy;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import com.teamacronymcoders.survivalism.compat.gamestages.CrushingHandler;
import com.teamacronymcoders.survivalism.compat.gamestages.MixingHandler;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import com.teamacronymcoders.survivalism.utils.commands.*;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.network.SurvivalismPacketHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.command.CommandTreeBase;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

@Mod(modid = Survivalism.MODID, name = Survivalism.NAME, version = Survivalism.VERSION, acceptedMinecraftVersions = Survivalism.MCVERSION, dependencies = Survivalism.DEPS)
public class Survivalism extends BaseModFoundation<Survivalism> {

    public static final String MODID = "survivalism";
    public static final SurvivalismTab TAB = new SurvivalismTab();
    public static final List<IAction> LATE_REMOVALS = new LinkedList<>();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    public static final String NAME = "Survivalism";
    public static final String VERSION = "1.12.2-1.0.0";
    public static final String MCVERSION = "1.12,";
    public static final String DEPS = "required-after:base;" + "after:patchouli;";
    private static final String COMMON = "com.teamacronymcoders.survivalism.common.CommonProxy";
    private static final String CLIENT = "com.teamacronymcoders.survivalism.client.ClientProxy";
    private final static CommandSubBase dumps = new CommandSubBase("dumps");
    @Mod.Instance(Survivalism.MODID)
    public static Survivalism INSTANCE;
    @SidedProxy(serverSide = COMMON, clientSide = CLIENT)
    public static CommonProxy proxy;
    public static Logger logger;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    private CommandTreeBase baseCommand = new CommandSubBase(MODID);

    public Survivalism() {
        super(MODID, NAME, VERSION, TAB);
    }

    private static void setup() {
        Survivalism.INSTANCE.getBaseCommand().addSubcommand(dumps);
        dumps.addSubcommand(new CommandBrewing());
        dumps.addSubcommand(new CommandCrushing());
        dumps.addSubcommand(new CommandSoaking());
        dumps.addSubcommand(new CommandDrying());
        dumps.addSubcommand(new CommandMixing());
    }

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        proxy.preInit(event);
        logger = event.getModLog();
        SurvivalismPacketHandler.registerMessages();
        if (Loader.isModLoaded("gamestages")) {
            MinecraftForge.EVENT_BUS.register(new CrushingHandler());
            MinecraftForge.EVENT_BUS.register(new MixingHandler());
        }
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
        if (Loader.isModLoaded("crafttweaker")) {
            LATE_REMOVALS.forEach(CraftTweakerAPI::apply);
            LATE_ADDITIONS.forEach(CraftTweakerAPI::apply);
        }
    }

    @Override
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        setup();
        event.registerServerCommand(this.getBaseCommand());
    }

    @Override
    public Survivalism getInstance() {
        return this;
    }

    private CommandTreeBase getBaseCommand() {
        return baseCommand;
    }
}
