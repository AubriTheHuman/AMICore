package com.aubrithehuman.amicore.item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.config.AMIConfig;
import com.aubrithehuman.amicore.malum.MalumSpiritAdditons;
import com.blakebr0.cucumber.item.BaseItem;
import com.sammy.malum.MalumMod;
import com.sammy.malum.common.items.SpiritItem;
import com.sammy.malum.core.init.items.MalumItems;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AMICore.MOD_ID);
	public static final DeferredRegister<Item> MALUM_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MalumMod.MODID);
	
	//Spaghettificate
	public static final List<Supplier<Item>> BLOCK_ENTRIES = new ArrayList<>();
	public static final Map<RegistryObject<Item>, Supplier<Item>> ENTRIES = new LinkedHashMap<>();
	
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
	public static final RegistryObject<Item> DUMMY_BIO_ITEM = ITEMS.register("dummy_bio_item", 
			() -> new Item(new Item.Properties()));		
	public static final RegistryObject<Item> DUMMY_PETRO_ITEM = ITEMS.register("dummy_petro_item", 
			() -> new Item(new Item.Properties()));	
	public static final RegistryObject<Item> DUMMY_INTERMEDIATES_ITEM = ITEMS.register("dummy_intermediates_item", 
			() -> new Item(new Item.Properties()));	
	public static final RegistryObject<Item> DUMMY_TOOLS_ITEM = ITEMS.register("dummy_tools_item", 
			() -> new Item(new Item.Properties()));	
	public static final RegistryObject<Item> DUMMY_WORKINGTREE_ITEM = ITEMS.register("dummy_workingtree_item", 
			() -> new Item(new Item.Properties()));	
	
	//Malum spirits
    public static final RegistryObject<Item> MALICIOUS_SPIRIT = MALUM_ITEMS.register("malicious_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.MALICIOUS_SPIRIT));
    public static final RegistryObject<Item> MAGNETIC_SPIRIT = MALUM_ITEMS.register("magnetic_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.MAGNETIC_SPIRIT));
    public static final RegistryObject<Item> RUINOUS_SPIRIT = MALUM_ITEMS.register("ruinous_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.RUINOUS_SPIRIT));
    public static final RegistryObject<Item> TEMPESTUOUS_SPIRIT = MALUM_ITEMS.register("tempestuous_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.TEMPESTUOUS_SPIRIT));
    public static final RegistryObject<Item> ELECTRIC_SPIRIT = MALUM_ITEMS.register("electric_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.ELECTRIC_SPIRIT));
    public static final RegistryObject<Item> MECHANICAL_SPIRIT = MALUM_ITEMS.register("mechanical_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.MECHANICAL_SPIRIT));
    public static final RegistryObject<Item> HERBAL_SPIRIT = MALUM_ITEMS.register("herbal_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.HERBAL_SPIRIT));
    
    public static final RegistryObject<Item> VOIDIAL_SPIRIT = MALUM_ITEMS.register("voidial_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.VOIDIAL_SPIRIT));
    public static final RegistryObject<Item> DEATHLY_SPIRIT = MALUM_ITEMS.register("deathly_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.DEATHLY_SPIRIT));
    public static final RegistryObject<Item> ASTRAL_SPIRIT = MALUM_ITEMS.register("astral_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.ASTRAL_SPIRIT));
    public static final RegistryObject<Item> CORPOREAL_SPIRIT = MALUM_ITEMS.register("corporeal_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.CORPOREAL_SPIRIT));
    public static final RegistryObject<Item> CHAOTIC_SPIRIT = MALUM_ITEMS.register("chaotic_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.CHAOTIC_SPIRIT));
    public static final RegistryObject<Item> FROZEN_SPIRIT = MALUM_ITEMS.register("frozen_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.FROZEN_SPIRIT));
    public static final RegistryObject<Item> HUNGRY_SPIRIT = MALUM_ITEMS.register("hungry_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.HUNGRY_SPIRIT));
    
    public static final RegistryObject<Item> RADIATIVE_SPIRIT = MALUM_ITEMS.register("radiative_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.RADIATIVE_SPIRIT));
    public static final RegistryObject<Item> THOUGHTFUL_SPIRIT = MALUM_ITEMS.register("thoughtful_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.THOUGHTFUL_SPIRIT));
    public static final RegistryObject<Item> ENLIGHTENED_SPIRIT = MALUM_ITEMS.register("enlightened_spirit", () -> new SpiritItem(MalumItems.SPLINTER_PROPERTIES(), MalumSpiritAdditons.ENLIGHTENED_SPIRIT));
    
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
		if(AMIConfig.doMalum.get()) MALUM_ITEMS.register(eventBus);
		
	}
	
	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		BLOCK_ENTRIES.stream().map(Supplier::get).forEach(registry::register);
		ENTRIES.forEach((reg, item) -> {
			registry.register(item.get());
			reg.updateReference(registry);
		});
	}

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(AMICore.MACHINE_TAB)));
	}

	private static RegistryObject<Item> register(String name, Supplier<Item> item) {
		ResourceLocation loc = new ResourceLocation(AMICore.MOD_ID, name);
		RegistryObject<Item> reg = RegistryObject.of(loc, ForgeRegistries.ITEMS);
		ENTRIES.put(reg, () -> item.get().setRegistryName(loc));
		return reg;
	}
}
