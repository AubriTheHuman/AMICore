package com.aubrithehuman.amicore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.aubrithehuman.amicore.crafting.IStabilityRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe.Serializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

@Mixin(Serializer.class)
public class MixinCombinationRecipeSeiralizer {
	
	@Overwrite(remap = false)
	public CombinationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		NonNullList<Ingredient> inputs = NonNullList.create();
		Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
		inputs.add(input);

		JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
		for (int i = 0; i < ingredients.size(); i++) {
			Ingredient ingredient = Ingredient.deserialize(ingredients.get(i));
			inputs.add(ingredient);
		}

		ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		if (!json.has("powerCost"))
			throw new JsonSyntaxException("Missing powerCost for combination crafting recipe");
		int powerCost = JSONUtils.getInt(json, "powerCost");
		int powerRate = JSONUtils.getInt(json, "powerRate", ModConfigs.CRAFTING_CORE_POWER_RATE.get());
		double stability = JSONUtils.getFloat(json, "stability");
		
		CombinationRecipe r = new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		((IStabilityRecipe) r).setInherantStability(stability);
		
		return r;
	}

	@Overwrite(remap = false)
	public CombinationRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
		int size = buffer.readVarInt();

		NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
		for (int i = 0; i < size; i++) {
			inputs.set(i, Ingredient.read(buffer));
		}

		ItemStack output = buffer.readItemStack();
		int powerCost = buffer.readVarInt();
		int powerRate = buffer.readVarInt();
		double stability = buffer.readDouble();		
		
		CombinationRecipe r = new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		((IStabilityRecipe) r).setInherantStability(stability);
		
		return r;
	}

	@Overwrite(remap = false)
	public void toNetwork(PacketBuffer buffer, CombinationRecipe recipe) {
		buffer.writeVarInt(recipe.getIngredients().size());

		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredient.write(buffer);
		}

		buffer.writeItemStack(recipe.getRecipeOutput());
		buffer.writeVarInt(recipe.getPowerCost());
		buffer.writeVarInt(recipe.getPowerRate());
		buffer.writeDouble(((IStabilityRecipe) recipe).getInherantStability());
	}
}