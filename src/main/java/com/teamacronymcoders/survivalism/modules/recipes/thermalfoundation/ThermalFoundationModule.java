package com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;
import com.teamacronymcoders.base.modulesystem.dependencies.ModDependency;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.List;

@Module(value = Survivalism.MODID)
public class ThermalFoundationModule extends ModuleBase {
    @Override
    public String getName() {
        return "Thermal Foundation";
    }

    @Override
    public List<IDependency> getDependencies(List<IDependency> dependencies) {
        dependencies.add(new ModDependency("thermalfoundation"));
        return super.getDependencies(dependencies);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        TFPotionBrewing.registerTFSupport();
    }
}
