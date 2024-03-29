package com.aubrithehuman.amicore.block;

import com.aubrithehuman.amicore.inventory.JarInventory;
import com.aubrithehuman.amicore.item.ModItems;
import com.aubrithehuman.amicore.tileentity.TemperedSpiritJarTileEntity;
import com.blakebr0.cucumber.block.BaseBlock;
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

public class TemperedSpiritJar extends Block  {

	public static final VoxelShape JAR_SHAPE = new VoxelShapeBuilder()
			.cuboid(2.5, 0.5, 2.5, 13.5, 13.5, 13.5)
			.cuboid(3.5, 14.5, 3.5, 12.5, 16.5, 12.5)
			.cuboid(4.5, 13.5, 4.5, 11.5, 14.5, 11.5)
			.cuboid(5.5, 0, 5.5, 10.5, 1, 10.5)
			.build();
	
	public TemperedSpiritJar() {
		super(Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(5.0f, 10.0f).harvestTool(ToolType.PICKAXE).variableOpacity());
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
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TemperedSpiritJarTileEntity) {
			TemperedSpiritJarTileEntity jar = (TemperedSpiritJarTileEntity) tile;
			if(jar.getInventory() instanceof JarInventory) {
				JarInventory inventory = (JarInventory) jar.getInventory();
				ItemStack input = inventory.getStackInSlot(inventory.getLastFilledSlot());
				ItemStack held = player.getHeldItem(hand);
				if (input.isEmpty() && !held.isEmpty()) {
					if(held.getItem() instanceof SpiritItem) {
						//add stack and return remaining stack
						ItemStack inserting = inventory.insertStackInLastSlot(StackHelper.withSize(held, held.getCount(), false));
//						if(inserting.equals(held, true)) {
							player.setHeldItem(hand, inserting);
							world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);							
//						}
					}
				} else if (!input.isEmpty() && !held.isEmpty()) {
					if(held.getItem() instanceof SpiritItem) {
						//need to match type!
						if(held.getItem() == input.getItem()) {
							//increment based on what already exists
							ItemStack inserting = inventory.insertStackInLastSlot(StackHelper.withSize(held, held.getCount(), false));
							player.setHeldItem(hand, inserting);
							if(inserting.isEmpty()) {
								world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);							
							}
						}
					}
				} else if (!input.isEmpty() && player.isCrouching() && held.isEmpty()) {
					ItemEntity item = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), input);
					item.setNoPickupDelay();
					world.addEntity(item);
					inventory.setStackInSlot(inventory.getLastFilledSlot(), ItemStack.EMPTY);
				}
				
				if(!input.isEmpty() && held.getItem().equals(MalumItems.HALLOWED_SPIRIT_RESONATOR.get())) {
					if(!world.isRemote()) {
						player.sendMessage(new StringTextComponent("Jar Contents: " + input.getItem().getRegistryName().toString() + "*" + inventory.getTotal()).mergeStyle(TextFormatting.AQUA), player.getUniqueID());
					}
				}
			}
		}

		return ActionResultType.SUCCESS;
	}
	 
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TemperedSpiritJarTileEntity) {
				TemperedSpiritJarTileEntity jar = (TemperedSpiritJarTileEntity) tile;
				InventoryHelper.dropInventoryItems(world, pos, ((JarInventory) jar.getInventory()).toIInventory());
			}
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}
	

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return JAR_SHAPE;
	}
	
}
