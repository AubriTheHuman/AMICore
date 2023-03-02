package com.aubrithehuman.amicore.mixin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.config.AMIConfig;
import com.aubrithehuman.amicore.crafting.IStabilityCrafting;
import com.aubrithehuman.amicore.crafting.IStabilityRecipe;
import com.aubrithehuman.amicore.crafting.StabilityEvents;
import com.aubrithehuman.amicore.crafting.StabilityObject;
import com.aubrithehuman.amicore.particle.IServerParticle;
import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.mojang.datafixers.util.Pair;
import com.sammy.malum.common.items.SpiritItem;
import com.sammy.malum.core.init.MalumSounds;
import com.sammy.malum.core.init.particles.MalumParticles;
import com.sammy.malum.core.systems.particles.ParticleManager;
import com.sammy.malum.core.systems.particles.ParticleManager.ParticleBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(CraftingCoreTileEntity.class)
public abstract class MixinCraftingCoreTileEntity extends BaseInventoryTileEntity implements IStabilityCrafting {

	public MixinCraftingCoreTileEntity(TileEntityType<?> type) {
		super(type);
		inventory = null;
		energy = null;
		recipeInventory = null;
	}

	@Shadow
	public final BaseItemStackHandler inventory;
	
	@Shadow
	public final BaseEnergyStorage energy;
	
	@Shadow
	public final BaseItemStackHandler recipeInventory;
	
	@Shadow
	public CombinationRecipe recipe;
	
	@Shadow
	public int progress;
	
	@Shadow
	public int oldEnergy;
	
	@Shadow
	public int pedestalCount;
	
	@Shadow
	public boolean haveItemsChanged;
	
	
	//Mixin Check
	private double lastStability;
	private int stabilityCooldown = 60;
	public int soundCooldown = 0;
	public HashMap<String, Pair<List<BlockPos>, Integer>> stabilityObjects;
	
	@Inject(method = "<init>*", at = @At("RETURN"))
    public void constructorTail(CallbackInfo ci) {
//        System.out.println("Override Inventory Size on craftingCore");
        this.inventory.setDefaultSlotLimit(64);
		this.recipeInventory.setDefaultSlotLimit(64);
    }
	
	@Shadow
	public abstract CombinationRecipe getActiveRecipe();
	
	@Shadow
	public abstract boolean process(CombinationRecipe recipe);
	

	@Shadow
	public abstract Map<BlockPos, ItemStack> getPedestalsWithItems();
	
//	@Overwrite
//	public Map<BlockPos, ItemStack> getPedestalsWithItems() {
//		Map<BlockPos, ItemStack> pedestals = new HashMap<>();
//		World world = this.getLevel();
//
//		int pedestalCount = 0;
//		if (world != null) {
//			BlockPos pos = this.getBlockPos();
//			Iterator<BlockPos> positions = BlockPos.betweenClosedStream(pos.offset(-3, 0, -3), pos.offset(3, 0, 3)).iterator();
//
//			while (positions.hasNext()) {
//				BlockPos aoePos = positions.next();
//				TileEntity tile = world.getBlockEntity(aoePos);
//
//				if (tile instanceof PedestalTileEntity) {
//					PedestalTileEntity pedestal = (PedestalTileEntity) tile;
//					ItemStack stack = pedestal.getInventory().getStackInSlot(0);
//
//					pedestalCount++;
//
//					if (!stack.isEmpty()) {
//						pedestals.put(aoePos.immutable(), stack);
//					}
//				}
////				if (tile instanceof SpiritJarTileEntity) {
////					SpiritJarTileEntity jar = (SpiritJarTileEntity) tile;
////					ItemStack stack = ((IInventory) jar).getInventory();
////
////					pedestalCount++;
////
////					if (!stack.isEmpty()) {
////						pedestals.put(aoePos.immutable(), stack);
////					}
////				}
//			}
//		}
//
//		this.pedestalCount = pedestalCount;
//
//		return pedestals;
//	}
	
	@Shadow
	public abstract <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count);

	public void spawnItemParticlesRemake(BlockPos pedestalPos, ItemStack stack) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		ServerWorld world = (ServerWorld) this.getLevel();
		BlockPos pos = this.getBlockPos();
         
        double x = pedestalPos.getX() + (world.getRandom().nextDouble() * 0.2D) + 0.4D;
		double y = pedestalPos.getY() + (world.getRandom().nextDouble() * 0.2D) + 1.4D;
		double z = pedestalPos.getZ() + (world.getRandom().nextDouble() * 0.2D) + 0.4D;

		double velX = (pos.getX() - pedestalPos.getX()) * 0.3;
		double velY = 0.0D;
		double velZ = (pos.getZ() - pedestalPos.getZ()) * 0.3;
        
        if(stack.getItem() instanceof SpiritItem) {
//            System.out.println(stack.getItem());
            SpiritItem spiritSplinterItem = (SpiritItem) stack.getItem();
            Color color = spiritSplinterItem.type.color;
//            SpiritHelper.spiritParticles(world, x,y,z, color);
    		
//            System.out.println("Spawn Wisp at: [" + x + "," + y + "," + z + "]" );
            
            
    		ParticleBuilder b = ParticleManager.create(MalumParticles.WISP_PARTICLE)
	            .setAlpha(0.15f, 0f)
	            .setLifetime(40)
	            .setScale(0.2f, 0)
	            .randomOffset(0.02f)
    	        .randomVelocity(0.01f, 0.01f)
	            .setColor(color, color.darker())
	            .randomVelocity(0.0025f, 0.0025f)
	            .addVelocity(velX, velY, velZ)
	            .enableNoClip();
//	            .enableGravity();
    		((IServerParticle) b).repeatServer(level, x, y, z, 4);
           
          ParticleBuilder c =  ParticleManager.create(MalumParticles.SPARKLE_PARTICLE)
	            .setAlpha(0.08f, 0f)
	            .setLifetime(20)
	            .setScale(0.5f, 0)
	            .randomOffset(0.1, 0.1)
	            .randomVelocity(0.02f, 0.02f)
	            .setColor(color, color.darker())
	            .randomVelocity(0.0025f, 0.0025f)
	            .enableNoClip();
//	            .enableGravity();
  		((IServerParticle) c).repeatServer(level, x, y, z, 4);
            
        } else {
    		world.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
        }
        
        
//		world.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
	}
	
	
	public boolean shouldSpawnItemParticlesRemake() {
		int powerCost = this.recipe.getPowerCost();
		int powerRate = this.recipe.getPowerRate();
		int endingPower = powerRate * 100;

		return this.progress > (powerCost - endingPower);
	}
	
	private static boolean areStacksEqualRemake(ItemStack stack1, ItemStack stack2) {
		if (stack1.isEmpty() && stack2.isEmpty()) {
			return true;
		} else {
//			System.out.println(!stack1.isEmpty());
//			System.out.println(stack1.sameItem(stack2));
//			System.out.println(ItemStack.tagMatches(stack1, stack2));
//			System.out.println((stack1.getCount() == stack2.getCount()));
			return !stack1.isEmpty() && stack1.sameItem(stack2) && ItemStack.tagMatches(stack1, stack2) && (stack1.getCount() == stack2.getCount());
		}
		
	}
	
	private static int getToTake(ItemStack itemStack, CombinationRecipe recipe, boolean result) {
//		Ingredient[] ingredients = (Ingredient[]) recipe.getIngredients().toArray();
		int toTake = 1;
		
//		if(!result) {
			for(Ingredient item : recipe.getIngredients()) {
//				System.out.println(item.getItems().length);
//				System.out.println(item.getItems()[0].toString());
				if(item.getItems().length > 0) {
//					System.out.println(item.getItems()[0].getItem().equals(itemStack.getItem()));
					if(item.getItems()[0].getItem().equals(itemStack.getItem())) {
						toTake = item.getItems()[0].getCount();
					}
				}
			}
//		}
		
//		System.out.println(toTake);
		return toTake;
	}
	
	@Overwrite
	public void tick() {
		boolean mark = false;

		Map<BlockPos, ItemStack> pedestalsWithItems = this.getPedestalsWithItems();
		World world = this.getLevel();

		if (world != null) {
			ItemStack[] stacks = pedestalsWithItems.values().toArray(new ItemStack[0]);
			//DEBUG
//			System.out.println(stacks.length);
			this.updateRecipeInventoryRemake(stacks);

			if (this.haveItemsChanged && (this.recipe == null || !this.recipe.matches(this.recipeInventory))) {
				this.recipe = (CombinationRecipe) world.getRecipeManager().getRecipeFor(RecipeTypes.COMBINATION, this.recipeInventory.toIInventory(), world).orElse(null);
				boolean flag = false;
				//stacksize check! (this sucks but whatevs)
				if(recipe != null && stacks != null) {
					for(Ingredient item : recipe.getIngredients()) {
	//						System.out.println(item.getItems().length);
	//						System.out.println(item.getItems()[0].toString());
						if(item.getItems().length > 0) {
	//							System.out.println(item.getItems()[0].getItem().equals(itemStack.getItem()));
							for(ItemStack stack : stacks) {
//								System.out.println("Count "+ stack.getCount());
//								System.out.println("Count "+ item.getItems()[0].getCount());
//								System.out.println("CheckType " + (item.getItems()[0].getItem().equals(stack.getItem())));
//								System.out.println("CheckSize " + (stack.getCount() >= item.getItems()[0].getCount()));
								if(item.getItems()[0].getItem().equals(stack.getItem()) && !(stack.getCount() >= item.getItems()[0].getCount())) {
									flag = true; 
									break;
								}
							}
						}
					}
					if(flag                                                                                         ) { this.recipe = null; this.haveItemsChanged = true; }
				}
			}
			
//			if (this.haveItemsChanged && (this.recipe == null || !this.recipe.matches(this.recipeInventory))) {
//				List<ICombinationRecipe> recipes = world.getRecipeManager().getAllRecipesFor(RecipeTypes.COMBINATION);
//				this.recipe = (CombinationRecipe) world.getRecipeManager().byType(RecipeTypes.COMBINATION).values().stream().flatMap((p_215372_3_) -> {
//			         return Util.toStream(RecipeTypes.COMBINATION.tryMatch(p_215372_3_, world, this.recipeInventory.toIInventory()));
//			      }).findFirst();
//			}

			if (!world.isClientSide()) {
				if (this.recipe != null) {
					if (this.energy.getEnergyStored() > 0) {
						
						if (soundCooldown > 0)
				        {
				            soundCooldown--;
				        }
						 
//						System.out.println(soundCooldown);
						if (soundCooldown <= 0)
		                {
//							System.out.println("playsound");
		                    level.playSound(null, worldPosition, MalumSounds.ALTAR_LOOP, SoundCategory.BLOCKS, 1f, 1f);
		                    soundCooldown = 180;
		                }
						
						boolean done = this.process(this.recipe);

						if (done) {
							
							
							
							for (BlockPos pedestalPos : pedestalsWithItems.keySet()) {
								TileEntity tile = world.getBlockEntity(pedestalPos);

								if (tile instanceof PedestalTileEntity) {
									PedestalTileEntity pedestal = (PedestalTileEntity) tile;
									IItemHandlerModifiable inventory = pedestal.getInventory();
									
									inventory.setStackInSlot(0, StackHelper.shrink(inventory.getStackInSlot(0), getToTake(inventory.getStackInSlot(0), this.recipe, false), true));
									pedestal.markDirtyAndDispatch();
									this.spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
								} 
//								else if (tile instanceof SpiritJarTileEntity) {
//									SpiritJarTileEntity jar = (SpiritJarTileEntity) tile;
//									
//									((IInventory) jar).setInventory(StackHelper.shrink(((IInventory) jar).getInventory(), getToTake(((IInventory) jar).getInventory(), this.recipe, false), true));
////									jar.markDirty();
//									this.spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
//								}
							}

							double stability = ((IStabilityRecipe) this.recipe).getAdjustedStability(getLastStability());
							stability = (AMIConfig.minimumStability.get() + 25) - stability;
//							AMICore.LOGGER.debug(stability);
							//if altar stability is greater than 25, prevent failure
							//else give factor equal to 0.1 times the scale, maxing at 15% chance of failure
							// then 0.15 is multiplied by recipe stab (its chances of success) and applied.
							// so a 25+ altar stab (after adjustment is always gonna prevent failures
							stability = stability * 1 / 15;
							stability = (stability - 1) * 0.1;
							if(stability < 0) stability = 0.0;

							
							
//							AMICore.LOGGER.debug(stability);
							
							// stability/500 chance to do event (min 1 every 25 sec, max 1 every sec)
							
							double rand = level.random.nextDouble();
//							AMICore.LOGGER.debug(rand);
							
//							this.inventory.setStackInSlot(0, this.recipe.getCraftingResult(this.recipeInventory));
							//TODO: CHECK THE RECIPE RESULT LOGIC
							ItemEntity item = new ItemEntity(world, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.2, this.getBlockPos().getZ() + 0.5, this.recipe.getCraftingResult(this.recipeInventory));
							item.setNoPickUpDelay();
							item.setDeltaMovement(0.0, 0.0, 0.0);
							inventory.setStackInSlot(0, StackHelper.shrink(inventory.getStackInSlot(0), getToTake(inventory.getStackInSlot(0), this.recipe, true), true));
							this.progress = 0;
							
							
							// do abject failure chance (low effective stab means higher fail chance, cuts off at less than 15
							if (rand > stability) {
								this.spawnParticles(ParticleTypes.END_ROD, this.getBlockPos(), 1.1, 50);
								world.addFreshEntity(item);
								level.playSound(null, worldPosition, MalumSounds.ALTAR_CRAFT, SoundCategory.BLOCKS, 1, 0.9f + level.random.nextFloat() * 0.2f);
//								System.out.println("recipe success!");
								
							} else {
								this.spawnParticles(ParticleTypes.ASH, this.getBlockPos(), 1.1, 50);
								this.spawnParticles(ParticleTypes.SMOKE, this.getBlockPos(), 1.1, 50);level.playSound(null, worldPosition, MalumSounds.ALTAR_CRAFT, SoundCategory.BLOCKS, 1, 0.9f + level.random.nextFloat() * 0.2f);
								level.playSound(null, worldPosition, MalumSounds.ALTAR_CRAFT, SoundCategory.BLOCKS, 1, 0.4f + level.random.nextFloat() * 0.2f);
//								System.out.println("recipe failure!");
							}

							mark = true;
						} else {
							//update stability every 20 ticks
							if(doStabilityUpdate(this.progress) && this.recipe != null) {
								setLastStability(getCrafterStabilityFactor());
							}
							
//							AMICore.LOGGER.debug(getLastStability());
							
							//adjust for min and max, to feel reduction effect need at least 15 factor!
							double stability = getLastStability();
							//get stab recipe effect
//							System.out.println("before: " + stability);
							stability = ((IStabilityRecipe) this.recipe).getAdjustedStability(stability);
//							System.out.println("after: " + stability);
							
							stability = (AMIConfig.minimumStability.get() + 25) - stability;
//							AMICore.LOGGER.debug(stability);
							if (stability < 1.0) {
								stability = 1.0;
							} else if (stability > 25.0) {
								stability = 25;
							}
//							AMICore.LOGGER.debug(stability);
							
							// stability/500 chance to do event (min 1 every 25 sec, max 1 every sec)
							
							double rand = level.random.nextDouble();
							
							//can only fire at max every 12 ticks, 25/500 becomes on avg 1/26 ticks
							if(this.stabilityCooldown <= 0) {
								//call weighted random effect on random fire
								if (rand < stability/500) {
									StabilityEvents.randomEffect(world, worldPosition, this, stability);
									this.stabilityCooldown = 12;
								}
							} else {
								this.stabilityCooldown--;
							}
							
							
							this.spawnParticles(ParticleTypes.ENTITY_EFFECT, this.getBlockPos(), 1.15, 2);

							if (this.shouldSpawnItemParticlesRemake()) {
								for (BlockPos pedestalPos : pedestalsWithItems.keySet()) {
									TileEntity tile = world.getBlockEntity(pedestalPos);
									
									int powerCost = this.recipe.getPowerCost();
									int powerRate = this.recipe.getPowerRate();
									int endingPowerItem = powerRate * 100;
									int endingPowerSpirit = powerRate * 100;

									if (tile instanceof PedestalTileEntity && (this.progress > (powerCost - endingPowerItem))) {
										PedestalTileEntity pedestal = (PedestalTileEntity) tile;
										IItemHandlerModifiable inventory = pedestal.getInventory();
										ItemStack stack = inventory.getStackInSlot(0);
										this.spawnItemParticlesRemake(pedestalPos, stack);
									} 
//									if (tile instanceof SpiritJarTileEntity && (this.progress > (powerCost - endingPowerSpirit))) {
//										SpiritJarTileEntity jar = (SpiritJarTileEntity) tile;
//										ItemStack stack = ((IInventory) jar).getInventory();
//										this.spawnItemParticlesRemake(pedestalPos, stack);
//									}
								}
							}
						}
					}
				} else {
					this.progress = 0;
				}
			}
		}

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			if (!mark)
				mark = true;
		}

		if (mark) {
			this.markDirtyAndDispatch();
		}
	}
	
	private void updateRecipeInventoryRemake(ItemStack[] items) {
		boolean haveItemsChanged = this.recipeInventory.getSlots() != items.length + 1
				|| !areStacksEqualRemake(this.recipeInventory.getStackInSlot(0), this.inventory.getStackInSlot(0));

		if (!haveItemsChanged) {
			for (int i = 0; i < items.length; i++) {
				if (!areStacksEqualRemake(this.recipeInventory.getStackInSlot(i + 1), items[i])) {
					haveItemsChanged = true;
					break;
				}
			}
		}

		this.haveItemsChanged = haveItemsChanged;

		if (!haveItemsChanged)
			return;

		this.recipeInventory.setSize(items.length + 1);
		this.recipeInventory.setStackInSlot(0, this.inventory.getStackInSlot(0));

		for (int i = 0; i < items.length; i++) {
			this.recipeInventory.setStackInSlot(i + 1, items[i]);
		}
	}
	
	@Override
	public double getCrafterStabilityFactor() {
		double output = 0;
		
		HashMap<String, Pair<List<BlockPos>, Integer>> objects = new HashMap<String,  Pair<List<BlockPos>, Integer>>();
		
		for(int offX = (int) (-1 * getSearchRangeXZ()); offX <= getSearchRangeXZ(); offX++) {
			for(int offZ = (int) (-1 * getSearchRangeXZ()); offZ <= getSearchRangeXZ(); offZ++) {
				for(int offY = (int) (-1 * getSearchRangeY()); offY <= getSearchRangeY(); offY++) {
//					AMICore.LOGGER.debug(this.getBlockPos().offset(offX, offZ, offY).toString());
					BlockState toCheck = this.level.getBlockState(this.getBlockPos().offset(offX, offY, offZ));
//					AMICore.LOGGER.debug(toCheck.getBlock().toString());
//					System.out.println(AMICore.STABILITY_OBJECTS.hasKey(new ResourceLocation("amicore:dark_oak_log")));
//					System.out.println(AMICore.STABILITY_OBJECTS.hasKey(new ResourceLocation("minecraft:dark_oak_log")));
//					System.out.println(AMICore.STABILITY_OBJECTS.getData(new ResourceLocation("amicore:dark_oak_log")));
//					System.out.println(AMICore.STABILITY_OBJECTS.getData(new ResourceLocation("minecraft:dark_oak_log")));
//					System.out.println(new ResourceLocation("amicore", toCheck.getBlock().getRegistryName().getPath()));
//					System.out.println(AMICore.STABILITY_OBJECTS.getData(new ResourceLocation("amicore", toCheck.getBlock().getRegistryName().getPath())));
//					System.out.println(new ResourceLocation("amicore", toCheck.getBlock().getRegistryName().getNamespace()));
//					System.out.println(AMICore.STABILITY_OBJECTS.getData(new ResourceLocation("amicore", toCheck.getBlock().getRegistryName().getNamespace())));
//					AMICore.LOGGER.debug(toCheck.getBlock().getRegistryName().toString());
					
					if(AMICore.STABILITY_OBJECTS.hasKey(new ResourceLocation("amicore", toCheck.getBlock().getRegistryName().getPath()))) {
//						AMICore.LOGGER.debug("Found Potential Stabalizer at: " + toCheck.toString() + "  Block: " + stb0.blockstate.toString() + "  Factor: " + stb0.stability_factor);
						StabilityObject stb0 = AMICore.STABILITY_OBJECTS.getData(new ResourceLocation("amicore", toCheck.getBlock().getRegistryName().getPath()));
						if(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stb0.block)).equals(toCheck.getBlock())) {
//							AMICore.LOGGER.debug("Properties Match at: " + toCheck.toString() + "  Block: " + stb0.blockstate.toString() + "  Factor: " + stb0.stability_factor);
														
							//Gather list of objects alongside
							Pair<List<BlockPos>, Integer> objs = objects.get(stb0.block);
							objects.remove(stb0.block);
							
							List<BlockPos> poses;
							int count;
							if(objs != null) {
								poses = objs.getFirst();
								count = objs.getSecond();
								
								if((Integer) count < stb0.maximum) {
									count++;
									poses.add(this.getBlockPos().offset(offX, offY, offZ));
									output += stb0.stability_factor;
								}						
							} else {
								poses = new ArrayList<BlockPos>();
								poses.add(this.getBlockPos().offset(offX, offY, offZ));
								count = 1; 
								output += stb0.stability_factor;
							}
							objs = new Pair<List<BlockPos>, Integer>(poses, count);							
							objects.put(stb0.block, objs);
						}
					}
				}
			}
		}
				
//		if(true) {
//			output *= 0.5;
//		}
		
		setLastStabilityObjects(objects);
		return output * getBaseStability();
	}
	
	

	@Override
	public double getBaseStability() {
		return 1.0;
	}
	
	@Override
	public double getSearchRangeXZ() {
		return 5.0;
	}
	
	@Override
	public double getSearchRangeY() {
		return 3.0;
	}

	@Override
	public double getLastStability() {
		return this.lastStability;
	}
	
	public void setLastStability(double in) {
		this.lastStability = in;
	}
	
	@Override
	public boolean doStabilityUpdate(int progress) {
		return progress % 20 == 0;
	}
	
	@Override
	public HashMap<String, Pair<List<BlockPos>, Integer>> getLastStabilityObjects() {
		return this.stabilityObjects;
	}
	
	@Override
	public void setLastStabilityObjects(HashMap<String, Pair<List<BlockPos>, Integer>> in) {
		this.stabilityObjects = in;
	}
	
}
