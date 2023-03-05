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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class WorkingTreeJEICatagory implements IRecipeCategory<WorkingTree> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(AMICore.MOD_ID, "textures/gui/workingtree_jei.png");
	public static final ResourceLocation UID = new ResourceLocation(AMICore.MOD_ID, "workingtree");

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
		return Localizable.of("jei.category.amicore.workingtree").buildString();
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

        minecraft.getTextureManager().bind(TEXTURE);
        for (Step s : recipe.getSteps()) {
        	if(s.getDisplayable() instanceof String) {
        		String key = (String) s.getDisplayable();
        		switch (key) {
	        		case "line": {
	        	        blit(matrixStack, s.getX() , s.getY(), 226, 0 + (s.getDirection().getLevel() * 15), 15, 15, 256, 256);	  
	        	        break;
	        		}
	        		case "arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 241, 0 + (s.getDirection().getLevel() * 15), 15, 15, 256, 256);	  
	        	        break;
	        		}
	        		case "long_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 0 + (s.getDirection().getLevel() * 26), 216, 26, 26, 256, 256);	  
	        	        break;
	        		}
	        		case "clock_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 204, 0 + (s.getDirection().getLevel() * 22), 22, 22, 256, 256);	  
	        	        break;
	        		}
	        		case "anti_clock_arrow": {
	        	        blit(matrixStack, s.getX() , s.getY(), 182, 0 + (s.getDirection().getLevel() * 22), 22, 22, 256, 256);	  
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
	        		case "tool": {
	        			DrawableTypes t = s.getDrawableType();
	        			int x = 180 + (16 * ((t.getID() - 1) % 4));
	        			int y = 88 + (16 * ((int) ((t.getID() - 1) / 4)));
	        	        blit(matrixStack, s.getX() , s.getY(), x, y, 16, 16, 256, 256);	  
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
		ingredients.setOutput(VanillaTypes.ITEM, out);
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
		ingredients.setInputs(VanillaTypes.ITEM, items);	
		ingredients.setInputs(VanillaTypes.FLUID, fluids);	
	}

	@Override
	public void setRecipe(IRecipeLayout layout, WorkingTree recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();
		IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<FluidStack>> inputsF = ingredients.getInputs(VanillaTypes.FLUID);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, false, stackmap.get(0).getSecond().x, stackmap.get(0).getSecond().y);
		stacks.set(0, outputs);
		
		int i = 0;
		for (; i < inputs.size(); i++) {
			stacks.init(i+1, true, stackmap.get(i+1).getSecond().x, stackmap.get(i+1).getSecond().y);
			stacks.set(i+1, inputs.get(i));
		}
		
		int k = i + 1;
		i = 0;
		for (; i < inputsF.size(); i++) {
			fluidStacks.init(i + k, true, stackmap.get(i + k).getSecond().x, stackmap.get(i + k).getSecond().y, 16, 16, 1, false, null);
			fluidStacks.set(i + k, inputsF.get(i));
		}
	}


}
