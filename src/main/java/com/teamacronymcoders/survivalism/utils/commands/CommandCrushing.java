package com.teamacronymcoders.survivalism.utils.commands;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipeManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class CommandCrushing extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "crushing_dump";
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return "Dumps Crushing Recipe ID's to Log";
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Logger logger = Survivalism.logger;
        logger.info("Crushing Recipe ID's: ");
        logger.info("#####################");
        List<CrushingRecipe> recipes = CrushingRecipeManager.getRecipes();
        for (CrushingRecipe recipe : recipes) {
            logger.info(recipe.getID().toString());
        }
        logger.info("#####################");
        sender.sendMessage(new TextComponentString("Crushing ID's has been dumped to Log"));
    }
}
