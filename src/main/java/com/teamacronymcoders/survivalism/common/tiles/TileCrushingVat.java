package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.inventory.IUpdatingInventory;
import com.teamacronymcoders.survivalism.common.inventory.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TileCrushingVat extends TileEntity implements IUpdatingInventory {

    private static List<RecipeVat> vatRecipes;
    private FluidTank tank;
    private ItemStackHandler inputItemHandler;
    private ItemStackHandler outputItemHandler;
    private RecipeVat recipeVat;
    private double jumpsTargeted = 0.0D;
    private double jumpsContained = 0.0D;

    public TileCrushingVat() {
        tank = new FluidTank(TileBarrel.TANK_CAPACITY);
        inputItemHandler = new UpdatingItemStackHandler(1, this) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
        };
        outputItemHandler = new UpdatingItemStackHandler(1, this) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
            }
        };
        vatRecipes = RecipeStorage.getVatRecipes();
    }

    public void makeProgress(EntityLivingBase entity) {
        Double multiplier = 0.0D;
        Map<Item, Double> map = RecipeStorage.getBootMultiplierMap();

        if (inputItemHandler.getStackInSlot(0).isEmpty()) {
            recipeVat = null;
            jumpsTargeted = 0.0D;
            jumpsContained = 0.0D;
            return;
        }

        if (recipeVat != null) {
            if (entity != null) {
                for (ItemStack boots : entity.getArmorInventoryList()) {
                    multiplier = map.getOrDefault(boots.getItem(), 1.0);
                }
            }
            jumpsContained += (SurvivalismConfigs.baseJumpValue * multiplier);
        } else {
            for (RecipeVat recipe : vatRecipes) {
                Ingredient inputIngredient = recipe.getInputIngredient();
                if (inputIngredient.apply(inputItemHandler.getStackInSlot(0))) {
                    jumpsTargeted = recipe.getJumps();
                    recipeVat = recipe;
                }
            }
        }

        if (jumpsContained >= jumpsTargeted) {
            if (recipeVat != null) {
                if (inputItemHandler.getStackInSlot(0) != ItemStack.EMPTY) {
                    inputItemHandler.getStackInSlot(0).shrink(1);
                }
                if (HelperMath.tryPercentage(recipeVat.getChanceOutput())) {
                    ItemStack stack = outputItemHandler.getStackInSlot(0);
                    ItemStack outputStack = recipeVat.getOutputStack();
                    if (stack.isEmpty()) {
                        if (stack.equals(outputStack)) {
                            stack.grow(outputStack.getCount());
                        }
                    } else {
                        outputItemHandler.setStackInSlot(0, recipeVat.getOutputStack());
                    }
                }
                tank.fill(recipeVat.getOutputFluid(), true);
                jumpsContained = 0.0D;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("tank")) {
            tank = tank.readFromNBT(compound.getCompoundTag("tank"));
        }
        if (compound.hasKey("inputItems")) {
            inputItemHandler.deserializeNBT(compound);
        }
        if (compound.hasKey("outputItems")) {
            outputItemHandler.deserializeNBT(compound);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        compound.setTag("inputItems", inputItemHandler.serializeNBT());
        compound.setTag("outputItems", outputItemHandler.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
                return (T) inputItemHandler;
            } else if (facing == EnumFacing.DOWN) {
                return (T) outputItemHandler;
            }
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return (T) tank;
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != EnumFacing.UP) {
                return true;
            }
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return true;
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public void updateSlot(int slot, ItemStack stack) {
        this.markDirty();
    }
}
