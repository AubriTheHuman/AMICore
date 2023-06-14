 package com.aubrithehuman.amicore.mixin;

import static com.sammy.malum.network.NetworkManager.INSTANCE;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.sammy.malum.MalumHelper;
import com.sammy.malum.common.blocks.spiritaltar.IAltarProvider;
import com.sammy.malum.common.blocks.spiritaltar.SpiritAltarTileEntity;
import com.sammy.malum.core.init.MalumSounds;
import com.sammy.malum.core.modcontent.MalumSpiritAltarRecipes.MalumSpiritAltarRecipe;
import com.sammy.malum.core.systems.inventory.SimpleInventory;
import com.sammy.malum.core.systems.recipes.SpiritIngredient;
import com.sammy.malum.core.systems.tileentities.SimpleTileEntity;
import com.sammy.malum.network.packets.particle.altar.SpiritAltarConsumeParticlePacket;
import com.sammy.malum.network.packets.particle.altar.SpiritAltarCraftParticlePacket;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

@Mixin(SpiritAltarTileEntity.class)
public abstract class MixinSpiritAltarTileEntity extends SimpleTileEntity implements ITickableTileEntity {

	
	public MixinSpiritAltarTileEntity(TileEntityType<?> type) {
		super(type);
	}


	@Shadow
	public int soundCooldown;
	@Shadow
    public int progress;
	@Shadow
    public boolean spedUp;
	@Shadow
    public int spinUp;
	@Shadow
    public float spin;
	@Shadow
    public SimpleInventory inventory;
	@Shadow
    public SimpleInventory extrasInventory;
	@Shadow
    public SimpleInventory spiritInventory;
	@Shadow
    public MalumSpiritAltarRecipe recipe;

	
//	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	@Overwrite
	public void tick() {
		if (soundCooldown > 0)
        {
            soundCooldown--;
        }
        if (recipe != null)
        {
            int spinCap = spedUp ? 30 : 10;
            if (spinUp < spinCap)
            {
                spinUp++;
            }
            if (MalumHelper.areWeOnServer(level))
            {
                if (soundCooldown == 0)
                {
                    level.playSound(null, worldPosition, MalumSounds.ALTAR_LOOP, SoundCategory.BLOCKS, 1, 1f);
                    soundCooldown = 180;
                }
                ItemStack stack = inventory.getStackInSlot(0);
//                System.out.println(stack.toString());
                progress++;
                int progressCap = spedUp ? 60 : 360;
                if (progress >= progressCap)
                {
                    Vector3d itemPos = SpiritAltarTileEntity.itemPos(this);

                    int extras = extrasInventory.nonEmptyItems();
                    if (extras != recipe.extraItemIngredients.size())
                    {
                        progress *= 0.5f;
                        int horizontal = 4;
                        int vertical = 2;
                        Collection<BlockPos> nearbyBlocks = MalumHelper.getBlocks(worldPosition, horizontal, vertical, horizontal);
                        for (BlockPos pos : nearbyBlocks)
                        {
                            if (level.getBlockEntity(pos) instanceof IAltarProvider)
                            {
                                IAltarProvider tileEntity = (IAltarProvider) level.getBlockEntity(pos);
                                ItemStack providedStack = tileEntity.providedInventory().getStackInSlot(0);
                                //CHANGED
//                                System.out.println("doing-changes");
                                if(recipe.extraItemIngredients.size() > 0) {
                                	if (recipe.extraItemIngredients.get(extrasInventory.nonEmptyItems()).matches(providedStack))
                                    {
                                        level.playSound(null, pos, MalumSounds.ALTAR_CONSUME, SoundCategory.BLOCKS, 1, 0.9f + level.random.nextFloat() * 0.2f);
                                        Vector3d providedItemPos = tileEntity.providedItemPos();
                                        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(()->level.getChunkAt(pos)), SpiritAltarConsumeParticlePacket.fromIngredients(providedStack, recipe.spiritIngredients, providedItemPos.x,providedItemPos.y,providedItemPos.z, itemPos.x,itemPos.y,itemPos.z));
                                        extrasInventory.playerInsertItem(level, providedStack.split(1));
                                        MalumHelper.updateAndNotifyState(level, pos);
                                        MalumHelper.updateAndNotifyState(level, this.worldPosition);

                                        break;
                                    }
                                }
                            }
                        }
                        return;
                    }
                    for (SpiritIngredient ingredient : recipe.spiritIngredients)
                    {
                        for (int i = 0; i < spiritInventory.slotCount; i++)
                        {
                            ItemStack spiritStack = spiritInventory.getStackInSlot(i);
                            if (ingredient.matches(spiritStack))
                            {
                                spiritStack.shrink(ingredient.count);
                                break;
                            }
                        }
                    }
                    INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(()->level.getChunkAt(worldPosition)), SpiritAltarCraftParticlePacket.fromIngredients(recipe.spiritIngredients, itemPos.x, itemPos.y, itemPos.z));
                    stack.shrink(recipe.inputIngredient.count);
                    ItemEntity entity = new ItemEntity(level, itemPos.x, itemPos.y, itemPos.z, recipe.outputIngredient.getItem());
                    level.addFreshEntity(entity);
                    progress = 0;
                    extrasInventory.clearItems();
                    level.playSound(null, worldPosition, MalumSounds.ALTAR_CRAFT, SoundCategory.BLOCKS, 1, 0.9f + level.random.nextFloat() * 0.2f);

                    //CHANGED
//                    System.out.println("doing-changes");
                    if (recipe != null && (!recipe.matches(spiritInventory.nonEmptyStacks()) || stack.isEmpty()))
                    {
                        recipe = null;
                    }
                    MalumHelper.updateAndNotifyState(level, worldPosition);
                }

            }
        }
        else
        {
            progress = 0;            if (spinUp > 0)
            {
                spinUp--;
            }
            spedUp = false;
        }
        if (MalumHelper.areWeOnClient(level))
        {
            passiveParticles();
        }
	}
	
	
	@Shadow
	public abstract void passiveParticles();
}
