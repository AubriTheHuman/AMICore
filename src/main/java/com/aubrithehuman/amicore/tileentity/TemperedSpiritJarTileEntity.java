package com.aubrithehuman.amicore.tileentity;

import com.aubrithehuman.amicore.inventory.JarInventory;
import com.blakebr0.cucumber.tileentity.BaseTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TemperedSpiritJarTileEntity extends BaseTileEntity {

    private final LazyOptional<IItemHandler> capability = LazyOptional.of(this::getInventory);
	
	private final JarInventory inventory;
	
	public TemperedSpiritJarTileEntity() {
		super(ModTileEntities.TEMPERED_JAR.get());
		this.inventory = new JarInventory(64, this::markDirtyAndDispatch);

		this.inventory.setDefaultSlotLimit(64);
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	@Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        this.getInventory().deserializeNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.merge(this.getInventory().serializeNBT());
        return tag;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.capability);
        }

        return super.getCapability(cap, side);
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        BlockPos pos = this.getPos();
        return player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }
}
