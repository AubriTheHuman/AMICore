package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aubrithehuman.amicore.block.ModBlocks;
import com.blakebr0.extendedcrafting.compat.jei.CombinationCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.JeiCompat;
import com.blakebr0.extendedcrafting.config.ModConfigs;

import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.item.ItemStack;

@Mixin(JeiCompat.class)
public abstract class MixinExtendedCraftingJeiCompat {

	@Inject(method = "registerRecipeCatalysts", at = @At("HEAD"))
	public void addCatalysts(IRecipeCatalystRegistration registration, CallbackInfo ci) {
		if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.TEMPERED_JAR.get()), CombinationCraftingCategory.UID);
		}
	}
	
}
