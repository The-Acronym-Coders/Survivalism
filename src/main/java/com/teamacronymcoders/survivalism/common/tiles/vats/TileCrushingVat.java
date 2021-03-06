package com.teamacronymcoders.survivalism.common.tiles.vats;

import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.ContainerCrushing;
import com.teamacronymcoders.survivalism.client.gui.GUICrushingVat;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipeManager;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.event.crushing.CrushingEvent;
import com.teamacronymcoders.survivalism.utils.event.crushing.JumpForceEvent;
import com.teamacronymcoders.survivalism.utils.helpers.MathHelper;
import crafttweaker.mc1120.item.VanillaIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileCrushingVat extends TileEntity implements IHasGui, IUpdatingInventory {

    private CrushingRecipe curRecipe;
    public double jumps = 0.0D;
    private ItemStack failedMatch;
    private FluidTank tank = new FluidTank(SurvivalismConfigs.crushingTankSize);
    private ItemStackHandler inputInv = new UpdatingItemStackHandler(1, this);
    private ItemStackHandler outputInv = new UpdatingItemStackHandler(1, this);
    private double jumpBase = SurvivalismConfigs.baseJumpValue;
    private double multiplierBase;
    private int fluidLastJump;

    public void onJump(EntityLivingBase jumper, TileCrushingVat vat) {
        boolean dirty = false;

        if (jumper == null || inputInv.getStackInSlot(0).isEmpty()) {
            curRecipe = null;
            jumps = 0;
            return;
        }

        if (fluidLastJump != tank.getFluidAmount()) {
            fluidLastJump = tank.getFluidAmount();
            dirty = true;
        }

        if ((curRecipe == null || !curRecipe.matches(jumper, inputInv.getStackInSlot(0))) && inputInv.getStackInSlot(0) != failedMatch) {
            curRecipe = CrushingRecipeManager.getRecipe(jumper, inputInv.getStackInSlot(0));
            if (curRecipe == null) {
                failedMatch = inputInv.getStackInSlot(0);
                return;
            } else {
                failedMatch = ItemStack.EMPTY;
            }
        } else if (curRecipe == null) {
            return;
        }

        if (!MinecraftForge.EVENT_BUS.post(new CrushingEvent.Post(jumper, vat))) {
            multiplierBase += CrushingRecipeManager.getBootsMultiplier(jumper);
            MinecraftForge.EVENT_BUS.post(new JumpForceEvent.BaseModification(jumper, this, jumpBase));
            MinecraftForge.EVENT_BUS.post(new JumpForceEvent.MultiplierModification(jumper, this, multiplierBase));
            jumps += (jumpBase * multiplierBase);

            if (jumps >= curRecipe.getJumps() && canInsertResults()) {
                if (MathHelper.tryPercentage(curRecipe.getItemChance())) {
                    outputInv.insertItem(0, curRecipe.getOutputStack().copy(), false);
                }
                tank.fill(curRecipe.getOutput(), true);
                jumps = 0;
                dirty = true;
                if (curRecipe.getInput() instanceof VanillaIngredient) {
                    inputInv.getStackInSlot(0).shrink(((VanillaIngredient) curRecipe.getInput()).getIngredient().getAmount());
                } else {
                    inputInv.getStackInSlot(0).shrink(1);
                }
            }

            jumpBase = SurvivalismConfigs.baseJumpValue;
            multiplierBase = 0d;
        }

        if (dirty) {
            markDirty();
        }
    }


    private boolean canInsertResults() {
        if (tank.getFluid() == null && (curRecipe.getOutputStack().isEmpty() || outputInv.getStackInSlot(0).isEmpty())) {
            return true;
        }
        Fluid inTank = tank.getFluid().getFluid();
        FluidStack output = curRecipe.getOutput();
        if (!(inTank.getName().equals(output.getFluid().getName()) && tank.getFluidAmount() + output.amount <= tank.getCapacity())) {
            return false;
        }
        ItemStack out = curRecipe.getOutputStack();
        ItemStack cur = outputInv.getStackInSlot(0);
        if (cur.isEmpty()) {
            return true;
        }
        if (!out.isEmpty() && !ItemHandlerHelper.canItemStacksStack(cur, out)) {
            return false;
        }
        return out.isEmpty() || cur.getCount() + out.getCount() <= cur.getMaxStackSize();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("tank"));
        inputInv.deserializeNBT(compound.getCompoundTag("inputItems"));
        outputInv.deserializeNBT(compound.getCompoundTag("outputItems"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        compound.setTag("inputItems", inputInv.serializeNBT());
        compound.setTag("outputItems", outputInv.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputInv);
            } else if (facing == EnumFacing.DOWN) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputInv);
            }
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return true;
            }
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

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    public ItemStackHandler getInputInv() {
        return inputInv;
    }

    public ItemStackHandler getOutputInv() {
        return outputInv;
    }

    public FluidTank getTank() {
        return tank;
    }

    public CrushingRecipe getRecipe() {
        return curRecipe;
    }

    public void addJumpBaseMod(double modifier) {
        jumpBase += modifier;
    }

    public void removeJumpBaseMod(double modifier) {
        jumpBase -= modifier;
        if (jumpBase < 0d) {
            jumpBase = 0d;
        }
    }

    public double getJumpBase() {
        return jumpBase;
    }

    public void addMultiplierBaseMod(double modifier) {
        multiplierBase += modifier;
    }

    public void removeMultiplierBaseMod(double modifier) {
        multiplierBase -= modifier;
        if (multiplierBase < 0d) {
            multiplierBase = 0d;
        }
    }

    public double getMultiplierBase() {
        return multiplierBase;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new GUICrushingVat(this, getContainer(entityPlayer, world, blockPos));
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new ContainerCrushing(entityPlayer.inventory, this);
    }

    public boolean onBlockActivated(EntityPlayer player) {
        GuiOpener.openTileEntityGui(Survivalism.INSTANCE, player, this.getWorld(), this.getPos());
        return true;
    }
}
