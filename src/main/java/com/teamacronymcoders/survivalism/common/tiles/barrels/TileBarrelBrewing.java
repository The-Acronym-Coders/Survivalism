package com.teamacronymcoders.survivalism.common.tiles.barrels;

import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelBrewing;
import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrelBrewing;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.RangedFluidWrapper;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.SurvivalismStorage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class TileBarrelBrewing extends TileBarrelBase implements ITickable, IHasGui, IUpdatingInventory {

    protected int ticks = 0;
    protected FluidTank input = new FluidTank(SurvivalismStorage.TANK_CAPACITY);
    protected FluidTank output = new FluidTank(SurvivalismStorage.TANK_CAPACITY);
    protected BrewingRecipe recipe;
    private RangedFluidWrapper wrapper = new RangedFluidWrapper(getInput(), getOutput());
    private ItemStackHandler inv = new UpdatingItemStackHandler(3, this);
    private boolean working = false;
    private FluidStack moarWater = FluidRegistry.getFluidStack("water", SurvivalismConfigs.rainFillRate);

    public TileBarrelBrewing() {
        input.setCanDrain(false);
        output.setCanFill(false);
    }

    @Override
    public void update() {
        super.update();
        if (this.isSealed()) {
            processBrewing();
            if (recipe != null) {
                working = true;
            }
        } else {
            ticks = 0;
            working = false;
            if (input.getFluid() == null || moarWater.isFluidEqual(input.getFluid())) {
                processRaining();
            }
        }
    }

    // NBT
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        input.readFromNBT(compound.getCompoundTag("inputTank"));
        output.readFromNBT(compound.getCompoundTag("outputTank"));
        inv.deserializeNBT(compound.getCompoundTag("items"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", input.writeToNBT(new NBTTagCompound()));
        compound.setTag("outputTank", output.writeToNBT(new NBTTagCompound()));
        compound.setTag("items", inv.serializeNBT());
        return super.writeToNBT(compound);
    }


    // Capabilities
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isSealed()) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(wrapper);
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


    // Process
    protected void processBrewing() {
        FluidStack fluid = input.getFluid();
        if (fluid != null && fluid.amount > 0) {
            if (recipe == null || !recipe.matches(this)) {
                recipe = BarrelRecipeManager.getBrewingRecipe(this);
            }
            if (recipe == null) {
                working = false;
                return;
            }
            if (ticks++ >= recipe.getTicks() && output.fillInternal(recipe.getOutput(), false) == recipe.getOutput().amount) {
                ticks = 0;
                for (Map.Entry<Ingredient, Integer> ingredient : recipe.getInputItems().entrySet()) {
                    for (int i = 0; i < inv.getSlots(); i++) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if (ingredient.getKey().apply(stack)) {
                            stack.shrink(ingredient.getValue());
                        }
                    }
                }
                input.drainInternal(recipe.getInput(), true);
                output.fillInternal(recipe.getOutput(), true);
            }
        }
    }

    protected void processRaining() {
        World world = getWorld();
        if (!isSealed()) {
            if (world.isRaining() && world.canBlockSeeSky(getPos())) {
                input.fillInternal(moarWater.copy(), true);
            }
        }
    }


    // Getters/Setters
    public FluidTank getInput() {
        return input;
    }

    public FluidTank getOutput() {
        return output;
    }

    public ItemStackHandler getInv() {
        return inv;
    }

    public boolean getWorking() {
        return working;
    }

    public BrewingRecipe getRecipe() {
        return recipe;
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }

    @Override
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return Optional.of(new GUIBarrelBrewing(this, getContainer(entityPlayer, world, blockPos))).orElse(null);
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return Optional.of(new ContainerBarrelBrewing(entityPlayer.inventory, this)).orElse(null);
    }

    public boolean onBlockActivated(EntityPlayer player) {
        GuiOpener.openTileEntityGui(Survivalism.INSTANCE, player, this.getWorld(), this.getPos());
        return true;
    }
}