package com.aubrithehuman.amicore.client.tesr;

import com.aubrithehuman.amicore.tileentity.TemperedSpiritJarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class TemperedJarRenderer extends TileEntityRenderer<TemperedSpiritJarTileEntity> {
	
	public TemperedJarRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(TemperedSpiritJarTileEntity tile, float v, MatrixStack matrix, IRenderTypeBuffer buffer, int i, int i1) {
		Minecraft minecraft = Minecraft.getInstance();
		ItemStack stack = tile.getInventory().getStackInSlot(0);
		if (!stack.isEmpty()) {
			matrix.pushPose();
			matrix.translate(0.5D, 0.4D, 0.5D);
			float scale = stack.getItem() instanceof BlockItem ? 0.9F : 0.65F;
			matrix.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800.0D;
			matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			matrix.mulPose(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
			minecraft.getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, i, i1, matrix, buffer);
			matrix.popPose();
		}
	}
}
