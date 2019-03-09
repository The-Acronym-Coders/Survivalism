package com.teamacronymcoders.survivalism.client.gui.helper;

import com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation.TFPHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class GUIHelper {
    public static void addPotionTooltip(List<String> strings, FluidStack fluid, int capacity) {
        if (fluid != null) {
            if (TFPHelper.isPotion(fluid) || TFPHelper.isSplashPotion(fluid) || TFPHelper.isLingeringPotion(fluid)) {
                strings.add(fluid.getLocalizedName() + ": " + fluid.amount + " / " + capacity + "mB");
                PotionType type = null;
                if (fluid.tag != null && !fluid.tag.isEmpty() && fluid.tag.hasKey("Potion")) {
                    type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(fluid.tag.getString("Potion")));
                }
                if (type != null) {
                    if (TFPHelper.isPotion(fluid)) {
                        strings.add("Potion Name: " + type.getNamePrefixed(""));
                    } else if (TFPHelper.isSplashPotion(fluid)) {
                        strings.add("Potion Name: " + type.getNamePrefixed("Splash "));
                    } else if (TFPHelper.isLingeringPotion(fluid)) {
                        strings.add("Potion Name: " + type.getNamePrefixed("Lingering "));
                    }
                } else {
                    strings.add("Potion Name: Null");
                }
            }
        }
    }
}
