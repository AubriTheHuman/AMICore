package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.aubrithehuman.amicore.crafting.IStabilityRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.compat.jei.CombinationCraftingCategory;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

@Mixin(CombinationCraftingCategory.class)
public abstract class MixinCombinationCraftingCategory implements IRecipeCategory<ICombinationRecipe> {

//	@Override
//	public List<ITextComponent> getTooltipStrings(ICombinationRecipe recipe, double mouseX, double mouseY) {
//		if (mouseX > 1 && mouseX < 14 && mouseY > 9 && mouseY < 86) {
//			return Arrays.asList(
//					new StringTextComponent(NumberFormat.getInstance().format(recipe.getPowerCost()) + " FE"),
//					new StringTextComponent(NumberFormat.getInstance().format(recipe.getPowerRate()) + " FE/t")
//			);
//		}
//
//		if (mouseX > 5 && mouseX < 23 && mouseY > 144 && mouseY < 165) {
//			return recipe.getInputsList();
//		}
//		
//		if(recipe != null) {
//			IStabilityRecipe r = (IStabilityRecipe) recipe;
//			if (mouseX > 28 && mouseX < 50 && mouseY > -18 && mouseY < -24) {
//				return Arrays.asList(new StringTextComponent(String.format("Stability: %.1F", r.getInherantStability() * 100.0) + "%"));
//			}
//		}
//
//		return Collections.emptyList();
//	}
	
	@Override
	public void draw(ICombinationRecipe recipe, MatrixStack matrix, double mouseX, double mouseY) {
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		
		if(recipe != null) {
			IStabilityRecipe r = (IStabilityRecipe) recipe;
			
			StringTextComponent text0 = (StringTextComponent) new StringTextComponent(" Extremely Stable ").mergeStyle(TextFormatting.DARK_GREEN);
			double recipeStab = r.getInherantStability();
			
			if (recipeStab <= 0.1) {
				text0 = (StringTextComponent) new StringTextComponent("Extremely Unstable").mergeStyle(TextFormatting.DARK_RED);
			} else if (recipeStab <= 0.25 && recipeStab > 0.1) {
				text0 = (StringTextComponent) new StringTextComponent("   Very Unstable  ").mergeStyle(TextFormatting.RED);
			} else if (recipeStab <= 0.5 && recipeStab > 0.25) {
				text0 = (StringTextComponent) new StringTextComponent("  Highly Unstable ").mergeStyle(TextFormatting.GOLD);
			} else if (recipeStab <= 0.75 && recipeStab > 0.5) {
				text0 = (StringTextComponent) new StringTextComponent("     Unstable     ").mergeStyle(TextFormatting.YELLOW);
			} else if (recipeStab <= 0.9 && recipeStab > 0.75) {
				text0 = (StringTextComponent) new StringTextComponent("   Mostly Stable  ").mergeStyle(TextFormatting.GREEN);
			} else if (recipeStab <= 0.99 && recipeStab > 0.9) {
				text0 = (StringTextComponent) new StringTextComponent("    Very Stable   ").mergeStyle(TextFormatting.DARK_GREEN);
			}
			
//			font.draw(matrix, text0, 5, -12, -1);
			font.drawText(matrix, text0, 28, -18, -1);
		}
	}
	
}
