package com.aubrithehuman.amicore.crafting;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StabilityEvents {

	public static void randomEffect(World level, BlockPos pos, TileEntity core, double stability) {
		
	}
	
	public static void pedestalPop(World level, BlockPos pos, TileEntity core) {
		System.out.println("pedestalPop!");
	}
	
	public static void spark(World level, BlockPos pos, TileEntity core) {
		System.out.println("spark!");
	}
	
	public static void noise(World level, BlockPos pos, TileEntity core) {
		System.out.println("noise!");
	}
	
	public static void explode(World level, BlockPos pos, TileEntity core) {
		System.out.println("explode!");
	}
	
	public static void mobs(World level, BlockPos pos, TileEntity core) {
		System.out.println("mobs!");
	}
	
	public static void hallucinate(World level, BlockPos pos, TileEntity core) {
		System.out.println("hallucinate!");
	}
	
	public static void lightning(World level, BlockPos pos, TileEntity core) {
		System.out.println("lightning!");
	}
	
}
