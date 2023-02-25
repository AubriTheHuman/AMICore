package com.aubrithehuman.amicore.mixin;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.crafting.IStabilityCrafting;
import com.aubrithehuman.amicore.crafting.StabilityEvents;
import com.aubrithehuman.amicore.crafting.StabilityObject;
import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.sammy.malum.MalumHelper;
import com.sammy.malum.common.items.SpiritItem;
import com.sammy.malum.core.init.MalumSounds;
import com.sammy.malum.core.init.particles.MalumParticles;
import com.sammy.malum.core.systems.particles.ParticleManager;
import com.sammy.malum.core.systems.spirits.SpiritHelper;

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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandlerModifiable;

@Mixin(CraftingCoreTileEntity.class)
public abstract class MixinCraftingCoreTileEntity extends BaseInventoryTileEntity implements IStabilityCrafting {

	public MixinCraftingCoreTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Shadow
	public final BaseItemStackHandler inventory;
	@Shadow
	private final BaseEnergyStorage energy;
	@Shadow
	private final BaseItemStackHandler recipeInventory;
	@Shadow
	private CombinationRecipe recipe;
	@Shadow
	private int progress;
	@Shadow
	private int oldEnergy;
	@Shadow
	private int pedestalCount;
	@Shadow
	private boolean haveItemsChanged;
	
	//Mixin Check
	private double lastStability;
	private int stabilityCooldown = 60;
	public int soundCooldown = 0;
	
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
	
	@Shadow
	public abstract <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count);

	@Overwrite
	public void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		ServerWorld world = (ServerWorld) this.getLevel();
		BlockPos pos = this.getBlockPos();
 
        Random rand = world.random;
        System.out.println(stack.toString());
        
        if(stack.getItem() instanceof SpiritItem) {
            System.out.println(stack.getItem());
        	double x = worldPosition.getX();
            double y = worldPosition.getY();
            double z = worldPosition.getZ();
            SpiritItem spiritSplinterItem = (SpiritItem) stack.getItem();
            Color color = spiritSplinterItem.type.color;
            SpiritHelper.spiritParticles(world, x,y,z, color);
    		
    		Vector3d spot = MalumHelper.pos(this.worldPosition).add(0.5f,1.15f,0.5f);
    		
    		Vector3d velocity = new Vector3d(x, y, z).subtract(spot).normalize().scale(-0.03f);
            System.out.println("Spawn Wisp");
            ParticleManager.create(MalumParticles.WISP_PARTICLE)
                .setAlpha(0.15f, 0f)
                .setLifetime(40)
                .setScale(0.2f, 0)
                .randomOffset(0.02f)
                .randomVelocity(0.01f, 0.01f)
                .setColor(color, color.darker())
                .randomVelocity(0.0025f, 0.0025f)
                .addVelocity(velocity.x, velocity.y, velocity.z)
                .enableNoClip()
                .repeat(world, x, y, z, 2);
            ParticleManager.create(MalumParticles.SPARKLE_PARTICLE)
	            .setAlpha(0.08f, 0f)
	            .setLifetime(20)
	            .setScale(0.5f, 0)
	            .randomOffset(0.1, 0.1)
	            .randomVelocity(0.02f, 0.02f)
	            .setColor(color, color.darker())
	            .randomVelocity(0.0025f, 0.0025f)
	            .enableNoClip()
	            .repeat(world, pedestalPos.getX(),pedestalPos.getY(),pedestalPos.getZ(), 2);
            
        }
        
        
//		world.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
	}
	@Shadow
	public abstract boolean shouldSpawnItemParticles();
	
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
			this.updateRecipeInventory(stacks);

			if (this.haveItemsChanged && (this.recipe == null || !this.recipe.matches(this.recipeInventory))) {
				this.recipe = (CombinationRecipe) world.getRecipeManager().getRecipeFor(RecipeTypes.COMBINATION, this.recipeInventory.toIInventory(), world).orElse(null);
			}

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
							}
							
							double stability = 40 - getLastStability();
//							AMICore.LOGGER.debug(stability);
							//if altar stability is greater than 25, prevent failure
							//else give factor equal to 0.1 times the scale, maxing at 15% chance of failure
							// then 0.15 is multiplied by recipe stab (its chances of success) and applied.
							// so a 25+ altar stab is always gonna prevent failures
							stability = stability * 1 / 15;
							stability = (stability - 1) * 0.1;
							
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
							
							double recipeStab = 1.0;
							
							// do abject failure chance (low effective stab means higher fail chance, cuts off at less than 15
							if (rand > stability * recipeStab) {
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
							double stability = 40 - getLastStability();
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

							if (this.shouldSpawnItemParticles()) {
								for (BlockPos pedestalPos : pedestalsWithItems.keySet()) {
									TileEntity tile = world.getBlockEntity(pedestalPos);

									if (tile instanceof PedestalTileEntity) {
										PedestalTileEntity pedestal = (PedestalTileEntity) tile;
										IItemHandlerModifiable inventory = pedestal.getInventory();
										ItemStack stack = inventory.getStackInSlot(0);
										this.spawnItemParticles(pedestalPos, stack);
									}
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
	
	@Overwrite
	private void updateRecipeInventory(ItemStack[] items) {
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
						if(stb0.blockstate.equals(toCheck)) {
//							AMICore.LOGGER.debug("Properties Match at: " + toCheck.toString() + "  Block: " + stb0.blockstate.toString() + "  Factor: " + stb0.stability_factor);
							output += stb0.stability_factor;
						}
					}
				}
			}
		}
				
//		if(true) {
//			output *= 0.5;
//		}
		
		
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
	
}
