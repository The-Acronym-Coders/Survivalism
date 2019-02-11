package com.teamacronymcoders.survivalism.compat.crafttweaker.commands;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class CommandSoakingDump extends CraftTweakerCommand {

    public CommandSoakingDump() {
        super("soakIDDump");
    }

    @Override
    protected void init() {
        setDescription(getClickableCommandText(TextFormatting.DARK_GREEN + "/ct soakIDDump", "/ct soakIDDump", true),
                getNormalMessage(TextFormatting.DARK_AQUA + "Outputs a list of all registered Soaking Recipe ID's to the crafttweaker.log"));
    }

    @Override
    public void executeCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) {
        CraftTweakerAPI.logCommand("Soaking Recipe ID's: ");
        CraftTweakerAPI.logCommand("#####################");
        List<SoakingRecipe> recipes = BarrelRecipeManager.getSoakingRecipes();
        for (SoakingRecipe recipe : recipes) {
            CraftTweakerAPI.logCommand(recipe.getID().toString());
        }
        CraftTweakerAPI.logCommand("#####################");
        iCommandSender.sendMessage(getNormalMessage("List of Soaking Recipe ID's Generated:"));
        iCommandSender.sendMessage(getLinkToCraftTweakerLog("List Size: " + recipes.size() + " Entries!", iCommandSender));
    }
}
