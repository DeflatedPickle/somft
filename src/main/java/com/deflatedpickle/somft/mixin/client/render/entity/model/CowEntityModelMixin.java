/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.model;

import com.deflatedpickle.somft.api.EatAngles;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"unused", "UnusedMixin", "rawtypes", "unchecked"})
@Mixin(CowEntityModel.class)
public abstract class CowEntityModelMixin extends QuadrupedEntityModel {
  @Unique private float headPitchModifier;

  protected CowEntityModelMixin(
      ModelPart root,
      boolean headScaled,
      float childHeadYOffset,
      float childHeadZOffset,
      float invertedChildHeadScale,
      float invertedChildBodyScale,
      int childBodyYOffset) {
    super(
        root,
        headScaled,
        childHeadYOffset,
        childHeadZOffset,
        invertedChildHeadScale,
        invertedChildBodyScale,
        childBodyYOffset);
  }

  @Override
  public void animateModel(Entity entity, float limbAngle, float limbDistance, float tickDelta) {
    super.animateModel(entity, limbAngle, limbDistance, tickDelta);

    if (entity instanceof EatAngles eatEntity) {
      this.head.pivotY = 6.0F + eatEntity.somft$getNeckAngle(tickDelta) * 9.0F;
      this.headPitchModifier = eatEntity.somft$getHeadAngle(tickDelta);
    }
  }

  @Override
  public void setAngles(
      Entity entity,
      float limbAngle,
      float limbDistance,
      float animationProgress,
      float headYaw,
      float headPitch) {
    super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
    this.head.pitch = this.headPitchModifier;
  }
}
