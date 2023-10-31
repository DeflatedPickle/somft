/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.render.entity;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
  @Shadow private boolean ready;

  @Shadow private BlockView area;

  @Shadow private Entity focusedEntity;

  @Shadow private boolean thirdPerson;

  @Shadow
  protected abstract void setRotation(float yaw, float pitch);

  @Shadow
  public abstract void moveBy(double x, double y, double z);

  @Shadow
  protected abstract void setPos(double x, double y, double z);

  @Shadow private float lastCameraY;

  @Shadow private float cameraY;

  @Shadow
  protected abstract double clipToSpace(double desiredCameraDistance);

  @Inject(method = "update", at = @At("HEAD"), cancellable = true)
  public void onUpdate(
      BlockView area,
      Entity focusedEntity,
      boolean thirdPerson,
      boolean inverseView,
      float tickDelta,
      CallbackInfo ci) {
    this.ready = true;
    this.area = area;
    this.focusedEntity = focusedEntity;
    this.thirdPerson = thirdPerson;

    if (thirdPerson) {
      this.setRotation(-45, -25f);
      this.setPos(
          MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()),
          MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY())
              + (double) MathHelper.lerp(tickDelta, this.lastCameraY, this.cameraY),
          MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
      this.moveBy(-this.clipToSpace(4.0), 0.2, 0.0);
      ci.cancel();
    }
  }
}
