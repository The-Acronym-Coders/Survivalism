package com.teamacronymcoders.survivalism.utils.helpers;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class StringHelper {

    public static String formatItemStacks(List<ItemStack> stacks) {
        if (stacks == null) {
            return "null";
        }

        String stackString = stacks.stream().map(s -> s + ", ").collect(Collectors.joining());
        if (!stackString.isEmpty()) {
            stackString = stackString.substring(0, stackString.length() - 2);
        }
        return stackString;
    }
}
