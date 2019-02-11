package com.teamacronymcoders.survivalism.compat.crafttweaker.commands;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class CommandBrewingDump extends CraftTweakerCommand {
    public CommandBrewingDump() {
        super("brewIDDump");
    }

    @Override
    protected void init() {
        setDescription(getClickableCommandText(TextFormatting.DARK_GREEN + "/ct brewIDDump", "/ct brewIDDump", true),
                getNormalMessage(TextFormatting.DARK_AQUA + "Outputs a list of all registered Brewing Recipe ID's to the crafttweaker.log"));
    }

    @Override
    public void executeCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) {
        CraftTweakerAPI.logCommand("Brewing Recipe ID's: ");
        CraftTweakerAPI.logCommand("#####################");
        List<BrewingRecipe> recipes = BarrelRecipeManager.getBrewingRecipes();
        for (BrewingRecipe recipe : recipes) {
            CraftTweakerAPI.logCommand(recipe.getID().toString());
        }
        CraftTweakerAPI.logCommand("#####################");
        iCommandSender.sendMessage(getNormalMessage("List of Brewing Recipe ID's Generated:"));
        iCommandSender.sendMessage(getLinkToCraftTweakerLog("List Size: " + recipes.size() + " Entries!", iCommandSender));
    }
}
