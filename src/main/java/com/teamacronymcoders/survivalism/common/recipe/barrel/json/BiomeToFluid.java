package com.teamacronymcoders.survivalism.common.recipe.barrel.json;

import com.google.common.base.Preconditions;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeToFluid implements IJsonObject {
    public String biomes;
    public String fluid;
    public Integer amount;

    public BiomeToFluid(String biome, String fluid, Integer amount) {
        this.biomes = biome;
        this.fluid = fluid;
        this.amount = amount;
    }

    @Override
    public void register() {
        try {
            Preconditions.checkNotNull(biomes);
            Biome biomez = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomes));
            Preconditions.checkNotNull(biomez);

            Preconditions.checkNotNull(fluid);
            Preconditions.checkNotNull(amount);
            FluidStack stackz = FluidRegistry.getFluidStack(fluid, amount);
            Preconditions.checkNotNull(stackz);

            BarrelRecipeManager.register(biomez, stackz);
        } catch (IllegalArgumentException e) {
            Survivalism.INSTANCE.getLogger().error("Skipping Biome, Fluid pairing with Biome Name: " + biomes + " and Fluid Name: " + fluid);
        }
    }
}
