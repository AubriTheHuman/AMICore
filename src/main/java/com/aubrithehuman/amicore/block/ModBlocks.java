package com.aubrithehuman.amicore.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.item.ModItems;
import com.blakebr0.cucumber.item.BaseBlockItem;
import com.blakebr0.extendedcrafting.block.PedestalBlock;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	
	public static final Map<RegistryObject<Block>, Supplier<Block>> ENTRIES = new LinkedHashMap<>();

	public static final RegistryObject<Block> TEMPERED_JAR = register("tempered_jar", TemperedSpiritJar::new);
	
	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		ENTRIES.forEach((reg, block) -> {
			registry.register(block.get());
			reg.updateReference(registry);
		});
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, b -> () -> new BaseBlockItem(b.get(), p -> p.tab(AMICore.MACHINE_TAB)));
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block, Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item) {
		ResourceLocation loc = new ResourceLocation(AMICore.MOD_ID, name);
		RegistryObject<Block> reg = RegistryObject.of(loc, ForgeRegistries.BLOCKS);
		ENTRIES.put(reg, () -> block.get().setRegistryName(loc));
		ModItems.BLOCK_ENTRIES.add(() -> item.apply(reg).get().setRegistryName(loc));
		return reg;
	}
}
