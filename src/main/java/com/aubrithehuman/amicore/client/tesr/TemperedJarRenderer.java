package com.aubrithehuman.amicore.client.tesr;

import com.aubrithehuman.amicore.AMICore;
import com.aubrithehuman.amicore.inventory.JarInventory;
import com.aubrithehuman.amicore.tileentity.TemperedSpiritJarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sammy.malum.common.items.SpiritItem;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

public class TemperedJarRenderer extends TileEntityRenderer<TemperedSpiritJarTileEntity> {
	
	public TemperedJarRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}



	private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
	    renderer.applyBakedLighting(255, null);
	    renderer.pos(stack.getLast().getMatrix(), x, y, z)
	            .color(r, g, b, a)
	            .tex(u, v)
//	            .overlayCoords(0, 240)
	            .normal(1, 0, 0)
	            .endVertex();
	   
	    
	}
	
	@Override
	public void render(TemperedSpiritJarTileEntity tile, float v, MatrixStack matrix, IRenderTypeBuffer buffer, int i, int i1) {
		if(tile == null || tile.isRemoved()) return;
		
		Minecraft minecraft = Minecraft.getInstance();
		ItemStack stack = tile.getInventory().getStackInSlot(0);
		if (!stack.isEmpty()) {
			matrix.push();
			matrix.translate(0.5D, 0.2D, 0.5D);
			float scale = stack.getItem() instanceof BlockItem ? 0.9F : 1.4F;
			matrix.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800.0D;
			matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			matrix.rotate(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
			minecraft.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, i, i1, matrix, buffer);
			matrix.pop();
			
			
			//render fill fluid
	        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(Fluids.WATER.getFluid().getAttributes().getStillTexture());
			IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
			
			float scale2 = ((JarInventory) tile.getInventory()).getTotal() / (64f*64f);
			
//			System.out.println("render Jar");
	        
	        int color = 16777215;
	        if(stack.getItem() instanceof SpiritItem) {
	        	color = ((SpiritItem) stack.getItem()).getColor().getRGB();
	        }
	        
	        float a = 1.0F;
	        float r = (color >> 16 & 0xFF) / 255.0F;
	        float g = (color >> 8 & 0xFF) / 255.0F;
	        float b = (color & 0xFF) / 255.0F;
			
			matrix.push();
	        matrix.translate(.5, 0, .5);
	        
	        //rotation?
	        Quaternion rotation = Vector3f.YP.rotationDegrees(0);
	        matrix.rotate(rotation);
	        
	        
	        if(scale2 <= 0.1f) { 
	        	scale2 = 0.1f;  
        	} else if(scale2 >= 1f) { 
        		scale2 = 1.0f;  
			}
	        
	        scale2 *= 0.75;
	        
	        matrix.translate(-0.5, 0.04f, -0.5);
	        
	        // Top Face
	        add(builder, matrix, 1 - .84f, scale2, 0 + .84f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, 0 + .84f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, 1 - .84f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, 1 - .84f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        
	        // Bottom Face of Top
	        add(builder, matrix, 0 + .84f, scale2, 0 + .84f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, 0 + .84f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, 1 - .84f , sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, 1 - .84f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        
	        // Front Faces [NORTH - SOUTH]
	        add(builder, matrix, 0 + .84f, scale2, .84f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .84f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .84f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, 1 - 1, .84f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        
	        add(builder, matrix, 0 + .84f, 1 - 1, .16f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .16f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .16f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, .16f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        
	        // Back Faces
	        add(builder, matrix, 0 + .84f, scale2, .16f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .16f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .16f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, 1 - 1, .16f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

	        add(builder, matrix, 0 + .84f, 1 - 1, .84f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .84f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .84f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, .84f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        
	        rotation = Vector3f.YP.rotationDegrees(90);
	        matrix.rotate(rotation);
	        matrix.translate(-1f, 0, 0);
	        // Front Faces [EAST - WEST]
	        add(builder, matrix, 0 + .84f, scale2, .84f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .84f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .84f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, 1 - 1, .84f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

	        add(builder, matrix, 0 + .84f, 1 - 1, .16f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .16f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .16f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, .16f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        
	        // Back Faces
	        add(builder, matrix, 0 + .84f, scale2, .16f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .16f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .16f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, 1 - 1, .16f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

	        add(builder, matrix, 0 + .84f, 1 - 1, .84f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, 1 - 1, .84f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
	        add(builder, matrix, 1 - .84f, scale2, .84f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
	        add(builder, matrix, 0 + .84f, scale2, .84f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
	        
	        matrix.pop();
		}
	}
			
}
