package com.aubrithehuman.amicore.crafting;

import com.aubrithehuman.amicore.AMICore;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;

public class StabilityObject {
	

	public static final Codec<StabilityObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
	  		// we can stuff up to 16 fields into the group method, using a codec for each field...
			BlockState.CODEC.fieldOf("block").forGetter(blockHolder -> blockHolder.blockstate),
	  		Codec.DOUBLE.fieldOf("factor").forGetter(blockHolder -> blockHolder.stability_factor)
	  	).apply(instance, (blockC, stability_factorC) -> new StabilityObject(blockC, stability_factorC))); // use the constructor to finish assembling the codec
	
		
	
	public final double stability_factor;
	public final BlockState blockstate;
	
	public StabilityObject(BlockState b, double f) {
		stability_factor = f;
		blockstate = b;
		AMICore.LOGGER.debug("Registered Stability Object [block:\"" + b.getBlock().toString() + "\", factor:" + f + "]");
	}
	
	public BlockState getBlock() {
		return blockstate;
	}
	
	public double getFactor() {
		return stability_factor;
	}
	
}