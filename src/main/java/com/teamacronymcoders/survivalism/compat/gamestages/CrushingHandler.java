package com.teamacronymcoders.survivalism.compat.gamestages;

import com.teamacronymcoders.survivalism.utils.event.CrushingEvent;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class CrushingHandler {
    private static List<String> stages = new ArrayList<>();
    private static Map<ItemStack, List<String>> itemLocks = new HashMap<>();

    public static void addGeneralRequirements(String... requirements) {
        stages.addAll(Arrays.asList(requirements));
    }

    public static void addRequirementToItemStack(ItemStack stack, String... requirements) {
        List<String> strings = new ArrayList<>(Arrays.asList(requirements));
        itemLocks.put(stack, strings);
    }

    @SubscribeEvent
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

    @SubscribeEvent
    public void onCrushingPost(CrushingEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            List<String> refs = new ArrayList<>();
            for (ItemStack stack : itemLocks.keySet()) {
                if (stack.hasTagCompound()) {
                    NBTTagCompound compound = stack.getTagCompound();
                    NBTTagCompound compound1 = event.getInputItem().getTagCompound();
                    if (compound != null && compound.equals(compound1)) {
                        refs.addAll(itemLocks.get(stack));
                    }
                } else if (stack.isItemEqual(event.getInputItem())) {
                    refs.addAll(itemLocks.get(stack));
                }
            }
            if (!GameStageHelper.hasAllOf(player, refs)) {
                player.sendStatusMessage(new TextComponentTranslation("error.crushing"), true);
                event.setCanceled(true);
            }
        }
    }

}
