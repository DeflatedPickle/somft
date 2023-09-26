/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {
  @Shadow @Final protected PathAwareEntity mob;

  @Redirect(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/control/LookControl;lookAt(Lnet/minecraft/entity/Entity;FF)V"))
  public void lookAt(LookControl instance, Entity entity, float yawSpeed, float pitchSpeed) {
    if (this.mob instanceof AnimalEntity animal) {
      if (animal.getBreedingAge() == 0 && !animal.isInLove()) {
        instance.lookAt(entity, yawSpeed, pitchSpeed);
      }
    } else {
      instance.lookAt(entity, yawSpeed, pitchSpeed);
    }
  }

  @Redirect(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(Lnet/minecraft/entity/Entity;D)Z"))
  public boolean startMovingTo(EntityNavigation instance, Entity entity, double speed) {
    if (this.mob instanceof AnimalEntity animal) {
      if (animal.getBreedingAge() == 0 && !animal.isInLove()) {
        return instance.startMovingTo(entity, speed);
      }
    } else {
      return instance.startMovingTo(entity, speed);
    }

    return false;
  }
}
