/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("UnusedMixin")
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin {
  @Shadow public BipedEntityModel.ArmPose rightArmPose;

  @Shadow @Final public ModelPart rightArm;

  @Shadow @Final public ModelPart head;

  @Shadow @Final public ModelPart leftArm;

  /*  @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
  public void onSetAngles(
      LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
    this.head.yaw = 0f;
  }*/

  /*@Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
  public void onPositionRightArm(LivingEntity entity, CallbackInfo ci) {
    if (this.rightArmPose == BipedEntityModel.ArmPose.EMPTY) {
      this.rightArm.yaw = -0.1F + this.head.yaw - 0.4F;
      this.rightArm.pitch = (float) (-Math.PI) + this.head.pitch;

      // this.rightArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
      ci.cancel();
    }
  }*/

  /*@Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
  public void onPositionLeftArm(LivingEntity entity, CallbackInfo ci) {
    if (this.rightArmPose == BipedEntityModel.ArmPose.EMPTY) {
      this.leftArm.yaw = -(-0.1F + this.head.yaw - 0.4F);
      this.leftArm.pitch = (float) (-Math.PI) + this.head.pitch;

      // this.leftArm.pitch = (float) (-Math.PI / 2) + this.head.pitch;
      ci.cancel();
    }
  }*/
}
