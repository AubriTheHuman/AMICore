                             package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;

@Mixin(PedestalTileEntity.class)
public abstract class MixinPedestalTileEntity {


	@Shadow
	public final BaseItemStackHandler inventory;
	
	public MixinPedestalTileEntity() {
		inventory = null;
	}
	
	
	@Inject(method = "<init>*", at = @At("RETURN"))
    public void constructorTail(CallbackInfo ci) {
//        System.out.println("Override Inventory Size on pedestal");
        this.inventory.setDefaultSlotLimit(64);
    }
	
}
