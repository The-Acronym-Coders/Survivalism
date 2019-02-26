package com.teamacronymcoders.survivalism.utils.commands;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class CommandBrewing extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "brewing_dump";
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return "Dumps Brewing Recipe ID's to Log";
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Logger logger = Survivalism.logger;
        logger.info("Brewing Recipe ID's: ");
        logger.info("#####################");
        List<BrewingRecipe> recipes = BarrelRecipeManager.getBrewingRecipes();
        for (BrewingRecipe recipe : recipes) {
            logger.info(recipe.getID().toString());
        }
        logger.info("#####################");
    }
}
