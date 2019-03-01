package com.teamacronymcoders.survivalism.common.tiles.vats;

import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.MixingFluidWrapper;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipeManager;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileMixingVat extends TileEntity implements IUpdatingInventory, IHasGui {

    private int clicks = 0;
    protected MixingRecipe recipe;
    private FluidStack failed;
    private FluidTank main = new FluidTank(SurvivalismConfigs.mixingInputTankSize);
    private FluidTank secondary = new FluidTank(SurvivalismConfigs.mixingSecondaryTankSize);
    protected FluidTank output = new FluidTank(SurvivalismConfigs.mixingOutputTankSize);
    private IFluidHandler fluidHandler = new MixingFluidWrapper(main, secondary, output);
    private UpdatingItemStackHandler handler = new UpdatingItemStackHandler(1, this);
    private boolean working = false;

    public TileMixingVat() {
        main.setCanDrain(false);
        secondary.setCanDrain(false);
        output.setCanFill(false);
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    public void onMix() {
        boolean dirty = false;

        if (recipe != null && recipe.getCatalyst() != Ingredient.EMPTY && handler.getStackInSlot(0).isEmpty()) {
            working = false;
            recipe = null;
            clicks = 0;
            return;
        }

        if (recipe == null || !recipe.matches(this)) {
            recipe = MixingRecipeManager.getMixingRecipe(this);
            if (recipe == null) {
                working = false;
                failed = main.getFluid();
                return;
            } else {
                working = true;
                failed = null;
            }
        }

        if (clicks++ >= 4 && canResolveRecipe()) {
            main.drainInternal(recipe.getMain(), true);
            if (recipe.getSecondary() != null) {
                secondary.drainInternal(recipe.getSecondary(), true);
            }
            if (recipe.getCatalyst() != Ingredient.EMPTY) {
                handler.getStackInSlot(0).shrink(1);
            }
            output.fillInternal(recipe.getOutput(), true);
            dirty = true;
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    protected boolean canResolveRecipe() {
        if (main.getFluid() != null && main.getFluidAmount() - recipe.getMain().amount >= 0) {
            if (output.getFluid() == null || output.getFluidAmount() + recipe.getOutput().amount <= SurvivalismConfigs.mixingOutputTankSize) {
                if (recipe.getSecondary() != null) {
                    return secondary.getFluid() != null && secondary.getFluidAmount() - recipe.getSecondary().amount >= 0;
                }
                if (recipe.getCatalyst() != Ingredient.EMPTY) {
                    return !handler.getStackInSlot(0).isEmpty();
                }
                return true;
            }
        }
        return false;
    }

    public FluidTank getMain() {
        return main;
    }

    public FluidTank getSecondary() {
        return secondary;
    }

    public FluidTank getOutput() {
        return output;
    }

    public UpdatingItemStackHandler getHandler() {
        return handler;
    }

    @Override
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return null;
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return null;
    }
}
