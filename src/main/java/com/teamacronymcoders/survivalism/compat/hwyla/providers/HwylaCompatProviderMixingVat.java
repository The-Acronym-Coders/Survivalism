package com.teamacronymcoders.survivalism.compat.hwyla.providers;

import com.teamacronymcoders.survivalism.common.blocks.vats.BlockMixingVat;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
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

import javax.annotation.Nonnull;
import java.util.List;

public class HwylaCompatProviderMixingVat implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof BlockMixingVat && accessor.getTileEntity() instanceof TileMixingVat) {
            NBTTagCompound compound = accessor.getNBTData();
            FluidStack main = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("main"));
            FluidStack secondary = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("secondary"));
            FluidStack output = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("output"));
            if (main != null) {
                tooltip.remove(main.getLocalizedName() + ": " + main.amount + " / " + compound.getInteger("capacityM") + " mB");
                tooltip.add(I18n.format("survivalism.hwyla.fluids.main") + " " + main.getLocalizedName() + " - " + main.amount + "/" + compound.getInteger("capacityM"));
            }
            if (secondary != null) {
                tooltip.remove(secondary.getLocalizedName() + ": " + secondary.amount + " / " + compound.getInteger("capacityS") + " mB");
                tooltip.add(I18n.format("survivalism.hwyla.fluids.secondary") + " " + secondary.getLocalizedName() + " - " + secondary.amount + "/" + compound.getInteger("capacityS"));
            }
            if (output != null) {
                tooltip.remove(output.getLocalizedName() + ": " + output.amount + " / " + compound.getInteger("capacityO") + " mB");
                tooltip.add(I18n.format("survivalism.hwyla.fluids.output") + " " + output.getLocalizedName() + " - " + output.amount + "/" + compound.getInteger("capacityO"));
            }
            if (compound.hasKey("catalyst")) {
                tooltip.add(I18n.format("survivalism.hwyla.items.input") + " " + compound.getString("inputItem"));
            }
            if (compound.hasKey("clicksCur") && compound.hasKey("clicks")) {
                int stirs = compound.getInteger("clicks");
                int curStirs = compound.getInteger("clicksCur");
                tooltip.add(I18n.format("survivalism.hwyla.stirs") + " " + (stirs - curStirs));
            }
        }
        return tooltip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileMixingVat) {
            TileMixingVat vat = (TileMixingVat) te;
            if (vat.getMain() != null && vat.getMain().getFluid() != null) {
                tag.setTag("main", vat.getMain().writeToNBT(new NBTTagCompound()));
                tag.setInteger("capacityM", vat.getMain().getCapacity());
            }
            if (vat.getSecondary() != null && vat.getSecondary().getFluid() != null) {
                tag.setTag("secondary", vat.getSecondary().writeToNBT(new NBTTagCompound()));
                tag.setInteger("capacityS", vat.getSecondary().getCapacity());
            }
            if (vat.getOutput() != null && vat.getOutput().getFluid() != null) {
                tag.setTag("output", vat.getOutput().writeToNBT(new NBTTagCompound()));
                tag.setInteger("capacityO", vat.getOutput().getCapacity());
            }
            tag.setInteger("clicksCur", vat.getClicks());
            if (vat.getRecipe() != null) {
                tag.setInteger("clicks", vat.getRecipe().getClicks());
            }
        }
        return tag;
    }
}
