package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.defaults.TileBase;
import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import com.teamacronymcoders.survivalism.utils.network.MessageSetState;
import com.teamacronymcoders.survivalism.utils.network.MessageUpdateBarrel;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileBarrel extends TileBase implements ITickable, IUpdatingInventory {

    public static final int TANK_CAPACITY = 16000;
    public static final int STORAGE_SIZE = 9;

    private List<RecipeBarrel> barrelRecipes;
    private FluidTank inputTank;
    private FluidTank outputTank;
    private ItemStackHandler itemHandler;

    int val2;
    private int state = 0;
    private int durationTicks;
    private int currentTicks = 0;

    private int prevAmountI;
    private int prevAmountO;

    private boolean sealed = false;

    public TileBarrel() {
        inputTank = new FluidTank(TANK_CAPACITY) {
            @Override
            public boolean canFill() {
                return true;
            }

            @Override
            public boolean canDrain() {
                return false;
            }
        };
        outputTank = new FluidTank(TANK_CAPACITY) {
            @Override
            public boolean canFill() {
                return false;
            }

            @Override
            public boolean canDrain() {
                return true;
            }
        };
        itemHandler = new UpdatingItemStackHandler(STORAGE_SIZE, this) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
        };
        barrelRecipes = RecipeStorage.getBarrelRecipes();
    }

    ///////////
    // Stuff //
    ///////////

    @Override
    public void update() {
        boolean sendUpdate = false;

        if (this.world.getBlockState(this.getPos()).getBlock() instanceof BlockBarrel) {
            if (state != this.getWorld().getBlockState(this.getPos()).getValue(BlockBarrel.BARREL_STATE).ordinal()) {
                state = this.getWorld().getBlockState(this.getPos()).getValue(BlockBarrel.BARREL_STATE).ordinal();
            }

            if (sealed != this.getWorld().getBlockState(this.getPos()).getValue(BlockBarrel.SEALED_STATE)) {
                sealed = this.getWorld().getBlockState(this.getPos()).getValue(BlockBarrel.SEALED_STATE);
            }
        }

        if (checkBarrelState(StorageEnumsBarrelStates.BREWING) && this.world.getBlockState(this.getPos()).getValue(BlockBarrel.SEALED_STATE)) {
            FluidTank inputTank = getInputTank();
            if (inputTank != null) {
                FluidStack inputTankFluid = getInputTank().getFluid();
                if (inputTankFluid != null) {
                    for (RecipeBarrel recipe : barrelRecipes) {
                        if (recipe instanceof BrewingRecipe) {
                            FluidStack recipeInputFS = ((BrewingRecipe) recipe).getInputFluid();
                            if (inputTankFluid.getFluid().equals(recipeInputFS.getFluid())) {
                                List<ItemStack> itemStacks = new ArrayList<>();

                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                    itemStacks.add(itemHandler.getStackInSlot(i));
                                }

                                boolean check1 = false;
                                boolean check2 = false;
                                boolean check3 = false;

                                List<Ingredient> ingredients = ((BrewingRecipe) recipe).getInputIngredients();

                                if (!(ingredients.get(0) == null && ingredients.get(1) == null && ingredients.get(2) == null)) {
                                    for (ItemStack stack : itemStacks) {
                                        if (ingredients.get(0) == null) {
                                            check1 = true;
                                        } else if (ingredients.get(0).apply(stack)) {
                                            check1 = true;
                                        }

                                        if (ingredients.get(1) == null) {
                                            check2 = true;
                                        } else if (ingredients.get(1).apply(stack)) {
                                            check2 = true;
                                        }

                                        if (ingredients.get(2) == null) {
                                            check3 = true;
                                        } else if (ingredients.get(2).apply(stack)) {
                                            check3 = true;
                                        }
                                    }
                                }

                                if (check1 && check2 && check3) {
                                    durationTicks = ((BrewingRecipe) recipe).getTicks();
                                    currentTicks += 1;
                                    if (currentTicks >= durationTicks) {
                                        inputTank.drain(recipeInputFS, true);
                                        itemHandler.getStackInSlot(0).shrink(1);
                                        itemHandler.getStackInSlot(1).shrink(1);
                                        itemHandler.getStackInSlot(2).shrink(1);
                                        getOutputTank().fill(((BrewingRecipe) recipe).getOutputFluid(), true);
                                        durationTicks = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (checkBarrelState(StorageEnumsBarrelStates.SOAKING) && this.world.getBlockState(this.getPos()).getValue(BlockBarrel.SEALED_STATE)) {
            FluidTank inputTank = getInputTank();
            if (inputTank != null) {
                FluidStack inputTankStack = inputTank.getFluid();
                if (inputTankStack != null) {
                    for (RecipeBarrel recipe : RecipeStorage.getBarrelRecipes()) {
                        if (recipe instanceof SoakingRecipe) {
                            FluidStack recipeInputFS = ((SoakingRecipe) recipe).getInputFluid();
                            if (inputTankStack.getFluid().equals(recipeInputFS.getFluid())) {
                                Ingredient recipeInputIS = ((SoakingRecipe) recipe).getInputIngredient();
                                if (recipeInputIS.apply(itemHandler.getStackInSlot(0))) {
                                    if (((SoakingRecipe) recipe).getDecreaseAmount() != 0 && (((SoakingRecipe) recipe).getDecreaseChance() == 0.0f || ((SoakingRecipe) recipe).getDecreaseChance() == 1.0f)) {
                                        int val = ((SoakingRecipe) recipe).getDecreaseAmount() / durationTicks;
                                        inputTank.drain(val, true);
                                        val2 += val;
                                    }
                                    durationTicks = ((SoakingRecipe) recipe).getTicks();
                                    currentTicks += 1;
                                    if (currentTicks >= durationTicks) {
                                        itemHandler.getStackInSlot(0).shrink(1);
                                        if (((SoakingRecipe) recipe).getDecreaseAmount() != 0) {
                                            int decreaseAmount = ((SoakingRecipe) recipe).getDecreaseAmount();
                                            if (((SoakingRecipe) recipe).getDecreaseChance() == 0.0f || ((SoakingRecipe) recipe).getDecreaseChance() == 1.0f) {
                                                if (val2 != decreaseAmount) {
                                                    int dif = decreaseAmount - val2;
                                                    inputTank.drain(dif, true);
                                                }
                                            }
                                            if (((SoakingRecipe) recipe).getDecreaseChance() != 0.0f && ((SoakingRecipe) recipe).getDecreaseChance() != 1.0f) {
                                                float decreaseChance = ((SoakingRecipe) recipe).getDecreaseChance();
                                                if (HelperMath.tryPercentage(decreaseChance)) {
                                                    inputTank.drain(decreaseAmount, true);
                                                }
                                            }
                                        }
                                        if (itemHandler.getStackInSlot(1).isEmpty()) {
                                            itemHandler.setStackInSlot(1, ((SoakingRecipe) recipe).getOutputItemStack());
                                        } else {
                                            ItemStack stack = itemHandler.getStackInSlot(1);
                                            stack.grow(((SoakingRecipe) recipe).getOutputItemStack().getCount());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!this.world.isRemote) {
            if (prevAmountI != (getInputTank() != null ? getInputTank().getFluidAmount() : 0) || prevAmountO != (getOutputTank() != null ? getOutputTank().getFluidAmount() : 0)) {
                prevAmountI = getInputTank().getFluidAmount();
                prevAmountO = getOutputTank().getFluidAmount();
                sendUpdate = true;
            }
        }

        if (sendUpdate) {
            this.markDirty();
            this.sealed = getWorld().getBlockState(this.getPos()).getValue(BlockBarrel.SEALED_STATE);
            this.state = getWorld().getBlockState(this.getPos()).getValue(BlockBarrel.BARREL_STATE).ordinal();
            this.prevAmountI = getInputTank().getFluidAmount();
            this.prevAmountO = getOutputTank().getFluidAmount();
            getWorld().addBlockEvent(getPos(), this.getBlockType(), 1, this.state);
            Survivalism.INSTANCE.getPacketHandler().sendToAllAround(new MessageUpdateBarrel(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
            this.world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("inputTank")) {
            inputTank = inputTank.readFromNBT(compound.getCompoundTag("inputTank"));
        }
        if (compound.hasKey("outputTank")) {
            outputTank = outputTank.readFromNBT(compound.getCompoundTag("outputTank"));
        }
        if (compound.hasKey("items")) {
            itemHandler.deserializeNBT(compound);
        }

        if (compound.hasKey("sealed")) {
            compound.getBoolean("sealed");
        }

        if (compound.hasKey("barrel_state")) {
            compound.getInteger("barrel_state");
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", inputTank.writeToNBT(new NBTTagCompound()));
        compound.setTag("outputTank", outputTank.writeToNBT(new NBTTagCompound()));
        compound.setTag("items", itemHandler.serializeNBT());
        compound.setBoolean("sealed", sealed);
        compound.setInteger("barrel_state", state);
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (EnumFacing.DOWN == facing) {
                if (checkBarrelState(StorageEnumsBarrelStates.BREWING)) {
                    return (T) outputTank;
                }
            } else {
                return (T) inputTank;
            }
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    //////////////////////
    // Utility Methods //
    ////////////////////

    /**
     * Stolen from HeatedTank
     * Sorry Skysom ;(
     */
    private void ensureBarrelStateIs(StorageEnumsBarrelStates expectedBarrelState) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        StorageEnumsBarrelStates currentBarrelState = currentState.getValue(BlockBarrel.BARREL_STATE);
        if (currentBarrelState != expectedBarrelState) {
            IBlockState state = currentState.withProperty(BlockBarrel.BARREL_STATE, expectedBarrelState);
            world.setBlockState(this.getPos(), state);
            if (world.isRemote) {
                Survivalism.INSTANCE.getPacketHandler().sendToServer(new MessageSetState(this.getPos(), state));
            }
        }
    }

    public boolean checkBarrelState(StorageEnumsBarrelStates expectedBarrelState) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        StorageEnumsBarrelStates currentBarrelState = currentState.getValue(BlockBarrel.BARREL_STATE);
        return currentBarrelState == expectedBarrelState;
    }

    public void cycleBarrelStates(IBlockState state) {
        StorageEnumsBarrelStates currentState = state.getValue(BlockBarrel.BARREL_STATE);
        currentState = currentState.cycle();
        ensureBarrelStateIs(currentState);
    }

    private void ensureSealedStateIs(boolean sealed) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        Boolean currentSealedState = currentState.getValue(BlockBarrel.SEALED_STATE);
        if (currentSealedState != sealed) {
            IBlockState newState = currentState.withProperty(BlockBarrel.SEALED_STATE, sealed);
            world.setBlockState(pos, newState);
            if (world.isRemote) {
                Survivalism.INSTANCE.getPacketHandler().sendToServer(new MessageSetState(this.getPos(), newState));
            }
        }
    }

    public boolean checkSealedState(boolean sealed) {
        IBlockState currentState = this.getWorld().getBlockState(this.getPos());
        Boolean currentSealed = currentState.getValue(BlockBarrel.SEALED_STATE);
        return currentSealed == sealed;
    }

    public void cycleSealedStates(IBlockState state) {
        Boolean currentSealed = state.getValue(BlockBarrel.SEALED_STATE);
        if (currentSealed) {
            ensureSealedStateIs(false);
        } else {
            ensureSealedStateIs(true);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }


    //////////////////////
    // Setters Getters //
    ////////////////////

    public void setState(int state) {
        this.state = state;
    }

    public void setSealed(boolean sealed) {
        this.sealed = sealed;
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public void setInputTank(FluidTank inputTank) {
        this.inputTank = inputTank;
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public void setOutputTank(FluidTank outputTank) {
        this.outputTank = outputTank;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }
}
