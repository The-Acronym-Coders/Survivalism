package com.teamacronymcoders.survivalism.compat.theoneprobe;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;
import com.teamacronymcoders.base.modulesystem.dependencies.ModDependency;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

@Module(value = Survivalism.MODID)
public class TheOneProbeModule extends ModuleBase {
    private static boolean registered;

    private static void register() {
        if (registered) {
            return;
        }

        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.teamacronymcoders.survivalism.compat.theoneprobe.TOPHandler");
    }

    @Override
    public List<IDependency> getDependencies(List<IDependency> dependencies) {
        dependencies.add(new ModDependency("theoneprobe"));
        return dependencies;
    }

    @Override
    public String getName() {
        return "TheOneProbe";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        register();
    }
}
