package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

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
                FluidTank input = null;
                FluidTank output = null;
                currenttip.add(I18n.format("survivalism.hwyla.barrel.sealed") + " " + state.getValue(BlockBarrelBase.SEALED));

                if (te instanceof TileBarrelBrewing) {
                    input = ((TileBarrelBrewing) te).getInput();
                }

                if (te instanceof TileBarrelBrewing) {
                    output = ((TileBarrelBrewing) te).getOutput();
                }

                if (input != null) {
                    if (input.getFluid() != null) {
                        currenttip.add(input.getFluid().getLocalizedName() + " : " + input.getFluidAmount() + " / " + input.getCapacity());
                    }
                }

                if (output != null) {
                    if (output.getFluid() != null) {
                        currenttip.add(output.getFluid().getLocalizedName() + " : " + output.getFluidAmount() + " / " + output.getCapacity());
                    }
                }

            }
        }
        return currenttip;
    }
}
