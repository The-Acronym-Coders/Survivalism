package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;
import com.teamacronymcoders.base.modulesystem.dependencies.ModDependency;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.compat.crafttweaker.commands.CommandBrewingDump;
import com.teamacronymcoders.survivalism.compat.crafttweaker.commands.CommandCrushingDump;
import com.teamacronymcoders.survivalism.compat.crafttweaker.commands.CommandSoakingDump;
import crafttweaker.mc1120.commands.CTChatCommand;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.List;

@Module(value = Survivalism.MODID)
public class CrafttweakerModule extends ModuleBase {
    @Override
    public String getName() {
        return "Crafttweaker";
    }

    @Override
    public List<IDependency> getDependencies(List<IDependency> dependencies) {
        dependencies.add(new ModDependency("crafttweaker"));
        return super.getDependencies(dependencies);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        CTChatCommand.registerCommand(new CommandBrewingDump());
        CTChatCommand.registerCommand(new CommandSoakingDump());
        CTChatCommand.registerCommand(new CommandCrushingDump());
    }
}
