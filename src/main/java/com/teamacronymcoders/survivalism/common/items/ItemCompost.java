package com.teamacronymcoders.survivalism.common.items;

import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemCompost extends Item {
    public ItemCompost() {
        setTranslationKey("compost");
        setCreativeTab(Survivalism.TAB);
    }

    /**
     * Credit goes out to Natura for this piece of code.
     * The project is licences under CC0 which is a public domain licence.
     * Regardless I will always attribute great code to their creators.
     * So credit where credit is due!
     */
    private static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target, EntityPlayer player, EnumHand hand) {
        IBlockState iblockstate = worldIn.getBlockState(target);
        BonemealEvent event = new BonemealEvent(player, worldIn, target, iblockstate, hand, stack);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        if (event.getResult() == Event.Result.ALLOW) {
            return true;
        }
        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();
            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Credit goes out to Natura for this piece of code.
     * The project is licences under CC0 which is a public domain licence.
     * Regardless I will always attribute great code to their creators.
     * So credit where credit is due!
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            ItemStack itemstack = player.getHeldItem(hand);
            BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();
            int posY = pos.getY();
            boolean planted = false;

            for (int posX = pos.getX() - 1; posX <= pos.getX() + 1; posX++) {
                for (int posZ = pos.getZ() - 1; posZ <= pos.getZ() + 1; posZ++) {
                    BlockPos position = mutableblockpos.setPos(posX, posY, posZ);
                    if (player.canPlayerEdit(position, facing, itemstack) && player.canPlayerEdit(position.up(), facing, itemstack)) {
                        if (applyBonemeal(itemstack, worldIn, position, player, hand)) {
                            planted = true;
                            if (!worldIn.isRemote) {
                                worldIn.playEvent(2005, position, 0);
                            }
                        }
                    }
                }
            }
            if (planted) {
                if (!player.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                if (itemstack.getCount() < 1) {
                    ForgeEventFactory.onPlayerDestroyItem(player, itemstack, hand);
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
    }
}
