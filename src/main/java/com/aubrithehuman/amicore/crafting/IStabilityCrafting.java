package com.aubrithehuman.amicore.crafting;

public interface IStabilityCrafting {
	
	public double getCrafterStabilityFactor();
	public double getBaseStability();
	public double getSearchRangeXZ();
	public double getSearchRangeY();
	
	public double getLastStability();
	public void setLastStability(double in);
	public boolean doStabilityUpdate(int progress);
}
