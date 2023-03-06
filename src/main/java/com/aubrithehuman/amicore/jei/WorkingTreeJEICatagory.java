package com.aubrithehuman.amicore.jei;

import static net.minecraft.client.gui.AbstractGui.blit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.crafting.WorkingTree;
import com.aubrithehuman.amicore.crafting.WorkingTree.DrawableTypes;
import com.aubrithehuman.amicore.crafting.WorkingTree.Step;
import com.aubrithehuman.amicore.item.ModItems;
import com.blakebr0.cucumber.util.Localizable;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

public class WorkingTreeJEICatagory implements IRecipeCategory<WorkingTree> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(AMICore.MOD_ID, "textures/gui/aworkingtree_jei.png");
	private static final ResourceLocation ICONS = new ResourceLocation(AMICore.MOD_ID, "textures/gui/icons_workingtree_jei.png");
	public static final ResourceLocation UID = new ResourceLocation(AMICore.MOD_ID, "aworkingtree");

	private final IDrawable background;
	private final IDrawable icon;

	public WorkingTreeJEICatagory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 180, 180);
		this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.DUMMY_WORKINGTREE_ITEM.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<WorkingTree> getRecipeClass() {
		return WorkingTree.class;
	}

	@Override
	public String getTitle() {
		return Localizable.of("jei.category.amicore.aworkingtree").buildString();
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
	public List<ITextComponent> getTooltipStrings(WorkingTree recipe, double mouseX, double mouseY) {
		
		return Collections.emptyList();
	}
	
	@Override
    public void draw(WorkingTree recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        background.draw(matrixStack);
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer font = minecraft.font;

        minecraft.getTextureManager().bind(ICONS);
        for (Step s : recipe.getSteps()) {
        	if(s.getDisplayable() instanceof String) {
        		String key = (String) s.getDisplayable();
        		switch (key) {
	        		case "long_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 0, 36, 36, 256, 256);		
	        	        break;
	        		}
	        		case "clock_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 36, 36, 36, 256, 256);	  
	        	        break;
	        		}
	        		case "t_joint_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 72, 36, 36, 256, 256);	  
	        	        break;
	        		}
	        		case "anti_clock_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 108, 36, 36, 256, 256);	  
	        	        break;
	        		}
	        		case "line": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 144, 36, 36, 256, 256);	  
	        	        break;
	        		}
	        		case "arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 180, 36, 36, 256, 256);	  
	        	        break;
	        		}
	        		case "branch_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 18, 18, 0 + (s.getDirection().getLevel() * 36), 216, 36, 36, 256, 256);	  
	        	        break;
	        		}
	        		case "slot": {
	        	        blit(matrixStack, s.getX() , s.getY(), 190, 238, 18, 18, 256, 256);	  
	        	        break;
	        		}
	        		case "slot_medium": {
	        	        blit(matrixStack, s.getX() , s.getY(), 208, 234, 22, 22, 256, 256);	  
	        	        break;
	        		}
	        		case "slot_large": {
	        	        blit(matrixStack, s.getX() , s.getY(), 230, 230, 26, 26, 256, 256);	  
	        	        break;
	        		}
	        		case "fire": {
	        	        blit(matrixStack, s.getX() , s.getY(), 174, 240, 16, 16, 256, 256);	  
	        	        break;
	        		}
	        		case "tool": {
	        			DrawableTypes t = s.getDrawableType();
	        			int x = 180 + (16 * ((t.getID() - 1) % 4));
	        			int y = 88 + (16 * ((int) ((t.getID() - 1) / 4)));
	        	        blit(matrixStack, s.getX() , s.getY(), x, y, 16, 16, 256, 256);	  
	        	        break;
	        		}
	        		case "text": {
	        	        //blit(matrixStack, s.getX() , s.getY(), 174, 240, 16, 16, 256, 256);	  
	        	        StringTextComponent str = new StringTextComponent(s.getTooltip());
	        	        for(TextFormatting t : (List<TextFormatting>) s.getToolTipFormats()) {
	        	        	str.withStyle(t);
	        	        }
	        	        font.draw(matrixStack, str, s.getX(), s.getY(), 0);
	        	        break;
	        		}
	        		case "text_shadow": {
	        	        //blit(matrixStack, s.getX() , s.getY(), 174, 240, 16, 16, 256, 256);	  
	        	        StringTextComponent str = new StringTextComponent(s.getTooltip());
	        	        for(TextFormatting t : (List<TextFormatting>) s.getToolTipFormats()) {
	        	        	str.withStyle(t);
	        	        }
	        	        font.drawShadow(matrixStack, str, s.getX(), s.getY(), 0);
	        	        break;
	        		}
	        		default: {
	        			break;
	        		}
        		}
        	} else if(s.getDisplayable() instanceof Ingredient) {
        		if(s.getDrawSlot()) blit(matrixStack, s.getX(), s.getY(), 190, 238, 18, 18, 256, 256);        		
        	} else if (s.getDisplayable() instanceof FluidStack) {
        		if(s.getDrawSlot()) blit(matrixStack, s.getX() - 1, s.getY() - 1, 190, 238, 18, 18, 256, 256);
        	}
        }
        
    }
	
	private List<Pair<Object,Point>> stackmap;

	@Override
	public void setIngredients(WorkingTree recipe, IIngredients ingredients) {
		stackmap = new ArrayList<Pair<Object,Point>>();
		ItemStack out = ((Ingredient) recipe.getStep(0).getDisplayable()).getItems()[0];
		ingredients.setInput(VanillaTypes.ITEM, out);
		stackmap.add(new Pair<Object, Point>(out, new Point(recipe.getStep(0).getX(), recipe.getStep(0).getY())));
		List<ItemStack> items = new ArrayList<ItemStack>();
		List<FluidStack> fluids = new ArrayList<FluidStack>();
		for (int i = 1; i < recipe.getSteps().size(); i++) {
			Step s = recipe.getStep(i);
			if(s.getDisplayable() instanceof Ingredient) {
				ItemStack item = ((Ingredient) s.getDisplayable()).getItems()[0];
				items.add(item);
				stackmap.add(new Pair<Object, Point>(item, new Point(s.getX(), s.getY())));
			}
		}
		for (int i = 1; i < recipe.getSteps().size(); i++) {
			Step s = recipe.getStep(i);
			if (s.getDisplayable() instanceof FluidStack) {
				FluidStack f = (FluidStack) s.getDisplayable();
				fluids.add(f);
				stackmap.add(new Pair<Object, Point>(f, new Point(s.getX(), s.getY())));			
			}
		}
		ingredients.setOutputs(VanillaTypes.ITEM, items);	
		ingredients.setOutputs(VanillaTypes.FLUID, fluids);	
	}

	@Override
	public void setRecipe(IRecipeLayout layout, WorkingTree recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();
		IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();

		List<List<ItemStack>> outputsI = ingredients.getOutputs(VanillaTypes.ITEM);
		List<List<FluidStack>> outputsF = ingredients.getOutputs(VanillaTypes.FLUID);
		List<ItemStack> input = ingredients.getInputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, false, stackmap.get(0).getSecond().x, stackmap.get(0).getSecond().y);
		stacks.set(0, input);
		
		int i = 0;
		for (; i < outputsI.size(); i++) {
			stacks.init(i+1, true, stackmap.get(i+1).getSecond().x, stackmap.get(i+1).getSecond().y);
			stacks.set(i+1, outputsI.get(i));
		}
		
		int k = i + 1;
		i = 0;
		for (; i < outputsF.size(); i++) {
			fluidStacks.init(i + k, true, stackmap.get(i + k).getSecond().x, stackmap.get(i + k).getSecond().y, 16, 16, 1, false, null);
			fluidStacks.set(i + k, outputsF.get(i));
		}
	}


}
