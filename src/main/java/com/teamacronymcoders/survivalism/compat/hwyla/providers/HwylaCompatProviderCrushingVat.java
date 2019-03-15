package com.teamacronymcoders.survivalism.compat.hwyla.providers;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class HwylaCompatProviderCrushingVat implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (config.getConfig("survivalism.crushing_vat")) {
            if (accessor.getBlock() instanceof BlockCrushingVat && te instanceof TileCrushingVat) {
                NBTTagCompound compound = accessor.getNBTData();
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("inputFluid"));
                if (compound.hasKey("inputCap")) {
                    if (fluid != null) {
                        currenttip.remove(fluid.getLocalizedName() + ": " + fluid.amount + " / " + compound.getInteger("capacityI") + " mB");
                        currenttip.add(I18n.format("survivalism.hwyla.fluids.input"));
                        currenttip.add(fluid.getLocalizedName() + ": " + fluid.amount + "/" + compound.getInteger("inputCap"));
                    }
                }
                if (compound.hasKey("inputItem")) {
                    currenttip.add(I18n.format("survivalism.hwyla.items.input") + " " + compound.getString("inputItem"));
                }
                if (compound.hasKey("outputItem")) {
                    currenttip.add(I18n.format("survivalism.hwyla.items.output") + " " + compound.getString("outputItem"));
                }
                if (compound.hasKey("jumpCur") && compound.hasKey("jumps")) {
                    int jumps = compound.getInteger("jumps");
                    double curJumps = compound.getDouble("jumpCur");
                    String jumpStr = curJumps + "/" + jumps;
                    if (jumps != 0 && curJumps != 0.0d) {
                        currenttip.add(I18n.format("survivalism.hwyla.jumps") + " " + jumpStr);
                    }
                }
            }
        }
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileCrushingVat) {
            TileCrushingVat vat = (TileCrushingVat) te;
            if (vat.getTank() != null && vat.getTank().getFluid() != null) {
                tag.setTag("inputFluid", vat.getTank().writeToNBT(new NBTTagCompound()));
                tag.setInteger("inputCap", vat.getTank().getCapacity());
            }
            if (vat.getInputInv() != null) {
                tag.setString("inputItem", vat.getInputInv().getStackInSlot(0).getCount() + "x " + vat.getInputInv().getStackInSlot(0).getDisplayName());
            }
            if (vat.getOutputInv() != null) {
                tag.setString("outputItem", vat.getOutputInv().getStackInSlot(0).getCount() + "x " + vat.getOutputInv().getStackInSlot(0).getDisplayName());
            }
            tag.setDouble("jumpCur", vat.jumps);
            if (vat.getRecipe() != null) {
                tag.setInteger("jumps", vat.getRecipe().getJumps());
            }
        }
        return tag;
    }
}
