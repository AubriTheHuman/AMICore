package com.aubrithehuman.amicore.particle;

import net.minecraft.world.World;

public interface IServerParticle {

	public void spawnServer(World world, double x, double y, double z);
	public void repeatServer(World world, double x, double y, double z, int i);
	
}
