package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class SurvivalismTab extends CreativeTabs {

    public SurvivalismTab() {
        super(Survivalism.MODID);
        setBackgroundImageName("item_search.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.WOODEN_AXE);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
