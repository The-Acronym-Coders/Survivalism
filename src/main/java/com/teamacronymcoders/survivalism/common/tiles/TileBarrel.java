package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelBrewing;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelSoaking;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelStorage;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.defaults.TileBase;
import com.teamacronymcoders.survivalism.common.inventory.BarrelHandler;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import com.teamacronymcoders.survivalism.utils.storages.BarrelState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class TileBarrel extends TileBase implements ITickable, IUpdatingInventory {

    public static final int TANK_CAPACITY = 16000;
    public static final int STORAGE_SIZE = 9;

    protected int prevInputAmount = 0;
    protected FluidTank input = new FluidTank(TANK_CAPACITY);
    protected FluidTank output = new FluidTank(TANK_CAPACITY);
    protected ItemStackHandler inv = new BarrelHandler(STORAGE_SIZE, this);
    protected int ticks = 0;
    protected boolean poweredLastTick = false;
    protected BrewingRecipe bRecipe;
    protected SoakingRecipe sRecipe;
    /**
     * Sketchy shit so when someone cycles the barrel their gui updates correctly.  If we do it from cycleType above it fails.
     */
    Runnable obj;

    public TileBarrel() {
        input.setCanDrain(false);
        output.setCanFill(false);
    }

    @Override
    public void update() {
        if (world.isRemote) {
            return;
        }
        if (obj != null) {
            obj.run();
            obj = null;
        }
        boolean powered = world.isBlockPowered(pos);
        if (poweredLastTick != powered) {
            updateSeal(powered);
        }

        updateClientInputFluid(getInput());
        updateClientOutputFluid(getOutput());

        poweredLastTick = powered;
        BarrelState state = getState();
        if (state == BarrelState.STORAGE || !isSealed()) {
            ticks = 0;
            return;
        } else if (state == BarrelState.BREWING) {
            processBrewing();
        } else if (state == BarrelState.SOAKING) {
            processSoaking();
        }
    }

    public void updateClientInputFluid(FluidTank tank) {
        if (tank != null) {
            if (tank.getFluidAmount() >= 0) {
                if (tank.getFluidAmount() != prevInputAmount) {
                    prevInputAmount = tank.getFluidAmount();
                    world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 8);
                }
            }
        }
    }

    public void updateClientOutputFluid(FluidTank tank) {
        if (tank != null) {
            if (getOutput().getFluidAmount() >= 0) {
                if (getOutput().getFluidAmount() != prevInputAmount) {
                    prevInputAmount = getOutput().getFluidAmount();
                    world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 8);
                }
            }
        }
    }

    protected void processBrewing() {
        FluidStack fluid = input.getFluid();
        if (fluid != null && fluid.amount > 0) {
            if (bRecipe == null || !bRecipe.matches(this)) {
                bRecipe = BarrelRecipeManager.getBrewingRecipe(this);
            }
            if (bRecipe == null) {
                return;
            }
            if (ticks++ >= bRecipe.getTicks() && output.fillInternal(bRecipe.getOutput(), false) == bRecipe.getOutput().amount) {
                ticks = 0;
                input.drain(bRecipe.getInput().amount, true);
                for (Map.Entry<Ingredient, Integer> ingredient : bRecipe.getInputItems().entrySet()) {
                    for (int i = 0; i < inv.getSlots(); i++) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if (ingredient.getKey().apply(stack)) {
                            stack.shrink(ingredient.getValue());
                        }
                    }
                }
                input.drainInternal(bRecipe.getInput(), true);
                output.fillInternal(bRecipe.getOutput(), true);
            }
        }
    }

    protected void processSoaking() {
        FluidStack fluid = input.getFluid();
        if (fluid != null && fluid.amount > 0) {
            if (sRecipe == null || !sRecipe.matches(this)) {
                sRecipe = BarrelRecipeManager.getSoakingRecipe(this);
            }
            if (sRecipe == null) {
                return;
            }
            if (ticks++ >= sRecipe.getTicks()) {
                ItemStack curOutput = inv.getStackInSlot(1);
                if (!curOutput.isEmpty() && !ItemHandlerHelper.canItemStacksStack(curOutput, sRecipe.getOutput())) {
                    return;
                }
                if (!curOutput.isEmpty() && curOutput.getCount() + sRecipe.getOutput().getCount() > curOutput.getMaxStackSize()) {
                    return;
                }
                ticks = 0;
                if (HelperMath.tryPercentage(sRecipe.getFluidUseChance())) {
                    input.getFluid().amount -= sRecipe.getInput().amount;
                }
                inv.getStackInSlot(0).shrink(1);
                inv.insertItem(1, sRecipe.getOutput().copy(), false);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        input.readFromNBT(compound.getCompoundTag("inputTank"));
        output.readFromNBT(compound.getCompoundTag("outputTank"));
        inv.deserializeNBT(compound.getCompoundTag("items"));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", input.writeToNBT(new NBTTagCompound()));
        compound.setTag("outputTank", output.writeToNBT(new NBTTagCompound()));
        compound.setTag("items", inv.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (EnumFacing.DOWN == facing && getState() == BarrelState.BREWING && !isSealed()) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(output);
            }
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(input);
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    public BarrelState getState() {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.blockBarrel) {
            return state.getValue(BlockBarrel.BARREL_STATE);
        }
        return BarrelState.STORAGE;
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

    @Nullable
    public boolean isSealed() {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.blockBarrel) {
            return state.getValue(BlockBarrel.SEALED);
        }
        return false;
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    public void updateSeal(boolean seal) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.blockBarrel) {
            world.setBlockState(pos, state.withProperty(BlockBarrel.SEALED, seal));
        }
    }

    public FluidTank getInput() {
        return input;
    }

    public FluidTank getOutput() {
        return output;
    }

    public ItemStackHandler getInv() {
        return inv;
    }

    public Container getContainer(InventoryPlayer inv) {
        BarrelState state = getState();
        if (state == BarrelState.STORAGE) {
            return new ContainerBarrelStorage(inv, this);
        }
        if (state == BarrelState.BREWING) {
            return new ContainerBarrelBrewing(inv, this);
        }
        return new ContainerBarrelSoaking(inv, this);
    }

    public void cycleType(EntityPlayerMP cycler) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockBarrel.BARREL_STATE, getState().next()));
        oneTickLater(() -> cycler.openGui(Survivalism.INSTANCE, 1, world, pos.getX(), pos.getY(), pos.getZ()));
    }

    private void oneTickLater(Runnable r) {
        this.obj = r;
    }

}
