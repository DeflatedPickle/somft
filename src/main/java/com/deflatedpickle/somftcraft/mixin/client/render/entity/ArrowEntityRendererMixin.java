/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.render.entity;

import static net.minecraft.client.render.entity.MobEntityRenderer.renderLeashPiece;

import com.deflatedpickle.somftcraft.api.Leashable;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("UnusedMixin")
@Mixin(ArrowEntityRenderer.class)
public abstract class ArrowEntityRendererMixin extends ProjectileEntityRenderer<ArrowEntity> {

  public ArrowEntityRendererMixin(EntityRendererFactory.Context context) {
    super(context);
  }

  @Override
  public void render(
      ArrowEntity persistentProjectileEntity,
      float f,
      float g,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i) {
    super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    var entity = ((Leashable) persistentProjectileEntity).somft$getHoldingEntity();
    if (entity != null) {
      this.renderLeash(persistentProjectileEntity, g, matrixStack, vertexConsumerProvider, entity);
    }
  }

  @Unique
  private <E extends Entity> void renderLeash(
      ArrowEntity entity,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider provider,
      E holdingEntity) {
    matrices.push();
    Vec3d vec3d = holdingEntity.getLeashHoldPosition(tickDelta);
    double d =
        (double)
                (MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())
                    * (float) (Math.PI / 180.0))
            + (Math.PI / 2);
    Vec3d vec3d2 = entity.getLeashOffset(tickDelta);
    double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
    double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
    double g = MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) + e;
    double h = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + vec3d2.y;
    double i = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) + f;
    matrices.translate(e, vec3d2.y, f);
    float j = (float) (vec3d.x - g);
    float k = (float) (vec3d.y - h);
    float l = (float) (vec3d.z - i);
    float m = 0.025F;
    VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.getLeash());
    Matrix4f matrix4f = matrices.peek().getModel();
    float n = MathHelper.inverseSqrt(j * j + l * l) * m / 2.0F;
    float o = l * n;
    float p = j * n;
    BlockPos blockPos = BlockPos.fromPosition(entity.getCameraPosVec(tickDelta));
    BlockPos blockPos2 = BlockPos.fromPosition(holdingEntity.getCameraPosVec(tickDelta));
    int q = this.getBlockLight(entity, blockPos);
    int r = this.dispatcher.getRenderer(holdingEntity).getBlockLight(holdingEntity, blockPos2);
    int s = entity.getWorld().getLightLevel(LightType.SKY, blockPos);
    int t = entity.getWorld().getLightLevel(LightType.SKY, blockPos2);

    for (int u = 0; u <= 24; ++u) {
      renderLeashPiece(
          vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025F, 0.025F, o, p, u, false);
    }

    for (int u = 24; u >= 0; --u) {
      renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025F, 0.0F, o, p, u, true);
    }

    matrices.pop();
  }
}
