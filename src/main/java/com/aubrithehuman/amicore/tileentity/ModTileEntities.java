package com.aubrithehuman.amicore.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.block.ModBlocks;
import com.aubrithehuman.amicore.client.tesr.TemperedJarRenderer;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModTileEntities {
	
	public static final List<Supplier<? extends TileEntityType<?>>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<TileEntityType<TemperedSpiritJarTileEntity>> TEMPERED_JAR = register("tempered_jar", TemperedSpiritJarTileEntity::new, () -> new Block[] { ModBlocks.TEMPERED_JAR.get() });
	
	@SubscribeEvent
	public void onRegisterTypes(RegistryEvent.Register<TileEntityType<?>> event) {
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

		ENTRIES.stream().map(Supplier::get).forEach(registry::register);
	}

	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup() {
		ClientRegistry.bindTileEntityRenderer(TEMPERED_JAR.get(), TemperedJarRenderer::new);
	}

	private static <T extends TileEntityType<?>> RegistryObject<T> register(String name, Supplier<TileEntity> tile, Supplier<Block[]> blocks) {
		ResourceLocation loc = new ResourceLocation(AMICore.MOD_ID, name);
		ENTRIES.add(() -> TileEntityType.Builder.of(tile, blocks.get()).build(null).setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.TILE_ENTITIES);
	}
}
