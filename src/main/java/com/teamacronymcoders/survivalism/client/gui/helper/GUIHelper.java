package com.teamacronymcoders.survivalism.client.gui.helper;

import com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class GUIHelper {
    public static void addPotionTooltip(List<String> strings, FluidStack fluid, int capacity) {
        if (fluid != null) {
            if (PotionHelper.isPotionFluid(fluid)) {
                PotionType type = null;
                if (fluid.tag != null && !fluid.tag.isEmpty() && fluid.tag.hasKey("Potion")) {
                    type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(fluid.tag.getString("Potion")));
                }
                String prefix = "";
                if (type != null) {
                    if (fluid.tag.getString("Potion").contains("strong")) {
                        prefix = "Empowered";
                    } else if (fluid.tag.getString("Potion").contains("long")) {
                        prefix = "Lasting";
                    }
                }
                strings.add("Fluid: " + prefix + " " + fluid.getLocalizedName() + ":");
            } else {
                strings.add("Fluid: " + fluid.getLocalizedName());
            }
            strings.add("Amount: " + fluid.amount + "/" + capacity + "mB");
        }
    }
}
