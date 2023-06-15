package com.aubrithehuman.amicore.crafting;

import java.util.List;
import java.util.Map;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.util.WeightedRandomList;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.sammy.malum.common.blocks.spiritaltar.SpiritAltarTileEntity;
import com.sammy.malum.core.systems.tileentities.SimpleInventoryTileEntity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.fish.CodEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StabilityEvents {
	
	private static WeightedRandomList<String> mobList;

	public static void randomEffect(World level, BlockPos pos, TileEntity core, double stability) {
		WeightedRandomList<String> effects = new WeightedRandomList<String>();
		
		if(stability <= 4.0 && stability >= 0.0) {
			effects.addEntry("spark", 1);
			effects.addEntry("noise", 4);
			effects.addEntry("lightning", 1);		
		} else if(stability <= 8.0 && stability > 4.0) {
			effects.addEntry("spark", 5);
			effects.addEntry("noise", 1);
			effects.addEntry("hallucinate", 1);
			effects.addEntry("lightning", 2);	
			effects.addEntry("mobs", 1);		
		} else if(stability <= 12.0 && stability > 8.0) {
			effects.addEntry("spark", 2);
			effects.addEntry("noise", 1);
			effects.addEntry("hallucinate", 1);
			effects.addEntry("lightning", 5);	
			effects.addEntry("mobs", 2);
			effects.addEntry("pedestalPop", 1);				
		} else if(stability <= 17.0 && stability > 12.0) {
			effects.addEntry("spark", 1);
			effects.addEntry("noise", 1);
			effects.addEntry("hallucinate", 1);
			effects.addEntry("lightning", 4);	
			effects.addEntry("mobs", 2);
			effects.addEntry("pedestalPop", 2);				
		} else if(stability > 17.0) {
			effects.addEntry("spark", 8);
			effects.addEntry("hallucinate", 1);
			effects.addEntry("lightning", 8);	
			effects.addEntry("mobs", 2);
			effects.addEntry("pedestalPop", 2);	
			effects.addEntry("explode", 1);				
		} else {
			effects.addEntry("spark", 1);			
		}
		
		String pull = effects.getRandom();
//		System.out.println("pulling " + pull);
		
		switch (pull) {
			case "spark": {
				spark(level, pos, core);
				return;
			}
			case "hallucinate": {
				hallucinate(level, pos, core);
				return;
			}
			case "noise": {
				noise(level, pos, core);
				return;
			}
			case "lightning": {
				lightning(level, pos, core, stability);
				return;
			}
			case "mobs": {
				mobs(level, pos, core);
				return;
			}
			case "pedestalPop": {
				pedestalPop(level, pos, core);
				return;
			}
			case "explode": {
				explode(level, pos, core);
				return;
			}
			default: {
				noise(level, pos, core);
				AMICore.LOGGER.warn("This noise was a default trigger! This should not happen! Please Report!");
				return;
			}
		}
		
	}
	
	public static void pedestalPop(World level, BlockPos pos, TileEntity core) {
//		System.out.println("pedestalPop!");
		if(core instanceof CraftingCoreTileEntity) {
			Map<BlockPos, ItemStack> pedestalsWithItems = ((CraftingCoreTileEntity) core).getPedestalsWithItems();
			if(pedestalsWithItems.size() > 0) {
				int rand = level.rand.nextInt(pedestalsWithItems.size());
				BlockPos pop = (BlockPos) pedestalsWithItems.keySet().toArray()[rand];
				TileEntity pedestal = level.getTileEntity(pop);
				if(pedestal instanceof PedestalTileEntity) {
					ItemEntity item = new ItemEntity(level, pop.getX() + 0.5, pop.getY() + 1.2, pop.getZ() + 0.5, ((PedestalTileEntity) pedestal).getInventory().getStackInSlot(0));
					item.setNoPickupDelay();
					level.addEntity(item);
					((PedestalTileEntity) pedestal).getInventory().setStackInSlot(0, ItemStack.EMPTY);
					level.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			
		} else if (core instanceof SpiritAltarTileEntity) {
			Map<BlockPos, ItemStack> pedestalsWithItems = ((CraftingCoreTileEntity) core).getPedestalsWithItems();
			int rand = level.rand.nextInt(pedestalsWithItems.size() - 1);
			BlockPos pop = (BlockPos) pedestalsWithItems.keySet().toArray()[rand];
			TileEntity pedestal = level.getTileEntity(pop);
			if (pedestal instanceof SimpleInventoryTileEntity)
	        {
	            for (ItemStack itemStack : ((SimpleInventoryTileEntity) pedestal).inventory.stacks())
	            {
	            	ItemEntity item = new ItemEntity(level, pos.getX()+0.5f, pos.getY()+1.2f, pos.getZ()+0.5f, itemStack);
	            	item.setNoPickupDelay();
					level.addEntity(item);
					level.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
	            }
			}
		}
	}
	
	public static void spark(World level, BlockPos pos, TileEntity core) {
//	    level.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.0D, 2.0D, 2.0D);
		((ServerWorld)level).spawnParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 15, 0, 0, 0, 0.1D);
		((ServerWorld)level).spawnParticle(ParticleTypes.FLASH, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 6, 0, 0, 0, 0.1D);
		((ServerWorld)level).spawnParticle(ParticleTypes.FIREWORK, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 50, 0, 0, 0, 0.1D);
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
//		System.out.println("spark!");
	}
	
	public static void noise(World level, BlockPos pos, TileEntity core) {
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
//		System.out.println("noise!");
	}
	
	public static void explode(World level, BlockPos pos, TileEntity core) {
//		System.out.println("explode!");
		level.createExplosion(null, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 5.0F * level.rand.nextFloat(), Explosion.Mode.NONE);
	}
	
	public static void mobs(World level, BlockPos pos, TileEntity core) {
//		System.out.println("mobs!");
		if(mobList == null) {
			mobList = new WeightedRandomList<String>();
			mobList.addEntry("zombie", 3);
			mobList.addEntry("skeleton", 3);
			mobList.addEntry("salmon", 2);
			mobList.addEntry("cod", 12);
			mobList.addEntry("pufferfish", 2);
			mobList.addEntry("spider", 1);
			mobList.addEntry("witch", 1);
			mobList.addEntry("vex", 6);
			mobList.addEntry("squid", 1);
			mobList.addEntry("enderman", 1);
			System.out.println("generated list!");
		}
		
		String pull = mobList.getRandom();

		switch (pull) {
			case "zombie": {
				spawnMob(new ZombieEntity(EntityType.ZOMBIE, level), pos, level);
				return;
			}
			case "skeleton": {
				SkeletonEntity skele = new SkeletonEntity(EntityType.SKELETON, level);
				skele.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW, 1));
				spawnMob(skele, pos, level);
				return;
			}
			case "salmon": {
				spawnMob(new SalmonEntity(EntityType.SALMON, level), pos, level);
				return;
			}
			case "cod": {
				spawnMob(new CodEntity(EntityType.COD, level), pos, level);
				return;
			}
			case "pufferfish": {
				spawnMob(new PufferfishEntity(EntityType.PUFFERFISH, level), pos, level);
				return;
			}
			case "spider": {
				spawnMob(new SpiderEntity(EntityType.SPIDER, level), pos, level);
				return;
			}
			case "witch": {
				spawnMob(new WitchEntity(EntityType.WITCH, level), pos, level);
				return;
			}
			case "vex": {
				VexEntity vex = new VexEntity(EntityType.VEX, level);
				vex.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD, 1));
				spawnMob(vex, pos, level);
				return;
			}
			case "squid": {
				spawnMob(new SquidEntity(EntityType.SQUID, level), pos, level);
				return;
			}
			case "enderman": {
				spawnMob(new EndermanEntity(EntityType.ENDERMAN, level), pos, level);
				return;
			}
		}
		 
	}
	
	private static void spawnMob(Entity spawn, BlockPos pos, World level) {
		int i = 0;
		BlockPos offsett = pos.add(0.5, 1.2, 0.5);
		while (i < 10) {
			BlockPos temp = pos.add(6 - level.rand.nextInt(13), 0, 6 - level.rand.nextInt(13));
			if(level.getBlockState(temp).getBlock().equals(Blocks.AIR)) {
				offsett = temp;
				i = 15;
			}
			i++;
		}
		spawn.setPosition(offsett.getX(),offsett.getY(),offsett.getZ());
		float face = 360 * level.rand.nextFloat();
		spawn.setRotationYawHead(face);
		level.addEntity(spawn);
	}
	
	public static void hallucinate(World level, BlockPos pos, TileEntity core) {
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 0.9f + level.rand.nextFloat() * 0.2f);
//		System.out.println("hallucinate!");
	}
	
	public static void lightning(World level, BlockPos pos, TileEntity core, double stability) {
//		System.out.println("lightning!");
		//x y z r
		List<Entity> nearestEntities = level.getLoadedEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos.add(-10, -5, -10), pos.add(10, 5, 10)));
		for (int i = 0; i < stability / 10; i++) {
			LightningBoltEntity strike = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, level);
			if(nearestEntities.size() > 0) {
				int rand = level.rand.nextInt(nearestEntities.size());
				strike.setPosition(nearestEntities.get(rand).getPosX(),nearestEntities.get(rand).getPosY(),nearestEntities.get(rand).getPosZ());
				nearestEntities.remove(rand);
			} else {
				strike.setPosition(pos.getX() + (10 - level.rand.nextInt(21)),pos.getY(),pos.getZ() + (10 - level.rand.nextInt(21)));
			}
			level.addEntity(strike);
		}
	}
	
	
	
}
