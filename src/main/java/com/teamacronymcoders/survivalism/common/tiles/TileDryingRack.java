package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRackRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.drying.modifiers.DryingModifierManager;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileDryingRack extends TileEntity implements ITickable, IUpdatingInventory {

    private UpdatingItemStackHandler handler = new UpdatingItemStackHandler(1, this) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private DryingRecipe recipe;
    private int ticks;
    private boolean working = false;

    @Override
    public void update() {
        processDrying();
    }

    protected void processDrying() {
        ItemStack stack = handler.getStackInSlot(0);
        if (stack != ItemStack.EMPTY) {
            if (recipe == null || !recipe.matches(this)) {
                recipe = DryingRackRecipeManager.getDryingRecipe(this);
                working = true;
            }
            if (recipe == null) {
                ticks = 0;
                working = false;
                return;
            }
            getTicksToApply();
            doRainCheck();
            doProcessHandling();
        } else {
            ticks = 0;
            working = false;
        }
    }

    protected void getTicksToApply() {
        if (SurvivalismConfigs.shouldDryingRackUseModifiers) {
            BlockPos checkPos = getPos().down();
            double block_modifier = DryingModifierManager.getModifier(world.getBlockState(checkPos));
            double biome_modifier = DryingModifierManager.getModifier(world.getBiome(checkPos));
            double ticksToAdd = (1d * block_modifier) * biome_modifier;
            ticks += Math.round(ticksToAdd);
        } else {
            ticks++;
        }
    }

    protected void doRainCheck() {
        if (getWorld().isRaining() && getWorld().canBlockSeeSky(getPos()) && world.getBiome(getPos()).canRain()) {
            ticks = 0;
            working = false;
        } else {
            working = true;
        }
    }

    protected void doProcessHandling() {
        if (ticks >= recipe.getTicks()) {
            handler.setStackInSlot(0, recipe.getOutput());
            ticks = 0;
            working = false;
            world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 8);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handler.deserializeNBT(compound.getCompoundTag("item"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("item", handler.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !working) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !working) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }

    public void setStack(ItemStack stack) {
        this.handler.setStackInSlot(0, stack);
    }

    public boolean isWorking() {
        return working;
    }

    public int getTicks() {
        return ticks;
    }

    public DryingRecipe getRecipe() {
        return recipe;
    }

    public UpdatingItemStackHandler getHandler() {
        return handler;
    }
}
