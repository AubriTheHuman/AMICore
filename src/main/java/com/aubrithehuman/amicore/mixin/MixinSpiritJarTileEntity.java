package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.aubrithehuman.amicore.inventory.IInventory;
import com.aubrithehuman.amicore.malum.MalumSpiritAdditons;
import com.sammy.malum.common.blocks.spiritjar.SpiritJarTileEntity;
import com.sammy.malum.core.systems.spirits.MalumSpiritType;
import com.sammy.malum.core.systems.tileentities.SimpleTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;

@Mixin(SpiritJarTileEntity.class)
public abstract class MixinSpiritJarTileEntity extends SimpleTileEntity implements IInventory {

	public MixinSpiritJarTileEntity(TileEntityType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Shadow
	public MalumSpiritType type;
	
	@Shadow
    public int count;
	
	
	@Override
	public ItemStack getInventory() {
		if(type != null && count > 0) {
			return new ItemStack(type.splinterItem(), count);			
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventory(ItemStack item) {
		this.type = MalumSpiritAdditons.getByItem(item);
		this.count = item.getCount();
	}
	
	

}
