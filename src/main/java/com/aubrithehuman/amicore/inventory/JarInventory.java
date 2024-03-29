package com.aubrithehuman.amicore.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class JarInventory extends ItemStackHandler {
	
    private final Runnable onContentsChanged;
    private final Map<Integer, Integer> slotSizeMap;
    private BiFunction<Integer, ItemStack, Boolean> slotValidator = null;
    private int maxStackSize = 64;
    private int[] outputSlots = null;

    public JarInventory() {
        this(64, null);
    }

    public JarInventory(int size, Runnable onContentsChanged) {
        super(size);
        this.onContentsChanged = onContentsChanged;
        this.slotSizeMap = new HashMap<>();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (this.outputSlots != null && ArrayUtils.contains(this.outputSlots, slot))
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.outputSlots != null && !ArrayUtils.contains(this.outputSlots, slot))
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.slotSizeMap.containsKey(slot) ? this.slotSizeMap.get(slot) : this.maxStackSize;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.slotValidator == null || this.slotValidator.apply(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.onContentsChanged != null)
            this.onContentsChanged.run();
    }

    public ItemStack insertItemSuper(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack extractItemSuper(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

    public int[] getOutputSlots() {
        return this.outputSlots;
    }

    public void setDefaultSlotLimit(int size) {
        this.maxStackSize = size;
    }

    public void addSlotLimit(int slot, int size) {
        this.slotSizeMap.put(slot, size);
    }

    public void setSlotValidator(BiFunction<Integer, ItemStack, Boolean> validator) {
        this.slotValidator = validator;
    }

    public void setOutputSlots(int... slots) {
        this.outputSlots = slots;
    }

    public IInventory toIInventory() {
        return new Inventory(this.stacks.toArray(new ItemStack[0]));
    }
    
    /**
     * Finds the last slot in the inventory with an item in it.
     * @return
     */
    public int getLastFilledSlot() {
    	for(int i = this.getSlots() - 1; i >= 0; i--) {
    		if(!this.getStackInSlot(i).isEmpty()) 
    	    	return i;
    	}
    	return 0;
    }
    
    public ItemStack insertStackInLastSlot(ItemStack stack) {
    	    	
    	int lastFilled = getLastFilledSlot();
    	ItemStack lastStack = this.getStackInSlot(lastFilled);
    	
    	if(lastStack.isEmpty()) {
    		this.setStackInSlot(lastFilled, stack);
    	}
    	
    	//add to last existing stack if space
    	if(lastStack.getCount() < 64) {
    		//how much do we need to get to 64 items
    		int neededDifference = 64 - lastStack.getCount();

    		//combine the two stacks and if this is 1 then the new size of last stack is 64, else its the remainder
    		int full = (lastStack.getCount() + stack.getCount()) / 64;
    		//combine the two stacks and the remainder is the new size of insert stack
    		int remain = (lastStack.getCount() + stack.getCount()) % 64;
    		
    		
    		lastStack.setCount(full >= 1 ? 64 : remain);
    		
    		//then subtract the difference from the input stack, if its now less than 0 delete it
    		stack.setCount(stack.getCount() - neededDifference);
    		if(stack.getCount() <= 0) {
    			stack = ItemStack.EMPTY;
    		}
    	}
    	
    	int slots = this.getSlots();
    	//are we at the last slot, if not then add the remaining stack in the next slot
    	if(!(lastFilled >= slots - 1) && !stack.isEmpty()) {
    		this.setStackInSlot(lastFilled + 1, stack.copy());
    		stack = ItemStack.EMPTY;
    	}
    	
    	return stack;
    }
    
    public int getTotal() {
    	int count = 0;
    	for (int i = getLastFilledSlot(); i >= 0; i--) {
    		count += this.getStackInSlot(i).getCount();
    	}
    	return count;
    }

    /**
     * Creates a deep copy of this BaseItemStackHandler, including new copies of the items
     * @return the copy of this BaseItemStackHandler
     */
    public JarInventory copy() {
    	JarInventory newInventory = new JarInventory(this.getSlots(), this.onContentsChanged);

        newInventory.setDefaultSlotLimit(this.maxStackSize);
        newInventory.setSlotValidator(this.slotValidator);
        newInventory.setOutputSlots(this.outputSlots);

        this.slotSizeMap.forEach(newInventory::addSlotLimit);

        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            newInventory.setStackInSlot(i, stack.copy());
        }

        return newInventory;
    }
}
