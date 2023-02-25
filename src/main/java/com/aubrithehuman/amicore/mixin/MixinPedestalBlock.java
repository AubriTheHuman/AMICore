package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

@Mixin(PedestalBlock.class)
public abstract class MixinPedestalBlock {

	@Overwrite
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		TileEntity tile = world.getBlockEntity(pos);
		if (tile instanceof PedestalTileEntity) {
			PedestalTileEntity pedestal = (PedestalTileEntity) tile;
			BaseItemStackHandler inventory = pedestal.getInventory();
			ItemStack input = inventory.getStackInSlot(0);
			ItemStack held = player.getItemInHand(hand);
			if (input.isEmpty() && !held.isEmpty()) {
				inventory.setStackInSlot(0, StackHelper.withSize(held, held.getCount(), false));
				player.setItemInHand(hand, StackHelper.shrink(held, held.getCount(), false));
				world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			} else if (!input.isEmpty()) {
				ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), input);
				item.setNoPickUpDelay();
				world.addFreshEntity(item);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
		}

		return ActionResultType.SUCCESS;
	}
	
	
}
