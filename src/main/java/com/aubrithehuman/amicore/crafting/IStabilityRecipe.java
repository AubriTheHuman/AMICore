package com.aubrithehuman.amicore.crafting;

public interface IStabilityRecipe {
	
	public double getInherantStability();

	public void setInherantStability(double stabilityIn);
	
	public double getAdjustedStability(double factor);

}
