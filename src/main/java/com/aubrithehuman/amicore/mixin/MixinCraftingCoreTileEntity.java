package com.aubrithehuman.amicore.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.crafting.IStabilityCrafting;
import com.aubrithehuman.amicore.crafting.StabilityObject;
import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
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
	
	@Inject(method = "<init>*", at = @At("RETURN"))
    public void constructorTail(CallbackInfo ci) {
//        System.out.println("Override Inventory Size on craftingCore");
        this.inventory.setDefaultSlotLimit(64);
		this.recipeInventory.setDefaultSlotLimit(64);
		this.baseStability = 1;
    }
	
	@Shadow
	public abstract CombinationRecipe getActiveRecipe();
	
	@Shadow
	public abstract boolean process(CombinationRecipe recipe);
	
	@Shadow
	public abstract Map<BlockPos, ItemStack> getPedestalsWithItems();
	
	@Shadow
	public abstract <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count);

	@Shadow
	public abstract void spawnItemParticles(BlockPos pedestalPos, ItemStack stack);
	
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

							this.spawnParticles(ParticleTypes.END_ROD, this.getBlockPos(), 1.1, 50);
//							this.inventory.setStackInSlot(0, this.recipe.getCraftingResult(this.recipeInventory));
							//TODO: CHECK THE RECIPE RESULT LOGIC
							ItemEntity item = new ItemEntity(world, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), this.recipe.getCraftingResult(this.recipeInventory));
							item.setNoPickUpDelay();
							world.addFreshEntity(item);
							inventory.setStackInSlot(0, StackHelper.shrink(inventory.getStackInSlot(0), getToTake(inventory.getStackInSlot(0), this.recipe, true), true));
							this.progress = 0;

							mark = true;
						} else {
							
							double altar_stability = getCrafterStabilityFactor();
							AMICore.LOGGER.debug(altar_stability);
							double rand = level.random.nextDouble();
							if (rand < altar_stability) {
								
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
					BlockState toCheck = this.level.getBlockState(this.getBlockPos().offset(offX, offZ, offY));
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

	public double getBaseStability() {
		return 1.0;
	}
	public double getSearchRangeXZ() {
		return 5.0;
	}
	public double getSearchRangeY() {
		return 3.0;
	}
	
}
