package com.aubrithehuman.amicore.jei;

import javax.annotation.Nonnull;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.crafting.WorkingTreeBuilder;
import com.aubrithehuman.amicore.item.ModItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
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
    }
    
    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {
        registry.addRecipes(WorkingTreeBuilder.TREES, WorkingTreeJEICatagory.UID);
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        registry.addRecipeCatalyst(new ItemStack(ModItems.DUMMY_WORKINGTREE_ITEM.get()), WorkingTreeJEICatagory.UID);
    }
    
    @Nonnull
    @Override
    public ResourceLocation getPluginUid()
    {
        return ID;
    }
}