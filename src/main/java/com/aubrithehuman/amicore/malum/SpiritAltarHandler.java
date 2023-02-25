package com.aubrithehuman.amicore.malum;

import java.util.ArrayList;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.sammy.malum.common.items.SpiritItem;
import com.sammy.malum.core.modcontent.MalumSpiritAltarRecipes;
import com.sammy.malum.core.modcontent.MalumSpiritAltarRecipes.MalumSpiritAltarRecipe;
import com.sammy.malum.core.systems.recipes.ItemIngredient;
import com.sammy.malum.core.systems.recipes.SimpleItemIngredient;
import com.sammy.malum.core.systems.recipes.SpiritIngredient;

@ZenRegister
@ZenCodeType.Name("mods.amicore.SpiritAltarHandler")
@Document("mods/amicore/SpiritAltarHandler")
public class SpiritAltarHandler {

	public static final ArrayList<MalumSpiritAltarRecipe> RECIPES = new ArrayList<>();
		
	@ZenCodeType.Method
    public static void addRecipe(String name, IItemStack output, IItemStack mainInput, IItemStack[] extraItems, IItemStack[] spiritTypes) {
		
        MalumSpiritAltarRecipe builder = new MalumSpiritAltarRecipe(new ItemIngredient(output.getDefinition(), output.getAmount()), new ItemIngredient(mainInput.getDefinition(), mainInput.getAmount()));
    
        for (int i = 0; i < spiritTypes.length; i++) {
        	try {
        		builder.addSpirit(new SpiritIngredient(((SpiritItem) spiritTypes[i].getDefinition()).type, spiritTypes[i].getAmount()));
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        	}
        }
        
        for (int i = 0; i < spiritTypes.length; i++) {
        	try {
        		builder.addExtraItem(new SimpleItemIngredient(extraItems[i].getDefinition()));
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        	}
        }
        
        RECIPES.add(builder);
        
	}
	
	@ZenCodeType.Method
    public static void clearMalumRecipes() {
		MalumSpiritAltarRecipes.RECIPES.clear();
	}
	
	@ZenCodeType.Method
    public static void regenMalumRecipes() {
		MalumSpiritAltarRecipes.init();
	}
	
}
