package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.aubrithehuman.amicore.particle.IServerParticle;
import com.sammy.malum.core.systems.particles.ParticleManager;
import com.sammy.malum.core.systems.particles.data.MalumParticleData;

import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

@Mixin(ParticleManager.ParticleBuilder.class)
public class MixinParticleManager implements IServerParticle {
	
	@Shadow
	public ParticleType<?> type;
	
	@Shadow
	public MalumParticleData data;
	
	@Shadow
	public double vx = 0;

	@Shadow
	public double vy = 0;

	@Shadow
	public double vz = 0;
	
	@Shadow
	public double dx = 0;

	@Shadow
	public double dy = 0;

	@Shadow
	public double dz = 0;
	
	@Shadow
	public double maxXSpeed = 0;
	
	@Shadow
	public double maxYSpeed = 0;

	@Shadow
	public double maxZSpeed = 0;
	
	@Shadow
	public double maxXDist = 0;
	
	@Shadow
	public double maxYDist = 0;
	
	@Shadow
	public double maxZDist = 0;
	
	@Override
	public void spawnServer(World world, double x, double y, double z)
    {
        double yaw = world.rand.nextFloat() * Math.PI * 2, pitch = world.rand.nextFloat() * Math.PI - Math.PI / 2, xSpeed = world.rand.nextFloat() * maxXSpeed, ySpeed = world.rand.nextFloat() * maxYSpeed, zSpeed = world.rand.nextFloat() * maxZSpeed;
        this.vx += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.vy += Math.sin(pitch) * ySpeed;
        this.vz += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        double yaw2 = world.rand.nextFloat() * Math.PI * 2, pitch2 = world.rand.nextFloat() * Math.PI - Math.PI / 2, xDist = world.rand.nextFloat() * maxXDist, yDist = world.rand.nextFloat() * maxYDist, zDist = world.rand.nextFloat() * maxZDist;
        this.dx = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        this.dy = Math.sin(pitch2) * yDist;
        this.dz = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
    
//        System.out.println("Attempt!");
        ((ServerWorld) world).spawnParticle(data, x + dx, y + dy, z + dz, 0, vx, vy, vz, 0.18F);
    }
	
	@Override	
	public void repeatServer(World world, double x, double y, double z, int n)
    {
        for (int i = 0; i < n; i++) spawnServer(world, x, y, z);
    }

}
