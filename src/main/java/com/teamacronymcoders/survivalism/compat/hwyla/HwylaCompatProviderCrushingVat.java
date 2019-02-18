package com.teamacronymcoders.survivalism.compat.hwyla;

import com.teamacronymcoders.survivalism.common.blocks.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import java.util.List;

public class HwylaCompatProviderCrushingVat implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (config.getConfig("survivalism.crushing_vat")) {
            if (accessor.getBlock() instanceof BlockCrushingVat && te instanceof TileCrushingVat) {
                if (((TileCrushingVat) te).getInputInv() != null) {
                    ItemStack input = ((TileCrushingVat) te).getInputInv().getStackInSlot(0);
                    if (!input.isEmpty()) {
                        currenttip.add(I18n.format("survivalism.hwyla.items.input") + " " + input.getCount() + "x " + input.getDisplayName());
                    }
                }
                if (((TileCrushingVat) te).getOutputInv() != null) {
                    ItemStack output = ((TileCrushingVat) te).getOutputInv().getStackInSlot(0);
                    if (!output.isEmpty()) {
                        currenttip.add(I18n.format("survivalism.hwyla.items.output") + " " + output.getCount() + "x " + output.getDisplayName());
                    }
                }
                if (((TileCrushingVat) te).getTank() != null) {
                    FluidTank tank = ((TileCrushingVat) te).getTank();
                    if (tank.getFluid() != null) {
                        currenttip.add(tank.getFluid().getLocalizedName() + ": " + tank.getFluidAmount() + "/" + tank.getCapacity());
                    }
                }
                if (((TileCrushingVat) te).curRecipe != null) {
                    int jumps = ((TileCrushingVat) te).curRecipe.getJumps();
                    double curJumps = ((TileCrushingVat) te).jumps;
                    String jumpStr = curJumps + "/" + jumps;
                    if (jumps != 0 && curJumps != 0.0d) {
                        currenttip.add(I18n.format("survivalism.hwyla.jumps") + " " + jumpStr);
                    }
                }
            }
        }
        return currenttip;
    }
}
