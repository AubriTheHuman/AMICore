package com.aubrithehuman.amicore.crafting;

import com.aubrithehuman.amicore.AMICore;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;

public class StabilityObject {
	

	public static final Codec<StabilityObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
	  		// we can stuff up to 16 fields into the group method, using a codec for each field...
			Codec.STRING.fieldOf("block").forGetter(blockHolder -> blockHolder.block),
	  		Codec.DOUBLE.fieldOf("factor").forGetter(blockHolder -> blockHolder.stability_factor),
	  		Codec.INT.fieldOf("maximum").forGetter(blockHolder -> blockHolder.maximum)
	  	).apply(instance, (blockC, stability_factorC, maximumC) -> new StabilityObject(blockC, stability_factorC, maximumC))); // use the constructor to finish assembling the codec
	
		
	
	public final double stability_factor;
	public final int maximum;
	public final String block;
	
	public StabilityObject(String b, double f, int max) {
		stability_factor = f;
		block = b;
		maximum = max;
		AMICore.LOGGER.debug("Registered Stability Object [block:\"" + b + "\", factor:" + f + ", maximum: " + max + "]");
	}
	
	public String getBlock() {
		return block;
	}
	
	public double getFactor() {
		return stability_factor;
	}
	
	public int getMaximum() {
		return maximum;
	}
	
}