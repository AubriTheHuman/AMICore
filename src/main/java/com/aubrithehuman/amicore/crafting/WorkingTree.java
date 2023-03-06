package com.aubrithehuman.amicore.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.text.TextFormatting;

public class WorkingTree {

		
	private final List<Step> steps;
	private final String id;
	
	public WorkingTree(String idIn) {
		steps = new ArrayList<Step>();
		id = idIn;
		// AMICore.LOGGER.debug("Registered Working tree for [item:\"" + d + "\", steps:" + m.size() + ", intermediates: " + m.size() + "]");
	}
	
	public String getId() {
		return id;
	}
	
	public List<Step> getSteps() {
		return steps;
	}
		
	public Step getStep(int i) {
		return this.steps.get(i);
	}
	
	public void addStep(Step p) {
		this.steps.add(p);
	}
	
	public void clearSteps() {
		this.steps.clear();
	}
	
	public static class Step<T> {
		
		String tooltip = "";
		List<TextFormatting> toolTipFormat = new ArrayList<TextFormatting>();
		final T display;
		final int x;
		final int y;
		GuiDirection direction = GuiDirection.NONE;
		boolean drawSlot = true;
		DrawableTypes drawableType = DrawableTypes.NONE;
		
		public Step(T dispIn, int xIn, int yIn) {
			display = dispIn;
			x = xIn;
			y = yIn;
		}
		
		public Step addTooltip(String tipIn) {
			this.tooltip = tipIn;
			return this;
		}
		
		public Step addDirection(GuiDirection dirIn) {
			this.direction = dirIn;
			return this;
		}
		
		public Step setDrawSlot(boolean draw) {
			this.drawSlot = draw;
			return this;
		}
		
		public Step setDrawableType(DrawableTypes type) {
			this.drawableType = type;
			return this;
		}
		
		public Step addTooltipFormatting(TextFormatting format) {
			this.toolTipFormat.add(format);
			return this;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public T getDisplayable() {
			return display;
		}
		
		public String getTooltip() {
			return tooltip;
		}
		
		public GuiDirection getDirection() {
			return direction;
		}
		
		public boolean getDrawSlot() {
			return drawSlot;
		}
		
		public DrawableTypes getDrawableType() {
			return drawableType;
		}

		public List<TextFormatting> getToolTipFormats() {
			return this.toolTipFormat;
		}
	}
	
	public static enum GuiDirection {
		NONE(0, 0),
		UP(0, 0),
		RIGHT(1, 90),
		DOWN(2, 180),
		LEFT(3, 270);
		
		int rotate;
		int level;
		
		private GuiDirection(int levelIn, int rotateIn) {
			rotate = rotateIn;
			level = levelIn;
		}
		
		public int getLevel() {
			return this.level;
		}
		
		public int getRotation() {
			return this.rotate;
		}
	}
	
	public static enum DrawableTypes {
		NONE(0, "none"),
		ATHAME(1, "athame"),
		BEAKER(2, "beaker"),
		BURNER(3, "burner"),
		CARVER(4, "carver"),
		CHISEL(5, "chisel"),
		COMPASS(6, "compass"),
		CUTTERS(7, "cutters"),
		CUTTING_BOARD(8, "cutting_board"),
		DRIVER(9, "driver"),
		FILE(10, "file"),
		FRAMING_HAMMER(11, "framing_hammer"),
		GEMCUTTER(12, "gemcutter"),
		GRIMOIRE(13, "grimoire"),
		GROOVER(14, "groover"),
		HAMMER(15, "hammer"),
		HANDSAW(16, "handsaw"),
		HATCHET(17, "hatchet"),
		KNIFE(18, "knife"),
		LENS(19, "lens"),
		MORTAR(20, "mortar"),
		NEEDLE(21, "needle"),
		PAINTBRUSH(22, "paintbrush"),
		PAN(23, "pan"),
		PENCIL(24, "pencil"),
		PLIERS(25, "pliers"),
		PUNCH(26, "punch"),
		QUILL(27, "quill"),
		RAZOR(28, "razor"),
		SHEARS(29, "shears"),
		SIFTER(30, "sifter"),
		SOLDERER(31, "solderer"),
		SPANNER(32, "spanner"),
		TROWEL(33, "trowel"),
		TSQUARE(34, "tsquare"),
		CRUSHER(35, "crusher"),
		PRESS(36, "press");
		
		int id;
		String name;
		
		private DrawableTypes(int idIn, String nameIn) {
			id = idIn;
			name = nameIn;
		}
		
		public int getID() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static DrawableTypes getByName(String s) {
			for(DrawableTypes t : DrawableTypes.values()) {
				if (t.name == s) return t;
			}
			return null;
		}
		
		public static DrawableTypes getByID(int i) {
			for(DrawableTypes t : DrawableTypes.values()) {
				if (t.id == i) return t;
			}
			return null;
		}
	}
}
