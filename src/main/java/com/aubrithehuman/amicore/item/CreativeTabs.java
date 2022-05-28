package com.aubrithehuman.amicore.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabs {
	
	public static final ItemGroup ORE_TAB = new ItemGroup("oretab") {
		
		@Override
		public ItemStack makeIcon() {
			// TODO Auto-generated method stub
			return new ItemStack(ModItems.DUMMY_ORE_ITEM.get());
		}
	};
	
	public static final ItemGroup METALS_TAB = new ItemGroup("metaltab") {
		
		@Override
		public ItemStack makeIcon() {
			// TODO Auto-generated method stub
			return new ItemStack(ModItems.DUMMY_METAL_ITEM.get());
		}
	};
	
	public static final ItemGroup MACHINE_TAB = new ItemGroup("machinetab") {
		
		@Override
		public ItemStack makeIcon() {
			// TODO Auto-generated method stub
			return new ItemStack(ModItems.DUMMY_MACHINE_ITEM.get());
		}
	};
	
	public static final ItemGroup FLUIDS_TAB = new ItemGroup("fluidtab") {
		
		@Override
		public ItemStack makeIcon() {
			// TODO Auto-generated method stub
			return new ItemStack(ModItems.DUMMY_FLUID_ITEM.get());
		}
	};
	
	public static final ItemGroup MATERIALS_TAB = new ItemGroup("materialtab") {
		
		@Override
		public ItemStack makeIcon() {
			// TODO Auto-generated method stub
			return new ItemStack(ModItems.DUMMY_MATERIAL_ITEM.get());
		}
	};

}
