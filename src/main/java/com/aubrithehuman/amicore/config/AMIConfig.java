package com.aubrithehuman.amicore.config;


import net.minecraftforge.common.ForgeConfigSpec;

public final class AMIConfig {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> tier_1_factor; 
	public static final ForgeConfigSpec.ConfigValue<Integer> tier_2_factor; 
	public static final ForgeConfigSpec.ConfigValue<Integer> tier_3_factor; 

	public static final ForgeConfigSpec.ConfigValue<Boolean> doMalum; 
	
	static {
		BUILDER.push("Config for AMICore");
		
		tier_1_factor = BUILDER.comment("Multiplication factor of tier 1 lubricants. Calculated as 1/val factor increase. Default: 4 (1.25x speed)").define("Tier 1 Factor", 4);
		tier_2_factor = BUILDER.comment("Multiplication factor of tier 2 lubricants. Calculated as 1/val factor increase. Default: 2 (1.5x speed)").define("Tier 2 Factor", 2);
		tier_3_factor = BUILDER.comment("Multiplication factor of tier 3 lubricants. Calculated as 1/val factor increase. Default: 1 (2x speed)").define("Tier 3 Factor", 1);
		
		doMalum = BUILDER.comment("Toggle malum spirit additions. Default: true").define("Do Malum Additions", true);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	
}
