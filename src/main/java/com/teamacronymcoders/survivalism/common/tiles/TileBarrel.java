package com.teamacronymcoders.survivalism.common.tiles;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelBrewing;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelSoaking;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelStorage;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.defaults.TileBase;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import com.teamacronymcoders.survivalism.utils.storages.BarrelState;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
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

public class TileBarrel extends TileBase implements ITickable, IUpdatingInventory {

	public static final int TANK_CAPACITY = 16000;
	public static final int STORAGE_SIZE = 9;

	protected FluidTank input = new FluidTank(TANK_CAPACITY);
	protected FluidTank output = new FluidTank(TANK_CAPACITY);
	protected ItemStackHandler inv;
	protected int ticks = 0;

	public TileBarrel() {
		input.setCanDrain(false);
		output.setCanFill(false);
		inv = new UpdatingItemStackHandler(getState().getInvSize(), this);
	}

	@Override
	public void update() {
		updateSeal(world.isBlockPowered(pos));
		BarrelState state = getState();
		if (state == BarrelState.STORAGE || !isSealed()) return;
		else if (state == BarrelState.BREWING) processBrewing();
		else if (state == BarrelState.SOAKING) processSoaking();
	}

	protected BrewingRecipe bRecipe;

	protected void processBrewing() {
		FluidStack fluid = input.getFluid();
		if (fluid != null && fluid.amount > 0) {
			if (bRecipe == null || !bRecipe.matches(this)) bRecipe = BarrelRecipeManager.getBrewingRecipe(this);
			if (bRecipe == null) return;
			if (ticks++ >= bRecipe.getTicks() && output.fill(bRecipe.getOutput(), false) == bRecipe.getOutput().amount) {
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
				output.fill(bRecipe.getOutput(), true);
			}
		}
	}

	protected SoakingRecipe sRecipe;

	protected void processSoaking() {
		FluidStack fluid = input.getFluid();
		if (fluid != null && fluid.amount > 0) {
			if (sRecipe == null || !sRecipe.matches(this)) sRecipe = BarrelRecipeManager.getSoakingRecipe(this);
			if (sRecipe == null) return;
			if (ticks++ >= sRecipe.getTicks()) {
				ItemStack curOutput = inv.getStackInSlot(1);
				if (!curOutput.isEmpty() && !ItemHandlerHelper.canItemStacksStack(curOutput, sRecipe.getOutput())) return;
				if (!curOutput.isEmpty() && curOutput.getCount() + sRecipe.getOutput().getCount() > curOutput.getMaxStackSize()) return;
				ticks = 0;
				if (HelperMath.tryPercentage(sRecipe.getFluidUseChance())) input.drain(sRecipe.getInput().amount, true);
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
			if (EnumFacing.DOWN == facing && getState() == BarrelState.BREWING) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(output);
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(input);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	public BarrelState getState() {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlocks.blockBarrel) return state.getValue(BlockBarrel.BARREL_STATE);
		return null;
	}

	@Nullable
	public boolean isSealed() {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlocks.blockBarrel) return state.getValue(BlockBarrel.SEALED);
		return false;
	}

	@Override
	public void updateSlot(int slot, ItemStack stack) {
		this.markDirty();
	}

	private void updateSeal(boolean powered) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlocks.blockBarrel) {
			world.setBlockState(pos, state.withProperty(BlockBarrel.SEALED, powered));
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
		if (state == BarrelState.STORAGE) return new ContainerBarrelStorage(inv, this);
		if (state == BarrelState.BREWING) return new ContainerBarrelBrewing(inv, this);
		return new ContainerBarrelSoaking(inv, this);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (oldState.getBlock() == ModBlocks.blockBarrel && newState.getBlock() == ModBlocks.blockBarrel) return oldState.getValue(BlockBarrel.BARREL_STATE) != newState.getValue(BlockBarrel.BARREL_STATE);
		return super.shouldRefresh(world, pos, oldState, newState);
	}

}
