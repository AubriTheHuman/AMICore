package com.aubrithehuman.amicore.config;


import net.minecraftforge.common.ForgeConfigSpec;

public final class AMIConfig {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<Boolean> doMalum; 
	public static final ForgeConfigSpec.ConfigValue<Boolean> doMalumAltarFix; 
	public static final ForgeConfigSpec.ConfigValue<Boolean> doCreativeTabs; 
	public static final ForgeConfigSpec.ConfigValue<Boolean> doMalumStability; 
	public static final ForgeConfigSpec.ConfigValue<Boolean> doComboCraftTweaks; 
	public static final ForgeConfigSpec.ConfigValue<Boolean> doCOmboCraftStability; 
	public static final ForgeConfigSpec.ConfigValue<Double> minimumStability; 
//	public static final ForgeConfigSpec.ConfigValue<Integer> perObjectMax; 
	
	static {
		BUILDER.push("Config for AMICore");
		 
		doMalum = BUILDER.comment("Toggle malum spirit additions. Default: true").define("Do Malum Additions", true);
		doMalumAltarFix = BUILDER.comment("Toggle malum spirit altar fixes. Patches out duplication issue, and empty inv crash. Default: true").define("Do Spirit Altar Patches", true);
		doCreativeTabs = BUILDER.comment("Toggle amicore creative tabs. Default: true").define("Do Creative Tabs", true);
		doMalumStability = BUILDER.comment("Toggle stability mechanic for malum spirit altar. Default: true").define("Do Spirit Altar Stability", true);
		doComboCraftTweaks = BUILDER.comment("Toggle stack size increase for extended crafting combination crafting recipes. Default: true").define("Do Combo Crafting Tweaks", true);
		doCOmboCraftStability = BUILDER.comment("Toggle stability mechanic for combiantion crafting recipes. Default: true").define("Do Combo Stability", true);
		minimumStability = BUILDER.comment("Minimum stability for combiantion crafting recipes. Default: 15.0").define("Minimum Stability", 15.0);
//		perObjectMax = BUILDER.comment("Max number of objectst of a type that can effect stability. Default: 24").define("Max object of type", 24);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	
}
