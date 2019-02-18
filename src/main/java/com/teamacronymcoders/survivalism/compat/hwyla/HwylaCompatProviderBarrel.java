package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
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
                String localKeyInput;
                String localKeyOutput = "";

                if (te instanceof TileBarrelBrewing) {
                    localKeyInput = ((TileBarrelBrewing) te).getInput().getFluid().getUnlocalizedName();
                    localKeyOutput = ((TileBarrelBrewing) te).getOutput().getFluid().getUnlocalizedName();
                } else if (te instanceof TileBarrelSoaking) {
                    localKeyInput = ((TileBarrelSoaking) te).getInput().getFluid().getUnlocalizedName();
                } else {
                    localKeyInput = ((TileBarrelStorage) te).getInput().getFluid().getUnlocalizedName();
                }

                currenttip.add(I18n.format("survivalism.hwyla.barrel.sealed") + " " + state.getValue(BlockBarrelBase.SEALED));
                if (accessor.getNBTData().hasKey("inputA") && accessor.getNBTData().hasKey("inputC")) {
                    currenttip.add(I18n.format(localKeyInput) + " : " + accessor.getNBTData().getInteger("inputA") + " / " + accessor.getNBTData().getInteger("inputC"));
                }
                if (accessor.getNBTData().hasKey("outputA") && accessor.getNBTData().hasKey("outputC")) {
                    currenttip.add(I18n.format(localKeyOutput) + " : " + accessor.getNBTData().getInteger("outputA") + " / " + accessor.getNBTData().getInteger("outputC"));
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
                tag.setInteger("inputA", tile.getInput().getFluidAmount());
                tag.setInteger("inputC", tile.getInput().getCapacity());

                tag.setInteger("outputA", tile.getOutput().getFluidAmount());
                tag.setInteger("outputC", tile.getOutput().getCapacity());

            } else if (te instanceof TileBarrelSoaking) {
                TileBarrelSoaking tile = (TileBarrelSoaking) te;
                tag.setInteger("inputA", tile.getInput().getFluidAmount());
                tag.setInteger("inputC", tile.getInput().getCapacity());
            } else {
                TileBarrelStorage tile = (TileBarrelStorage) te;
                tag.setInteger("inputA", tile.getInput().getFluidAmount());
                tag.setInteger("inputC", tile.getInput().getCapacity());
            }
        }
        return tag;
    }
}
