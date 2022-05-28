package com.aubrithehuman.amicore.item;

import com.aubrithehuman.amicore.AMICore;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AMICore.MOD_ID);
	
	public static final RegistryObject<Item> DUMMY_ORE_ITEM = ITEMS.register("dummy_ore_item", 
			() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> DUMMY_METAL_ITEM = ITEMS.register("dummy_metal_item", 
			() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> DUMMY_MACHINE_ITEM = ITEMS.register("dummy_machine_item", 
			() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> DUMMY_FLUID_ITEM = ITEMS.register("dummy_fluid_item", 
			() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> DUMMY_MATERIAL_ITEM = ITEMS.register("dummy_material_item", 
			() -> new Item(new Item.Properties()));
				
	 
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
