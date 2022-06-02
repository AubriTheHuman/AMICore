package com.aubrithehuman.fluids;

import flaxbeard.immersivepetroleum.api.crafting.LubricantHandler;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag.INamedTag;

public class FluidTagsAMI {

	public static final INamedTag<Fluid> LUBE_TIER_1 = FluidTags.bind("lubetier1");
	public static final INamedTag<Fluid> LUBE_TIER_2 = FluidTags.bind("lubetier2");
	public static final INamedTag<Fluid> LUBE_TIER_3 = FluidTags.bind("lubetier3");
	
	
	public static void init() {
		LubricantHandler.register(LUBE_TIER_1, 1);
//		LubricantHandler.register(LUBE_TIER_2, 1);
		LubricantHandler.register(LUBE_TIER_3, 1);
	}
}
