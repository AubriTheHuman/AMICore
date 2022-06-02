package com.aubrithehuman.amicore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aubrithehuman.amicore.config.AMIConfig;
import com.aubrithehuman.amicore.item.ModItems;
import com.aubrithehuman.amicore.malum.MalumSpiritAdditons;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("amicore")
public class AMICore
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "amicore";
    
    public static final ItemGroup ORE_TAB = new OreTab();
	public static final ItemGroup METAL_TAB = new MetalTab();
	public static final ItemGroup MACHINE_TAB = new MachineTab();
	public static final ItemGroup MATERIAL_TAB = new MaterialTab();
	public static final ItemGroup FLUID_TAB = new FluidTab();
	public static final ItemGroup BIO_TAB = new BioTab();
	public static final ItemGroup PETRO_TAB = new PetroTab();
	public static final ItemGroup INTERMEDIATES_TAB = new IntermediatesTab();
	public static final ItemGroup TOOLS_TAB = new ToolsTab();
	
//	public static void register() {
//		ORE_TAB = new OreTab();
//		METAL_TAB = new MetalTab();
//		MACHINE_TAB = new MachineTab();
//		FLUID_TAB = new FluidTab();
//		MATERIAL_TAB = new MaterialTab();
//		BIO_TAB = new BioTab();
//	}
	
	

    public AMICore()
    {
    	
    	
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
    	ModItems.register(eventBus);
    	MalumSpiritAdditons.register();
    	
    	init();
    	
    	ModLoadingContext.get().registerConfig(Type.COMMON, AMIConfig.SPEC, "amicore-common.toml");
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    	LOGGER.info("AMICore Loaded");
    }
    
    public static void init() {

		LOGGER.info("AMICore Init");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    	
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
//        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.messageSupplier().get()).
//                collect(Collectors.toList()));
    }

    public static class OreTab extends ItemGroup {
		public OreTab() {
			super("amicore.oretab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_ORE_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class MetalTab extends ItemGroup {
		public MetalTab() {
			super("amicore.metaltab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_METAL_ITEM.get().getDefaultInstance();
		}
	};
		
	public static class MachineTab extends ItemGroup {
		public MachineTab() {
			super("amicore.machinetab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_MACHINE_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class FluidTab extends ItemGroup {
		public FluidTab() {
			super("amicore.fluidtab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_FLUID_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class MaterialTab extends ItemGroup {
		public MaterialTab() {
			super("amicore.materialtab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_MATERIAL_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class BioTab extends ItemGroup {
		public BioTab() {
			super("amicore.biotab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_BIO_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class PetroTab extends ItemGroup {
		public PetroTab() {
			super("amicore.petrotab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_PETRO_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class IntermediatesTab extends ItemGroup {
		public IntermediatesTab() {
			super("amicore.intermediatestab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_INTERMEDIATES_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class ToolsTab extends ItemGroup {
		public ToolsTab() {
			super("amicore.toolstab");
		}

		@Override
		public ItemStack makeIcon() {
			return ModItems.DUMMY_TOOLS_ITEM.get().getDefaultInstance();
		}
	};

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
//            LOGGER.info("HELLO from Register Block");
        	
        }
        
        
           
    }
}
