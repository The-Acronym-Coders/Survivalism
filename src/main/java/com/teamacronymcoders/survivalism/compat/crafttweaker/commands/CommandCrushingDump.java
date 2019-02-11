package com.teamacronymcoders.survivalism.compat.crafttweaker.commands;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipeManager;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class CommandCrushingDump extends CraftTweakerCommand {

    public CommandCrushingDump() {
        super("brewIDDump");
    }

    @Override
    protected void init() {
        setDescription(getClickableCommandText(TextFormatting.DARK_GREEN + "/ct crushIDDump", "/ct crushIDDump", true),
                getNormalMessage(TextFormatting.DARK_AQUA + "Outputs a list of all registered Crushing Recipe ID's to the crafttweaker.log"));
    }

    @Override
    public void executeCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) {
        CraftTweakerAPI.logCommand("Crushing Recipe ID's: ");
        CraftTweakerAPI.logCommand("#####################");
        List<VatRecipe> recipes = VatRecipeManager.getRecipes();
        for (VatRecipe recipe : recipes) {
            CraftTweakerAPI.logCommand(recipe.getID().toString());
        }
        CraftTweakerAPI.logCommand("#####################");
        iCommandSender.sendMessage(getNormalMessage("List of Crushing Vat Recipe ID's Generated:"));
        iCommandSender.sendMessage(getLinkToCraftTweakerLog("List Size: " + recipes.size() + " Entries!", iCommandSender));
    }
}
