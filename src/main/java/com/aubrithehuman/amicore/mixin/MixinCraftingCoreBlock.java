package com.aubrithehuman.amicore.mixin;

import java.util.HashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.config.AMIConfig;
import com.aubrithehuman.amicore.crafting.IStabilityCrafting;
import com.aubrithehuman.amicore.crafting.IStabilityRecipe;
import com.aubrithehuman.amicore.crafting.StabilityObject;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.block.CraftingCoreBlock;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.mojang.datafixers.util.Pair;
import com.sammy.malum.core.init.items.MalumItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

@Mixin(CraftingCoreBlock.class)
public abstract class MixinCraftingCoreBlock {

	@Overwrite
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		ItemStack held = player.getItemInHand(hand);
		if (!world.isClientSide()) {
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof CraftingCoreTileEntity) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				if (trace.getDirection() == Direction.UP) {
					BaseItemStackHandler inventory = core.getInventory();
					ItemStack stack = inventory.getStackInSlot(0);
					if (stack.isEmpty()) {
						if (!held.isEmpty()) {
							inventory.setStackInSlot(0, StackHelper.withSize(held, held.getCount(), false));
							player.setItemInHand(hand, StackHelper.shrink(held, held.getCount(), false));
							world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack);
						item.setNoPickUpDelay();
						world.addFreshEntity(item);
						inventory.setStackInSlot(0, ItemStack.EMPTY);
					}
				} else {
//					System.out.println(held.getItem().toString());
					if (held.getItem().equals(MalumItems.HALLOWED_SPIRIT_RESONATOR.get())) {

						((IStabilityCrafting) core).setLastStability(((IStabilityCrafting) core).getCrafterStabilityFactor());
						double stab = ((IStabilityCrafting) core).getLastStability();
						
						player.sendMessage(new StringTextComponent(String.format("Altar Stability: %.1f", stab)).withStyle(TextFormatting.LIGHT_PURPLE), player.getUUID());
						
						if(core.getActiveRecipe() != null) {
							stab = ((IStabilityRecipe) core.getActiveRecipe()).getAdjustedStability(stab);
							player.sendMessage(new StringTextComponent(String.format("Recipe Stability: %.1f", ((IStabilityRecipe) core.getActiveRecipe()).getInherantStability() * 100.0 ) + "%").withStyle(TextFormatting.YELLOW), player.getUUID());
							player.sendMessage(new StringTextComponent(String.format("Adjusted Stability: %.1f", stab)).withStyle(TextFormatting.YELLOW), player.getUUID());
							
							double stability = stab;
							
							stability = (AMIConfig.minimumStability.get() + 25) - stability;
//								AMICore.LOGGER.debug(stability);	
							//if altar stability is greater than 15, prevent failure
							//else give factor equal to 0.1 times the scale, maxing at 15% chance of failure
							// then 0.15 is multiplied by recipe stab (its chances of success) and applied.
							// so a 25+ altar stab (after adjustment is always gonna prevent failures
							stability = stability * 1 / 15;
							stability = (stability - 1) * 0.1;
							if(stability < 0) stability = 0.0;
							player.sendMessage(new StringTextComponent(String.format("Recipe Fail Chance: %.1f", stability * 100.0) + "%").withStyle(TextFormatting.GOLD), player.getUUID());
							
						  
						} else {
							double stability = stab;
							stability = (AMIConfig.minimumStability.get() + 25) - stability;
//								AMICore.LOGGER.debug(stability);
							//if altar stability is greater than 25, prevent failure
							//else give factor equal to 0.1 times the scale, maxing at 15% chance of failure
							// then 0.15 is multiplied by recipe stab (its chances of success) and applied.
							// so a 25+ altar stab (after adjustment is always gonna prevent failures
							stability = stability * 1 / 15;
							stability = (stability - 1) * 0.1;
							if(stability < 0) stability = 0.0;
							player.sendMessage(new StringTextComponent(String.format("Altar Fail Chance: %.1f", stability * 100.0) + "%").withStyle(TextFormatting.GOLD), player.getUUID());
							
						}
						
						stab = (AMIConfig.minimumStability.get() + 25) - stab;
//							AMICore.LOGGER.debug(stability);
						if (stab < 1.0) {
							stab = 1.0;
						} else if (stab > 25.0) {
							stab = 25;
						}
						
						player.sendMessage(new StringTextComponent(String.format("Final Stability: %.1f/24.0", 25 - stab)).withStyle(stab <= 5 ? TextFormatting.GREEN : stab <= 12 ? TextFormatting.YELLOW : TextFormatting.RED), player.getUUID());
						
					} else if (held.getItem().equals(MalumItems.STAINED_SPIRIT_RESONATOR.get())) { 
						((IStabilityCrafting) core).setLastStability(((IStabilityCrafting) core).getCrafterStabilityFactor());
						double stab = ((IStabilityCrafting) core).getLastStability();
						
						HashMap<String, Pair<List<BlockPos>, Integer>> objs = ((IStabilityCrafting) core).getLastStabilityObjects();
						player.sendMessage(new StringTextComponent("Altar Stabilizers:"), player.getUUID());
						int i = 0;
						for (String key : objs.keySet()) {
							//TODO Print out total of objects detected
							Pair<List<BlockPos>, Integer> p = objs.get(key);
							if(p != null) {
								Integer count = p.getSecond();
								AMICore.LOGGER.debug(key);                      
								StabilityObject obj = AMICore.STABILITY_OBJECTS.getData(new ResourceLocation("amicore", key.substring(key.lastIndexOf(":") + 1)));
								if(obj != null) {
									player.sendMessage(new StringTextComponent("  -" + key + ": " + count + "/" + obj.maximum + (count >= obj.maximum ? " Max " : " ") +  (obj != null ? "(*" + obj.stability_factor + " each)" : "")).withStyle(TextFormatting.GRAY), player.getUUID());
								}
							}
							i++;
						}
						if(i == 0) player.sendMessage(new StringTextComponent("  -None!").withStyle(TextFormatting.GRAY), player.getUUID());
						
					} else {
						NetworkHooks.openGui((ServerPlayerEntity) player, core, pos);
					}
				}
			}
		}

		return ActionResultType.SUCCESS;
	}
	
}
