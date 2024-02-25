/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.model;

import com.deflatedpickle.somft.api.EatAngles;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"unused", "UnusedMixin", "rawtypes", "unchecked", "SpellCheckingInspection"})
@Mixin(ChickenEntityModel.class)
public abstract class ChickenEntityModelMixin extends AnimalModel {
  @Shadow @Final private ModelPart head;
  @Shadow @Final private ModelPart wattle;
  @Shadow @Final private ModelPart beak;
  @Unique private float headPitchModifier;

  @Override
  public void animateModel(Entity entity, float limbAngle, float limbDistance, float tickDelta) {
    super.animateModel(entity, limbAngle, limbDistance, tickDelta);

    if (entity instanceof EatAngles eatEntity) {
      this.head.pivotY =
          this.beak.pivotY =
              this.wattle.pivotY = 15.0F + eatEntity.somft$getNeckAngle(tickDelta) * 4.0F;
      this.headPitchModifier = eatEntity.somft$getHeadAngle(tickDelta);
    }
  }

  @Inject(method = "setAngles", at = @At("TAIL"))
  public void somft$setAngles(
      Entity entity,
      float limbAngle,
      float limbDistance,
      float animationProgress,
      float headYaw,
      float headPitch,
      CallbackInfo ci) {
    this.head.pitch = this.beak.pitch = this.wattle.pitch = this.headPitchModifier;
  }
}
