package com.aubrithehuman.amicore.mixin.malum;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.sammy.malum.common.blocks.spiritaltar.SpiritAltarTileEntity;
import com.sammy.malum.core.modcontent.MalumSpiritAltarRecipes.MalumSpiritAltarRecipe;

@Mixin(SpiritAltarTileEntity.class)
public abstract class MalumSpiritAltarFixer {

	@Shadow
    public MalumSpiritAltarRecipe recipe;
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void nullFix() {
		recipe = null;
	}
}
