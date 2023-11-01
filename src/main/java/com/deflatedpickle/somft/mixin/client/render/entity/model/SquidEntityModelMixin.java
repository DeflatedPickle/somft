/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.model;

import com.deflatedpickle.somft.api.DoesAge;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SquidEntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(SquidEntityModel.class)
public class SquidEntityModelMixin {
  @Shadow @Final private ModelPart root;
  @Unique private final float babyScale = 0.6f;

  @Inject(
      method = "setAngles",
      at =
          @At(
              value = "FIELD",
              target = "Lnet/minecraft/client/model/ModelPart;pitch:F",
              shift = At.Shift.AFTER),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void resizeTentacles(
      Entity entity,
      float limbAngle,
      float limbDistance,
      float animationProgress,
      float headYaw,
      float headPitch,
      CallbackInfo ci,
      ModelPart[] modelParts,
      int var8,
      int var9,
      ModelPart modelPart) {
    if (((DoesAge) entity).somft$isBaby()) {
      modelPart.scaleX = babyScale;
      modelPart.scaleY = babyScale;
      modelPart.scaleZ = babyScale;
    } else {
      modelPart.scaleX = 1f;
      modelPart.scaleY = 1f;
      modelPart.scaleZ = 1f;
    }
  }

  @Inject(method = "setAngles", at = @At("TAIL"))
  public void resizeBody(
      Entity entity,
      float limbAngle,
      float limbDistance,
      float animationProgress,
      float headYaw,
      float headPitch,
      CallbackInfo ci) {
    if (((DoesAge) entity).somft$isBaby()) {
      root.scaleX = babyScale;
      root.scaleY = babyScale;
      root.scaleZ = babyScale;
    } else {
      root.scaleX = 1f;
      root.scaleY = 1f;
      root.scaleZ = 1f;
    }
  }
}
