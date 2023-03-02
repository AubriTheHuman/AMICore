package com.aubrithehuman.amicore.crafting;

import java.util.HashMap;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.math.BlockPos;

public interface IStabilityCrafting {
	
	public double getCrafterStabilityFactor();
	public double getBaseStability();
	public double getSearchRangeXZ();
	public double getSearchRangeY();
	
	public double getLastStability();
	public void setLastStability(double in);
	public boolean doStabilityUpdate(int progress);
	

	public HashMap<String, Pair<List<BlockPos>, Integer>> getLastStabilityObjects();
	public void setLastStabilityObjects(HashMap<String, Pair<List<BlockPos>, Integer>> in);
}
