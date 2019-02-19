package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class HwylaCompatProviderBarrel implements IWailaDataProvider {
    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        IBlockState state = accessor.getBlockState();
        TileEntity te = accessor.getTileEntity();
        if (config.getConfig("survivalism.barrel")) {
            if (accessor.getBlock() instanceof BlockBarrelBase && te instanceof TileBarrelBase) {
                NBTTagCompound compound = accessor.getNBTData();

                currenttip.add(I18n.format("survivalism.hwyla.barrel.sealed") + " " + state.getValue(BlockBarrelBase.SEALED));

                if (compound.hasKey("input")) {
                    FluidStack stack = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("input"));
                    if (stack != null) {
                        currenttip.add(stack.getLocalizedName() + " : " + stack.amount + " / " + compound.getInteger("capacityI"));
                    }
                }

                if (compound.hasKey("output")) {
                    FluidStack stack = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("output"));
                    if (stack != null) {
                        currenttip.add(stack.getLocalizedName() + " : " + stack.amount + " / " + compound.getInteger("capacityO"));
                    }
                }

                if (compound.hasKey("working")) {
                    currenttip.add("Working: " + compound.getBoolean("working"));
                }
                if (compound.hasKey("ticksR") && compound.hasKey("ticksC")) {
                    int ticksLeft;
                    if (state.getValue(BlockBarrelBase.SEALED)) {
                        ticksLeft = (compound.getInteger("ticksR") - compound.getInteger("ticksC")) / 20;
                        currenttip.add("Time Left: " + HelperString.getDurationString(ticksLeft));
                    } else {
                        ticksLeft = (compound.getInteger("ticksR") - compound.getInteger("ticksC")) / 20;
                        currenttip.remove("Time Left: " + HelperString.getDurationString(ticksLeft));
                    }
                }
            }
        }
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileBarrelBase) {
            if (te instanceof TileBarrelBrewing) {
                TileBarrelBrewing tile = (TileBarrelBrewing) te;
                if (tile.getInput() != null) {
                    tag.setInteger("capacityI", tile.getInput().getCapacity());
                    tag.setTag("input", tile.getInput().writeToNBT(new NBTTagCompound()));
                }
                if (tile.getOutput() != null) {
                    tag.setInteger("capacityO", tile.getOutput().getCapacity());
                    tag.setTag("output", tile.getOutput().writeToNBT(new NBTTagCompound()));
                }

                if (tile.getWorking()) {
                    tag.setBoolean("working", tile.getWorking());
                    tag.setInteger("ticksR", tile.getRecipe().getTicks());
                    tag.setInteger("ticksC", tile.getTicks());
                } else {
                    tag.setBoolean("working", tile.getWorking());
                }
            } else if (te instanceof TileBarrelSoaking) {
                TileBarrelSoaking tile = (TileBarrelSoaking) te;
                if (tile.getInput() != null) {
                    tag.setInteger("capacityI", tile.getInput().getCapacity());
                    tag.setTag("input", tile.getInput().writeToNBT(new NBTTagCompound()));
                }
                if (tile.getWorking()) {
                    tag.setBoolean("working", tile.getWorking());
                    if (tile.getRecipe() != null) {
                        tag.setInteger("ticksR", tile.getRecipe().getTicks());
                        tag.setInteger("ticksC", tile.getTicks());
                    }
                } else {
                    tag.setBoolean("working", tile.getWorking());
                }
            } else {
                TileBarrelStorage tile = (TileBarrelStorage) te;
                if (tile.getInput() != null) {
                    tag.setInteger("capacityI", tile.getInput().getCapacity());
                    tag.setTag("input", tile.getInput().writeToNBT(new NBTTagCompound()));
                }
            }
        }
        return tag;
    }
}
