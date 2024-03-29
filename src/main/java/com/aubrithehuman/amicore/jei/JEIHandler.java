package com.aubrithehuman.amicore.jei;

import javax.annotation.Nonnull;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.block.ModBlocks;
import com.aubrithehuman.amicore.crafting.WorkingTreeBuilder;
import com.aubrithehuman.amicore.item.ModItems;
import com.sammy.malum.core.init.items.MalumItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIHandler implements IModPlugin
{
    private static final ResourceLocation ID = new ResourceLocation(AMICore.MOD_ID, "main");
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
         registry.addRecipeCategories(new WorkingTreeJEICatagory(registry.getJeiHelpers().getGuiHelper()));
         registry.addRecipeCategories(new StabilityObjectsJEICatagory(registry.getJeiHelpers().getGuiHelper()));
    }
    
    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {
        registry.addRecipes(WorkingTreeBuilder.TREES, WorkingTreeJEICatagory.UID);
        registry.addRecipes(AMICore.STABILITY_OBJECTS.getData(), StabilityObjectsJEICatagory.UID);
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
//        registry.addRecipeCatalyst(new ItemStack(ModItems.DUMMY_WORKINGTREE_ITEM.get()), WorkingTreeJEICatagory.UID);
        registry.addRecipeCatalyst(new ItemStack(com.blakebr0.extendedcrafting.init.ModBlocks.CRAFTING_CORE.get()), StabilityObjectsJEICatagory.UID);
        registry.addRecipeCatalyst(new ItemStack(com.blakebr0.extendedcrafting.init.ModBlocks.PEDESTAL.get()), StabilityObjectsJEICatagory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.TEMPERED_JAR.get()), StabilityObjectsJEICatagory.UID);
        registry.addRecipeCatalyst(new ItemStack(MalumItems.STAINED_SPIRIT_RESONATOR.get()), StabilityObjectsJEICatagory.UID);
    }
    
    @Nonnull
    @Override
    public ResourceLocation getPluginUid()
    {
        return ID;
    }
}