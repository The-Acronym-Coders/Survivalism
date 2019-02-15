package com.teamacronymcoders.survivalism.utils;

import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class SurvivalismTab extends CreativeTabs {

    public SurvivalismTab() {
        super(Survivalism.MODID);
        setBackgroundImageName("item_search.png");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Item.getItemFromBlock(Blocks.BARRIER));
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
