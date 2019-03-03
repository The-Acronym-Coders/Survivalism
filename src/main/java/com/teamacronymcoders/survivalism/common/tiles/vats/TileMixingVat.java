package com.teamacronymcoders.survivalism.common.tiles.vats;

import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.ContainerMixing;
import com.teamacronymcoders.survivalism.client.gui.GUIMixingVat;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.MixingFluidWrapper;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipeManager;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.network.MessageUpdateMixingVat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileMixingVat extends TileEntity implements IUpdatingInventory, IHasGui {

    protected MixingRecipe recipe;
    protected FluidTank output = new FluidTank(SurvivalismConfigs.mixingOutputTankSize);
    private int clicks = 0;
    private FluidStack failed;
    private FluidTank main = new FluidTank(SurvivalismConfigs.mixingInputTankSize);
    private FluidTank secondary = new FluidTank(SurvivalismConfigs.mixingSecondaryTankSize);
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

    public boolean onMix() {
        processMixing();
        return working;
    }

    protected void processMixing() {
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

        if (clicks++ >= recipe.getClicks() && canResolveRecipe()) {
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

    protected boolean canResolveRecipe() {
        if (main.getFluid() != null && main.getFluidAmount() - recipe.getMain().amount >= 0) {
            if (output.getFluid() == null || output.getFluidAmount() + recipe.getOutput().amount <= SurvivalismConfigs.mixingOutputTankSize) {
                if (recipe.getSecondary() != null && recipe.getCatalyst() != Ingredient.EMPTY) {
                    return secondary.getFluid() != null && secondary.getFluidAmount() - recipe.getSecondary().amount >= 0 && !handler.getStackInSlot(0).isEmpty();
                }
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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("items", handler.serializeNBT());
        compound.setTag("mainTank", main.writeToNBT(new NBTTagCompound()));
        compound.setTag("secondaryTank", secondary.writeToNBT(new NBTTagCompound()));
        compound.setTag("outputTank", output.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handler.deserializeNBT(compound.getCompoundTag("items"));
        main.readFromNBT(compound.getCompoundTag("mainTank"));
        secondary.readFromNBT(compound.getCompoundTag("secondaryTank"));
        output.readFromNBT(compound.getCompoundTag("outputTank"));
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
    @SideOnly(Side.CLIENT)
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new GUIMixingVat(this, getContainer(entityPlayer, world, blockPos));
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new ContainerMixing(entityPlayer.inventory, this);
    }

    public boolean onBlockActivated(EntityPlayer player) {
        GuiOpener.openTileEntityGui(Survivalism.INSTANCE, player, this.getWorld(), this.getPos());
        return true;
    }
}
