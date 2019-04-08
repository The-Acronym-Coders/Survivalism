package com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation;

import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

public class TFPHelper {

    public static boolean isPotionFluid(@Nonnull FluidStack stack) {
        return isPotion(stack) || isSplashPotion(stack) || isLingeringPotion(stack);
    }

    public static boolean isPotion(@Nonnull FluidStack stack) {
        return stack.getFluid().getName().equals("potion");
    }

    public static boolean isSplashPotion(@Nonnull FluidStack stack) {
        return stack.getFluid().getName().equals("potion_splash");
    }

    public static boolean isLingeringPotion(@Nonnull FluidStack stack) {
        return stack.getFluid().getName().equals("potion_lingering");
    }

    public static FluidStack getPotion(int amount, PotionType type) {
        if (type == null || type == PotionTypes.EMPTY) {
            return null;
        }
        if (type == PotionTypes.WATER) {
            return new FluidStack(FluidRegistry.WATER, amount);
        }
        return addPotionToFluidStack(FluidRegistry.getFluidStack("potion", amount), type);
    }

    public static FluidStack getSplashPotion(int amount, PotionType type) {
        if (type == null || type == PotionTypes.EMPTY) {
            return null;
        }

        return addPotionToFluidStack(FluidRegistry.getFluidStack("potion_splash", amount), type);
    }

    public static FluidStack getLingeringPotion(int amount, PotionType type) {
        if (type == null || type == PotionTypes.EMPTY) {
            return null;
        }
        return addPotionToFluidStack(FluidRegistry.getFluidStack("potion_lingering", amount), type);
    }

    private static FluidStack addPotionToFluidStack(FluidStack stack, PotionType type) {
        ResourceLocation resourcelocation = ForgeRegistries.POTION_TYPES.getKey(type);
        if (resourcelocation == null) {
            return null;
        }
        if (type == PotionTypes.EMPTY) {
            if (stack.tag != null) {
                stack.tag.removeTag("Potion");
                if (stack.tag.isEmpty()) {
                    stack.tag = null;
                }
            }
        } else {
            if (stack.tag == null) {
                stack.tag = new NBTTagCompound();
            }
            stack.tag.setString("Potion", resourcelocation.toString());
        }
        return stack;
    }

}
