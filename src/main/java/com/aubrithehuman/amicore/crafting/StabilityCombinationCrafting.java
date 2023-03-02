package com.aubrithehuman.amicore.crafting;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.openzen.zencode.java.ZenCodeType;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CombinationCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

@ZenCodeType.Name("mods.amicore.StabilityCombinationCrafting")
@ZenRegister
@Document("mods/amicore/StabilityCombinationCrafting")
public class StabilityCombinationCrafting {

	@ZenCodeType.Method
	public static void addStabilityRecipe(String id, IItemStack output, int cost, IIngredient[] inputs, int perTick, double stability) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				CombinationRecipe recipe = new CombinationRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), cost, perTick);
				((IStabilityRecipe) recipe).setInherantStability(stability);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Stability Combination Crafting recipe for " + output.getCommandString();
			}
		});
	}
	
	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}
	
}
