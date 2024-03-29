package com.aubrithehuman.amicore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aubrithehuman.amicore.block.ModBlocks;
import com.aubrithehuman.amicore.config.AMIConfig;
import com.aubrithehuman.amicore.crafting.StabilityObject;
import com.aubrithehuman.amicore.crafting.StabilityRegistry;
import com.aubrithehuman.amicore.item.ModItems;
import com.aubrithehuman.amicore.malum.MalumSpiritAdditons;
import com.aubrithehuman.amicore.tileentity.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
	
	
//	Codec<Block> BLOCK_CODEC = ResourceLocation.CODEC.xmap(ForgeRegistries.BLOCKS::getValue, Block::getRegistryName); 
	// DATASET
	public static final StabilityRegistry<StabilityObject> STABILITY_OBJECTS = new StabilityRegistry<StabilityObject>("stabilityobjects", StabilityObject.CODEC, AMICore.LOGGER);
		
	
	
	@SubscribeEvent
	public void onAddReloadListeners(final AddReloadListenerEvent event)
	{
		LOGGER.debug("Reloading Stability Obejects!");
		event.addListener(STABILITY_OBJECTS);
	}
			  


    public AMICore()
    {
    	
    	
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
    	ModItems.register(eventBus);
    	MalumSpiritAdditons.register();
    	
    	init();
    	reorganizeTabs();
    	
    	ModLoadingContext.get().registerConfig(Type.COMMON, AMIConfig.SPEC, "amicore-common.toml");
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
     
        //Using Cucumber/Extended Crafting registry system
        FMLJavaModLoadingContext.get().getModEventBus().register(new ModBlocks());
        FMLJavaModLoadingContext.get().getModEventBus().register(new ModItems());
        FMLJavaModLoadingContext.get().getModEventBus().register(new ModTileEntities());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    	MinecraftForge.EVENT_BUS.addListener( this::onAddReloadListeners );
        

    	LOGGER.info("AMICore Loaded");
    }
    
    private void reorganizeTabs() {
//    	ItemGroup[] old = ItemGroup.TABS.clone();
//    	ItemGroup[] curr = new ItemGroup[9]; 
//    	int[] currIndexes = new int[] { ORE_TAB.getId(), METAL_TAB.getId(), MACHINE_TAB.getId(), MATERIAL_TAB.getId(), FLUID_TAB.getId(), BIO_TAB.getId(), PETRO_TAB.getId(), INTERMEDIATES_TAB.getId(), TOOLS_TAB.getId() };
//    	ItemGroup[] move = new ItemGroup[9]; 
//    	int[] moveIndexes = new int[9];
//    	ItemGroup[] news = old.clone();
//    	
//    	System.out.println(curr.toString());
//    	System.out.println(currIndexes.toString());
//    	System.out.println(old.toString());
//    	if(old.length <= 21) return;
//    	for(int i = 12; i < 21; i++) {
//    		move[i-12] = old[i];
//    		moveIndexes[i-12] = old[i].getId();
//    	}
//    	System.out.println(move.toString());
//    	System.out.println(moveIndexes.toString());
//    	
//    	for(int i = 0; i < currIndexes.length; i++) {
//    		curr[i] = old[currIndexes[i]];
//    	}
//
//    	System.out.println(curr.toString());
//    	System.out.println(currIndexes.toString());
//    	
//    	for(int i = 0; i < moveIndexes.length; i++) {
//    		news[currIndexes[i]] = move[i];
//    		news[i+12] = curr[i];
//    	}
//    	System.out.println(news.toString());
//    		
//    	ItemGroup.TABS = news;
	
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
    
    @SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		ModTileEntities.onClientSetup();
		ModBlocks.onClientSetup();
	}

    public static class OreTab extends ItemGroup {
		public OreTab() {
			super("amicore.oretab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_ORE_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class MetalTab extends ItemGroup {
		public MetalTab() {
			super("amicore.metaltab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_METAL_ITEM.get().getDefaultInstance();
		}
	};
		
	public static class MachineTab extends ItemGroup {
		public MachineTab() {
			super("amicore.machinetab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_MACHINE_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class FluidTab extends ItemGroup {
		public FluidTab() {
			super("amicore.fluidtab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_FLUID_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class MaterialTab extends ItemGroup {
		public MaterialTab() {
			super("amicore.materialtab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_MATERIAL_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class BioTab extends ItemGroup {
		public BioTab() {
			super("amicore.biotab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_BIO_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class PetroTab extends ItemGroup {
		public PetroTab() {
			super("amicore.petrotab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_PETRO_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class IntermediatesTab extends ItemGroup {
		public IntermediatesTab() {
			super("amicore.intermediatestab");
		}

		@Override
		public ItemStack createIcon() {
			return ModItems.DUMMY_INTERMEDIATES_ITEM.get().getDefaultInstance();
		}
	};
	
	public static class ToolsTab extends ItemGroup {
		public ToolsTab() {
			super("amicore.toolstab");
		}

		@Override
		public ItemStack createIcon() {
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
