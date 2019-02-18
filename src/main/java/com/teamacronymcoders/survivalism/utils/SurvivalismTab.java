package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SurvivalismTab extends CreativeTabs {

    public SurvivalismTab() {
        super(Survivalism.MODID);
        setBackgroundImageName("item_search.png");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.storage);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
