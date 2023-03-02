package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.aubrithehuman.amicore.crafting.IStabilityRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;

@Mixin(CombinationRecipe.class)
public abstract class MixinCombinationRecipe implements IStabilityRecipe {

	public double recipeStability = 1.0;
	
	@Override
	public double getInherantStability() {
		return recipeStability;
	}

	@Override
	public void setInherantStability(double stabilityIn) {
		this.recipeStability = stabilityIn;
	}
	
	@Override
	public double getAdjustedStability(double factor) {
		double x = (0.5 * (1 - this.recipeStability)); 
		return factor - (factor * x);
	}

	
	
}
