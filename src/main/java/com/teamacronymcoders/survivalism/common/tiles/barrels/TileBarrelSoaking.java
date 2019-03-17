package com.teamacronymcoders.survivalism.common.tiles.barrels;

import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelSoaking;
import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrelSoaking;
import com.teamacronymcoders.survivalism.common.inventory.SoakingWrapper;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import crafttweaker.mc1120.item.VanillaIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileBarrelSoaking extends TileBarrelBase implements ITickable, IHasGui {

    protected int ticks = 0;
    protected FluidTank input = new FluidTank(SurvivalismConfigs.soakingTankSize);
    protected SoakingRecipe recipe;
    protected UpdatingItemStackHandler inv = new UpdatingItemStackHandler(2, this);
    protected SoakingWrapper wrapper = new SoakingWrapper(inv);
    private boolean working = false;

    public TileBarrelSoaking() {
        input.setCanDrain(false);
    }

    @Override
    public void update() {
        super.update();
        if (this.isSealed()) {
            processSoaking();
            if (recipe != null) {
                working = true;
            }
            this.markDirty();
        } else {
            ticks = 0;
            working = false;
            processRaining();
            this.markDirty();
        }
    }

    // NBT
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        input.readFromNBT(compound.getCompoundTag("inputTank"));
        inv.deserializeNBT(compound.getCompoundTag("items"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", input.writeToNBT(new NBTTagCompound()));
        compound.setTag("items", inv.serializeNBT());
        return super.writeToNBT(compound);
    }


    // Capabilities
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(input);
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(wrapper);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !isSealed()) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    // Process
    protected void processSoaking() {
        FluidStack fluid = input.getFluid();
        if (fluid != null && fluid.amount > 0) {
            if (recipe == null || !recipe.matches(this)) {
                recipe = BarrelRecipeManager.getSoakingRecipe(this);
            }
            if (recipe == null) {
                return;
            }
            if (ticks++ >= recipe.getTicks()) {
                ItemStack curOutput = inv.getStackInSlot(1);
                if (!curOutput.isEmpty() && !ItemHandlerHelper.canItemStacksStack(curOutput, recipe.getOutput())) {
                    return;
                }
                if (!curOutput.isEmpty() && curOutput.getCount() + recipe.getOutput().getCount() > curOutput.getMaxStackSize()) {
                    return;
                }
                ticks = 0;
                if (HelperMath.tryPercentage(recipe.getFluidUseChance())) {
                    input.getFluid().amount -= recipe.getInput().amount;
                }
                if (recipe.getInputItem() instanceof VanillaIngredient) {
                    inv.getStackInSlot(0).shrink(((VanillaIngredient) recipe.getInputItem()).getIngredient().getAmount());
                } else {
                    inv.getStackInSlot(0).shrink(1);
                }

                inv.insertItem(1, recipe.getOutput().copy(), false);
            }
        }
    }

    protected void processRaining() {
        World world = getWorld();
        if (!isSealed()) {
            if (SurvivalismConfigs.canBarrelsFillInRain && world.isRaining() && world.canBlockSeeSky(getPos())) {
                if (!SurvivalismConfigs.shouldBarrelsRespectRainValueOfBiomes) {
                    if (world.getBiome(getPos()).canRain()) {
                        FluidStack fluidStack = BarrelRecipeManager.getBiomeFluidStack(world.getBiome(getPos())).copy();
                        input.fill(fluidStack, true);
                    }
                } else {
                    FluidStack fluidStack = BarrelRecipeManager.getBiomeFluidStack(world.getBiome(getPos())).copy();
                    input.fill(fluidStack, true);
                }
            }
        }
    }


    // Getters/Setters
    public FluidTank getInput() {
        return input;
    }

    public ItemStackHandler getInv() {
        return inv;
    }

    public boolean getWorking() {
        return working;
    }

    public SoakingRecipe getRecipe() {
        return recipe;
    }

    public int getTicks() {
        return ticks;
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
    @SideOnly(Side.CLIENT)
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new GUIBarrelSoaking(this, getContainer(entityPlayer, world, blockPos));
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new ContainerBarrelSoaking(entityPlayer.inventory, this);
    }

    public boolean onBlockActivated(EntityPlayer player) {
        GuiOpener.openTileEntityGui(Survivalism.INSTANCE, player, this.getWorld(), this.getPos());
        return true;
    }
}
