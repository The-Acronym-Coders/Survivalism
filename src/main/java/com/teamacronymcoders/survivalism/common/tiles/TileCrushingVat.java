package com.teamacronymcoders.survivalism.common.tiles;

import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;
import com.teamacronymcoders.survivalism.utils.storages.StorageItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TileCrushingVat extends TileEntity {

    private static List<RecipeVat> vatRecipes;
    private FluidTank tank;
    private StorageItemHandler itemHandler;
    private RecipeVat recipeVat;
    private double jumpsTargetted;
    private double jumpsContained = 0.0D;

    public TileCrushingVat() {
        tank = new FluidTank(16000);
        itemHandler = new StorageItemHandler(3, 64);
        vatRecipes = RecipeStorage.getVatRecipes();
    }

    public void makeProgress(EntityLivingBase entity) {
        Double multiplier = 0.0D;
        Map<Item, Double> map = RecipeStorage.getBootMultiplierMap();

        if (recipeVat != null) {
            if (entity != null) {
                for (ItemStack boots : entity.getArmorInventoryList()) {
                    multiplier = map.getOrDefault(boots.getItem(), 1.0);
                }
            }
            jumpsContained = jumpsContained + multiplier;
        } else {
            for (RecipeVat recipe : vatRecipes) {
                ItemStack inputStack = recipe.getInputStack();
                if (itemHandler.getStackInSlot(0) == inputStack) {
                    jumpsTargetted = recipe.getJumps();
                    recipeVat = recipe;
                }
            }
        }

        if (jumpsContained >= jumpsTargetted) {
            if (recipeVat != null) {
                itemHandler.setStackInSlot(1, recipeVat.getOutputStack());
                tank.fill(recipeVat.getOutputFluid(), true);
            }
            itemHandler.getStackInSlot(0).shrink(1);
            jumpsContained = 0;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }
}
