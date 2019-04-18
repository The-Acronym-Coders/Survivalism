package com.teamacronymcoders.survivalism.utils.commands;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRackRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRecipe;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class CommandDrying extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "drying_dump";
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return "Dumps Drying Recipe ID's to Log";
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Logger logger = Survivalism.logger;
        logger.info("Drying Recipe ID's: ");
        logger.info("#####################");
        List<DryingRecipe> recipes = DryingRackRecipeManager.getDryingRecipes();
        for (DryingRecipe recipe : recipes) {
            logger.info(recipe.getId().toString());
        }
        logger.info("#####################");
        sender.sendMessage(new TextComponentString("Drying ID's has been dumped to Log"));
    }
}
