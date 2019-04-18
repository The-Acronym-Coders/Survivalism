package com.teamacronymcoders.survivalism.compat.gamestages;

import com.teamacronymcoders.survivalism.utils.event.crushing.CrushingEvent;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class CrushingHandler {
    private static List<String> stages = new ArrayList<>();
    private static Map<ResourceLocation, List<String>> recipeLocks = new HashMap<>();

    public static void addGeneralRequirements(String... requirements) {
        stages.addAll(Arrays.asList(requirements));
    }

    public static void addRecipeRequirements(ResourceLocation id, String... requirements) {
        List<String> strings = new ArrayList<>(Arrays.asList(requirements));
        recipeLocks.put(id, strings);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCrushingPre(CrushingEvent.Pre event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!GameStageHelper.hasAllOf(player, stages)) {
                player.sendStatusMessage(new TextComponentTranslation("error.crushing"), true);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCrushingPost(CrushingEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            List<String> refs = new ArrayList<>();
            for (ResourceLocation id : recipeLocks.keySet()) {
                if (id.equals(event.getId())) {
                    refs.addAll(recipeLocks.get(id));
                }
            }
            if (!GameStageHelper.hasAllOf(player, refs)) {
                player.sendStatusMessage(new TextComponentTranslation("error.crushing"), true);
                event.setCanceled(true);
            }
        }
    }

}
