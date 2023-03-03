package com.aubrithehuman.amicore.malum;

import java.util.ArrayList;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.sammy.malum.common.items.SpiritItem;
import com.sammy.malum.core.modcontent.MalumRuneTableRecipes;
import com.sammy.malum.core.modcontent.MalumRuneTableRecipes.MalumRuneTableRecipe;
import com.sammy.malum.core.modcontent.MalumSpiritAltarRecipes;
import com.sammy.malum.core.modcontent.MalumSpiritAltarRecipes.MalumSpiritAltarRecipe;
import com.sammy.malum.core.systems.recipes.ItemIngredient;
import com.sammy.malum.core.systems.recipes.SimpleItemIngredient;
import com.sammy.malum.core.systems.recipes.SpiritIngredient;

@ZenRegister
@ZenCodeType.Name("mods.amicore.RuneTableHandler")
@Document("mods/amicore/RuneTableHandler")
public class RuneTableHandler {

	public static final ArrayList<MalumRuneTableRecipe> RECIPES = new ArrayList<>();
		
	@ZenCodeType.Method
    public static void addRecipe(String name, IItemStack output, IItemStack[] extraItems) {
		
		MalumRuneTableRecipe builder = new MalumRuneTableRecipe(new ItemIngredient(output.getDefinition(), output.getAmount()));
         
        for (int i = 0; i < extraItems.length; i++) {
        	try {
        		builder.addExtraItem(new ItemIngredient(extraItems[i].getDefinition(), extraItems[i].getAmount()));
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        	}
        }
        
        RECIPES.add(builder);
        
	}
	
	@ZenCodeType.Method
    public static void clearMalumRecipes() {
		MalumRuneTableRecipes.RECIPES.clear();
	}
	
	@ZenCodeType.Method
    public static void regenMalumRecipes() {
		MalumRuneTableRecipes.init();
	}
	
	@ZenCodeType.Method
    public static void removeMalumRecipeByIndex(int i) {
		MalumRuneTableRecipes.RECIPES.remove(i);
	}
	
}
