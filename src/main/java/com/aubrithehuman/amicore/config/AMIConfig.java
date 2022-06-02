package com.aubrithehuman.amicore.config;


import net.minecraftforge.common.ForgeConfigSpec;

public final class AMIConfig {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<Boolean> doMalum; 
	
	static {
		BUILDER.push("Config for AMICore");
		
		doMalum = BUILDER.comment("Toggle malum spirit additions. Default: true").define("Do Malum Additions", true);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	
}
