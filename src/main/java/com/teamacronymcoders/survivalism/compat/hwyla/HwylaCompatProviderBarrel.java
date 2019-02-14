package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
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
            if (accessor.getBlock() instanceof BlockBarrel && te instanceof TileBarrel) {
                FluidTank output = ((TileBarrel) te).getOutput();
                if (output != null) {
                    if (output.getFluid() != null) {
                        currenttip.add(output.getFluid().getLocalizedName() + " : " + output.getFluid().amount + " / " + output.getCapacity());
                    }
                }
                currenttip.add(I18n.format("survivalism.hwyla.barrel.state") + " " + HelperString.cleanBarrelStateString(String.valueOf(state.getValue(BlockBarrel.BARREL_STATE))));
                currenttip.add(I18n.format("survivalism.hwyla.barrel.sealed") + " " + state.getValue(BlockBarrel.SEALED));
            }
        }
        return currenttip;
    }
}
