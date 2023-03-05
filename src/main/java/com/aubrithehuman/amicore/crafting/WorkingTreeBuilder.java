package com.aubrithehuman.amicore.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.openzen.zencode.java.ZenCodeType;

import com.aubrithehuman.amicore.crafting.WorkingTree.DrawableTypes;
import com.aubrithehuman.amicore.crafting.WorkingTree.GuiDirection;
import com.aubrithehuman.amicore.crafting.WorkingTree.Step;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

@ZenRegister
@ZenCodeType.Name("mods.amicore.WorkingTreeBuilder")
@Document("mods/amicore/WorkingTreeBuilder")
public class WorkingTreeBuilder {

	public static final ArrayList<WorkingTree> TREES = new ArrayList<>();
	
	public WorkingTree build;
	
	public WorkingTreeBuilder(String name) {
		build = new WorkingTree(name);
	}

	@ZenCodeType.Method
	public static void clearTrees() {
		TREES.clear();
	}

	@ZenCodeType.Method
    public static WorkingTreeBuilder create(String name, IIngredient root, int x, int y, boolean drawSlot) {
		WorkingTreeBuilder b = new WorkingTreeBuilder(name);
		b.addItem(root, x, y, drawSlot);
		return b;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addItem(IIngredient i, int x, int y, boolean drawSlot) {
		build.addStep(new Step<Ingredient>(toIngredient(i).get(0), x, y).setDrawSlot(drawSlot));
		return this;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addItemWithToolTip(IIngredient i, int x, int y, boolean drawSlot, String tooltip) {
		build.addStep(new Step<Ingredient>(toIngredient(i).get(0), x, y).setDrawSlot(drawSlot).addTooltip(tooltip));
		return this;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addStep(IIngredient i, int x, int y) {
		build.addStep(new Step<Ingredient>(toIngredient(i).get(0), x, y).setDrawSlot(false));
		return this;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addStepWithToolTip(IIngredient i, int x, int y, String tooltip) {
		build.addStep(new Step<Ingredient>(toIngredient(i).get(0), x, y).addTooltip(tooltip).setDrawSlot(false));
		return this;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addFluid(IFluidStack i, int x, int y, boolean drawSlot) {
		build.addStep(new Step<FluidStack>(i.getInternal(), x + 1, y + 1).setDrawSlot(drawSlot));
		return this;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addArrow(int x, int y, String dir) {
		return this.addDrawableObject("arrow", "none", x, y, dir);
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addLongArrow(int x, int y, String dir) {
		return this.addDrawableObject("long_arrow", "none", x, y, dir);
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addClockwiseArrow(int x, int y, String dir) {
		return this.addDrawableObject("clock_arrow", "none", x, y, dir);
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addAntiClockwiseArrow(int x, int y, String dir) {
		return this.addDrawableObject("anti_clock_arrow", "none", x, y, dir);
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addLine(int x, int y, String dir) {
		return this.addDrawableObject("line", "none", x, y, dir);
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder addDrawableObject(String d, String id, int x, int y, String dir) {
		if (dir == "up") {
			build.addStep(new Step<String>(d, x, y).addDirection(GuiDirection.UP).setDrawableType(DrawableTypes.getByName(id)));			
		} else if (dir == "down") {
			build.addStep(new Step<String>(d, x, y).addDirection(GuiDirection.DOWN).setDrawableType(DrawableTypes.getByName(id)));			
		} else if (dir == "left") {
			build.addStep(new Step<String>(d, x, y).addDirection(GuiDirection.LEFT).setDrawableType(DrawableTypes.getByName(id)));			
		} else if (dir == "right") {
			build.addStep(new Step<String>(d, x, y).addDirection(GuiDirection.RIGHT).setDrawableType(DrawableTypes.getByName(id)));			
		}
		return this;
	}
	
	@ZenCodeType.Method
    public WorkingTreeBuilder register() {
		TREES.add(build);
		return this;
	}
	
	private static NonNullList<Ingredient> toIngredient(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}
	
	
}
