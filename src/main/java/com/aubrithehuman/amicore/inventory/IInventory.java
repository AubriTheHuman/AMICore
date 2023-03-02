package com.aubrithehuman.amicore.inventory;

import net.minecraft.item.ItemStack;

public interface IInventory {

	 public abstract ItemStack getInventory();

	 public void setInventory(ItemStack item);
	 
}
