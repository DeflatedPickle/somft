/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ShoulderParrotFeatureRenderer;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(ShoulderParrotFeatureRenderer.class)
public abstract class ShoulderParrotFeatureRendererMixin {
  @Shadow @Final private ParrotEntityModel model;

  @Inject(
      method = "method_17958",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/render/entity/model/ParrotEntityModel;poseOnShoulder(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFFI)V"))
  public void somft$renderShoulderParrot(
      MatrixStack matrices,
      boolean bl,
      PlayerEntity playerEntity,
      NbtCompound nbtCompound,
      VertexConsumerProvider vertexConsumers,
      int i,
      float f,
      float g,
      float h,
      float j,
      EntityType type,
      CallbackInfo ci) {
    this.model.child = nbtCompound.getBoolean("child");
  }
}
