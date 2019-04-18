package com.teamacronymcoders.survivalism.common.tiles.barrels;

import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelStorage;
import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrelStorage;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileBarrelStorage extends TileBarrelBase implements ITickable, IHasGui {

    protected FluidTank input = new FluidTank(SurvivalismConfigs.storageTankSize);
    protected ItemStackHandler inv = new UpdatingItemStackHandler(9, this);

    @Override
    public void update() {
        if (!this.isSealed()) {
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

    protected void processRaining() {
        World world = getWorld();
        if (!isSealed()) {
            if (SurvivalismConfigs.canBarrelsFillInRain && world.isRaining() && world.canBlockSeeSky(getPos())) {
                if (SurvivalismConfigs.shouldBarrelsRespectRainValueOfBiomes) {
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

    // Capabilities
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(input);
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
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


    // Getters/Setters
    public FluidTank getInput() {
        return input;
    }

    public ItemStackHandler getInv() {
        return inv;
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
        return new GUIBarrelStorage(this, getContainer(entityPlayer, world, blockPos));
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return new ContainerBarrelStorage(entityPlayer.inventory, this);
    }

    public boolean onBlockActivated(EntityPlayer player) {
        GuiOpener.openTileEntityGui(Survivalism.INSTANCE, player, this.getWorld(), this.getPos());
        return true;
    }
}