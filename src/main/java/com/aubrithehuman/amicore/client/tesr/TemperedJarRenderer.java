package com.aubrithehuman.amicore.client.tesr;

import com.aubrithehuman.amicore.tileentity.TemperedSpiritJarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class TemperedJarRenderer extends TileEntityRenderer<TemperedSpiritJarTileEntity> {
	
	public TemperedJarRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}



	private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
	    renderer.pos(stack.getLast().getMatrix(), x, y, z)
	            .color(r, g, b, a)
	            .tex(u, v)
	            .lightmap(0, 240)
	            .normal(1, 0, 0)
	            .endVertex();
	}
	
	@Override
	public void render(TemperedSpiritJarTileEntity tile, float v, MatrixStack matrix, IRenderTypeBuffer buffer, int i, int i1) {
		if(tile == null || tile.isRemoved()) return;
		
		Minecraft minecraft = Minecraft.getInstance();
		ItemStack stack = tile.getInventory().getStackInSlot(0);
		if (!stack.isEmpty()) {
			matrix.pushPose();
			matrix.translate(0.5D, 0.3D, 0.5D);
			float scale = stack.getItem() instanceof BlockItem ? 0.9F : 1.0F;
			matrix.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800.0D;
			matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			matrix.mulPose(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
			minecraft.getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, i, i1, matrix, buffer);
			matrix.popPose();
			
			matrix.pushPose();
			
			matrix.popPose();
		}
//		
//		FluidStack fluid = tileEntityIn.getTank().getFluid();
//		if (fluid == null) return;
//		
//		Fluid renderFluid = fluid.getFluid();
//		if (renderFluid == null) return;
//		
//		FluidAttributes attributes = renderFluid.getAttributes();
//        ResourceLocation fluidStill = attributes.getStillTexture(fluid);
//        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStill);
//		
		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
		
//		float scale = (1.0f - TANK_THICKNESS/2 - TANK_THICKNESS) * fluid.getAmount() / (tileEntityIn.getTank().getCapacity());
		
        Quaternion rotation = Vector3f.YP.rotationDegrees(0);
        
        int color = renderFluid.getAttributes().getColor();
        
        float a = 1.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
		
		matrix.pushPose();
        matrix.translate(.5, 0, .5);
        matrix.rotate(rotation);
        if(scale == 0.330f) { matrix.translate(0, -.1, 0); matrix.scale(.6f, scale + 0.110f, .6f);  } else if(scale == 0.440f) { matrix.translate(0, -.2, 0); matrix.scale(.6f, scale + 0.110f, .6f); } else if(scale == 0.550f) { matrix.translate(0, -.4, 0); matrix.scale(.6f, scale + 0.210f, .6f); } else { matrix.scale(.6f, scale, .6f); }
        matrix.translate(-.5, scale, -.5);
        
        // Top Face
        add(builder, matrix, 1 - .8f, 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Bottom Face of Top
        add(builder, matrix, 0 + .8f, 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Front Faces [NORTH - SOUTH]
        add(builder, matrix, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        
        add(builder, matrix, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Back Faces
        add(builder, matrix, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

        add(builder, matrix, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        matrix.rotate(Vector3f.YP.rotationDegrees(90));
        matrix.translate(-1f, 0, 0);
        // Front Faces [EAST - WEST]
        add(builder, matrix, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

        add(builder, matrix, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        
        // Back Faces
        add(builder, matrix, 0 + .8f, 0 + 1, .2f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .2f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .2f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 1 - 1, .2f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);

        add(builder, matrix, 0 + .8f, 1 - 1, .8f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 1 - 1, .8f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
        add(builder, matrix, 1 - .8f, 0 + 1, .8f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
        add(builder, matrix, 0 + .8f, 0 + 1, .8f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
		matrix.pop();
	}
			
}
