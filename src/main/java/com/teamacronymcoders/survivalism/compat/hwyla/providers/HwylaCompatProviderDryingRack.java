package com.teamacronymcoders.survivalism.compat.hwyla.providers;

import com.teamacronymcoders.survivalism.common.blocks.BlockDryingRack;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.TileDryingRack;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class HwylaCompatProviderDryingRack implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof BlockDryingRack && accessor.getTileEntity() instanceof TileDryingRack) {
            NBTTagCompound compound = accessor.getNBTData();
            if (compound.hasKey("item")) {
                tooltip.add(I18n.format("survivalism.hwyla.items.items") + " " + compound.getString("item"));
            }
            if (compound.hasKey("ticksR") && compound.hasKey("ticksC")) {
                int ticksLeft;
                ticksLeft = (compound.getInteger("ticksR") - compound.getInteger("ticksC")) / 20;
                tooltip.add("Time Left: " + HelperString.getDurationString(ticksLeft));
            }
        }
        return tooltip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileDryingRack) {
            TileDryingRack rack = (TileDryingRack) te;
            if (rack.getHandler() != null) {
                tag.setString("item", rack.getHandler().getStackInSlot(0).getCount() + "x " + rack.getHandler().getStackInSlot(0).getDisplayName());
            }
            if (rack.isWorking() && rack.getRecipe() != null) {
                tag.setInteger("ticksR", rack.getRecipe().getTicks());
                tag.setInteger("ticksC", rack.getTicks());
            }
        }
        return tag;
    }

}
