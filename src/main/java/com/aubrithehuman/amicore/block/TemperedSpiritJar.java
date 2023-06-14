package com.aubrithehuman.amicore.block;

import com.aubrithehuman.amicore.inventory.JarInventory;
import com.aubrithehuman.amicore.tileentity.TemperedSpiritJarTileEntity;
import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.sammy.malum.common.items.SpiritItem;
import com.sammy.malum.core.init.items.MalumItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class TemperedSpiritJar extends BaseTileEntityBlock  {

	public static final VoxelShape JAR_SHAPE = new VoxelShapeBuilder()
			.cuboid(13, 14, 13, 3, 2, 3)
			.cuboid(15, 2, 15, 1, 0, 1)
			.cuboid(14, 16, 14, 2, 14, 2)
			.build();
	
	public TemperedSpiritJar() {
		super(Material.GLASS, SoundType.GLASS, 5.0F, 10.0F, ToolType.PICKAXE);
	}

	@Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TemperedSpiritJarTileEntity();
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		TileEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TemperedSpiritJarTileEntity) {
			TemperedSpiritJarTileEntity jar = (TemperedSpiritJarTileEntity) tile;
			if(jar.getInventory() instanceof JarInventory) {
				JarInventory inventory = (JarInventory) jar.getInventory();
				ItemStack input = inventory.getStackInSlot(0);
				ItemStack held = player.getItemInHand(hand);
				if (input.isEmpty() && !held.isEmpty()) {
					if(held.getItem() instanceof SpiritItem) {
						inventory.setStackInSlot(0, StackHelper.withSize(held, held.getCount(), false));
						player.setItemInHand(hand, StackHelper.shrink(held, held.getCount(), false));
						world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				} else if (!input.isEmpty() && !held.isEmpty()) {
					if(held.getItem() instanceof SpiritItem) {
						//need to match type!
						if(held.getItem() == input.getItem()) {
							//increment based on what already exists
							inventory.setStackInSlot(0, StackHelper.withSize(held, held.getCount() + input.getCount(), false));
							player.setItemInHand(hand, StackHelper.shrink(held, held.getCount(), false));
							world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);							
						}
					}
				}  else if (!input.isEmpty() && player.isCrouching() && held.isEmpty()) {
					ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), input);
					item.setNoPickUpDelay();
					world.addFreshEntity(item);
					inventory.setStackInSlot(0, ItemStack.EMPTY);
				} else if(!input.isEmpty() && player.isCrouching() && (held.getItem().getRegistryName().equals(MalumItems.HALLOWED_SPIRIT_RESONATOR.get().getRegistryName()) || held.getItem().getRegistryName().equals(MalumItems.STAINED_SPIRIT_RESONATOR.get().getRegistryName()))) {
					player.sendMessage(new StringTextComponent("Jar Contents: " + input.getItem().getName(input) + "*" + input.getCount()).withStyle(TextFormatting.AQUA), player.getUUID());
				}
			}
		}

		return ActionResultType.SUCCESS;
	}
	 
	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof TemperedSpiritJarTileEntity) {
				TemperedSpiritJarTileEntity jar = (TemperedSpiritJarTileEntity) tile;
				InventoryHelper.dropContents(world, pos, ((JarInventory) jar.getInventory()).getStacks());
			}
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}
	

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return JAR_SHAPE;
	}
	
}
