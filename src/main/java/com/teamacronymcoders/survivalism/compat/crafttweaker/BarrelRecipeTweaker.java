package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;

import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Credit goes out to CritFlaw
 * Please and kindly go fluff yourself!
 */
@ZenClass("mods.survivalism.Barrel")
@ZenRegister
public class BarrelRecipeTweaker {

	@ZenMethod
	public static void addSoakingRecipe(ILiquidStack input, IIngredient inputItem, IItemStack output, int ticks, @Optional float fluidUseChance) {
		Survivalism.LATE_ADDITIONS.add(new AddSoakingRecipe(input, inputItem, output, fluidUseChance, ticks));
	}

	/*	
		@ZenMethod
		public static void addBrewingRecipe(ILiquidStack inputFluid, IIngredient[] inputIngredients, ILiquidStack outputFluid, int ticks) {
			Survivalism.LATE_ADDITIONS.add(new addBrewingRecipe(inputFluid, inputIngredients, outputFluid, ticks));
		}
		
		
		private static class addBrewingRecipe implements IAction {
			FluidStack inputFluid;
			List<String> names = new ArrayList<>();
			Map<Ingredient, Integer> inputIngredientsMap = new HashMap<>(3);
			FluidStack outputFluid;
			int ticks;
	
			addBrewingRecipe(ILiquidStack inputFluid, IIngredient[] inputIngredients, ILiquidStack outputFluid, int ticks) {
				this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
				for (IIngredient ingredient : inputIngredients) {
					this.names.add(ingredient.toCommandString());
				}
				for (IIngredient ingredient : inputIngredients) {
					if (ingredient instanceof IItemStack) {
						ItemStack stack = CraftTweakerMC.getItemStack((IItemStack) ingredient);
						if (stack.hasTagCompound()) {
							IngredientNBT nbt = (IngredientNBT) IngredientNBT.fromStacks(CraftTweakerMC.getItemStack(ingredient));
							this.inputIngredientsMap.put(nbt, ingredient.getAmount());
						}
					} else {
						this.inputIngredientsMap.put(CraftTweakerMC.getIngredient(ingredient), ingredient.getAmount());
					}
				}
				this.outputFluid = CraftTweakerMC.getLiquidStack(outputFluid);
				this.ticks = ticks;
			}
	
			@Override
			public void apply() {
				BrewingRecipe recipe = new BrewingRecipe();
				if (inputFluid.amount <= 0 || inputFluid.amount >= TileBarrel.TANK_CAPACITY) {
					CraftTweakerAPI.logError("Input Fluid: " + inputFluid.getLocalizedName() + " is either null or has an amount less than or equal to 0mb!");
				} else {
					recipe.setInputFluid(inputFluid);
				}
	
				if (inputIngredientsMap.size() <= 0 || inputIngredientsMap.size() > 3) {
					CraftTweakerAPI.logError("Ingredient list for Brewing Recipe: " + inputFluid.getLocalizedName() + " can't be less than or equal to 0 and not greater than 3 ItemStacks big!");
				} else {
					recipe.setInputIngredientsMap(inputIngredientsMap);
				}
	
				if (outputFluid.amount <= 0 || outputFluid.amount >= TileBarrel.TANK_CAPACITY) {
					CraftTweakerAPI.logError("Output Fluid: " + outputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
				} else {
					recipe.setOutputFluid(outputFluid);
				}
	
				if (ticks <= 0) {
					CraftTweakerAPI.logError("Processing Ticks can't be less than or equal to 0");
				} else {
					recipe.setTicks(ticks);
				}
	
				RecipeStorage.getBarrelRecipes().add(recipe);
			}
	
			@Override
			public String describe() {
				StringBuilder sb = new StringBuilder();
				if (SurvivalismConfigs.crtVerboseLogging) {
					sb.append("Added Brewing Recipe: ").append("\n");
					sb.append("Input Fluidstack: ").append(inputFluid.getLocalizedName()).append(":").append(inputFluid.amount).append("\n");
					sb.append("Input Ingredients: ").append("\n");
					for (String name : names) {
						sb.append(name).append("\n");
					}
					sb.append("Output Fluid: ").append(outputFluid.getLocalizedName()).append(":").append(outputFluid.amount).append("\n");
					sb.append("Ticks: ").append(ticks).append("\n");
				} else {
					sb.append("Added Brewing Recipe: ").append(" ");
					sb.append(inputFluid.getLocalizedName()).append(" ");
					for (String name : names) {
						sb.append(name).append(" : ");
					}
					sb.append(outputFluid.getLocalizedName()).append(" ");
	
				}
	
				return sb.toString();
			}
		}
	*/
	private static class AddSoakingRecipe implements IAction {
		String itemDesc;
		String outDesc;
		FluidStack input;
		Ingredient inputItem;
		ItemStack output;
		float fluidUseChance;
		int ticks;

		AddSoakingRecipe(ILiquidStack input, IIngredient inputItem, IItemStack output, float fluidUseChance, int ticks) {
			this.itemDesc = inputItem.toCommandString();
			this.outDesc = output.toCommandString();
			this.input = CraftTweakerMC.getLiquidStack(input);
			this.inputItem = CraftTweakerMC.getIngredient(inputItem);
			this.output = CraftTweakerMC.getItemStack(output);
			this.ticks = ticks;
			this.fluidUseChance = fluidUseChance;
		}

		@Override
		public void apply() {
			BarrelRecipeManager.register(new SoakingRecipe(new ResourceLocation(CraftTweaker.MODID, itemDesc), input, inputItem, output, fluidUseChance, ticks));
		}

		@Override
		public String describe() {
			StringBuilder sb = new StringBuilder();
			if (SurvivalismConfigs.crtVerboseLogging) {
				sb.append("Added Soaking Recipe: ").append("\n");
				sb.append("Input Fluid: ").append(input.getLocalizedName()).append(":").append(input.amount).append("\n");
				sb.append("Input Ingredient: ").append(itemDesc).append("\n");
				sb.append("Output ItemStack: ").append(outDesc).append("\n");
				sb.append("Fluid Use Chance: ").append(fluidUseChance).append("\n");
				sb.append("Ticks: ").append(ticks).append("\n");
			} else {
				sb.append("Added Soaking Recipe for: ").append(" ");
				sb.append(input.getLocalizedName()).append(" : ").append(itemDesc).append(" : ").append(outDesc);
			}
			return sb.toString();
		}
	}
}
