package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.ModItems;
import com.teamacronymcoders.survivalism.common.blocks.BlockCompostBin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;

public class TileCompostBin extends TileEntity {

    private int compost;

    public TileCompostBin() {
    }

    public void addCompost(EntityPlayer player) {
        if (world.getBlockState(getPos()).getBlock() instanceof BlockCompostBin) {
            IBlockState state = world.getBlockState(getPos());
            compost = state.getValue(BlockCompostBin.Compost_Amount);
            if (!(compost > 9)) {
                compost += 1;
                IBlockState newState = world.getBlockState(getPos()).withProperty(BlockCompostBin.Compost_Amount, compost);
                world.setBlockState(getPos(), newState);
            } else {
                IBlockState newState = world.getBlockState(getPos()).withProperty(BlockCompostBin.Compost_Amount, 0);
                world.setBlockState(getPos(), newState);
                player.inventory.addItemStackToInventory(new ItemStack(ModItems.compost, MathHelper.getInt(world.rand, 1, 4)));
            }
        }
    }
}
