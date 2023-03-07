package com.aubrithehuman.amicore.jei;

import java.util.Collections;
import java.util.List;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.crafting.StabilityObject;
import com.blakebr0.cucumber.util.Localizable;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.sammy.malum.core.init.items.MalumItems;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

public class StabilityObjectsJEICatagory implements IRecipeCategory<StabilityObject> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(AMICore.MOD_ID, "textures/gui/stability_objects_jei.png");
	public static final ResourceLocation UID = new ResourceLocation(AMICore.MOD_ID, "stabilityobject"); 

	private final IDrawable background;
	private final IDrawable icon;

	public StabilityObjectsJEICatagory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 24);
		this.icon = helper.createDrawableIngredient(new ItemStack(MalumItems.STAINED_SPIRIT_RESONATOR.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<StabilityObject> getRecipeClass() {
		return StabilityObject.class;
	}

	@Override
	public String getTitle() {
		return Localizable.of("jei.category.amicore.stabilityobjects").buildString();
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public List<ITextComponent> getTooltipStrings(StabilityObject recipe, double mouseX, double mouseY) {
		
		return Collections.emptyList();
	}
	
	@Override
    public void draw(StabilityObject recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
		background.draw(matrixStack);
		
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer font = minecraft.font;
        
        StringTextComponent s = new StringTextComponent("Max " + recipe.getMaximum() + ", " + (recipe.stability_factor > 0 ? "+" : "-" ) + recipe.stability_factor + " Each");
        s.withStyle(TextFormatting.WHITE);
        font.drawShadow(matrixStack, s, 28, 7, 0);
         
    }
	
	@Override
	public void setIngredients(StabilityObject recipe, IIngredients ingredients) {
		ItemStack out = new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(recipe.block)).asItem());
		ingredients.setInput(VanillaTypes.ITEM, out);
	}
	
	@Override
	public void setRecipe(IRecipeLayout layout, StabilityObject recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();
		List<ItemStack> input = ingredients.getInputs(VanillaTypes.ITEM).get(0);
		stacks.init(0, true, 3, 3);
		stacks.set(0, input);
		
	}
}
