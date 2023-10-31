/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.ai.goal;

import com.deflatedpickle.somftcraft.api.PetLogic;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.passive.TameableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(FollowOwnerGoal.class)
public abstract class FollowOwnerGoalMixin {
  @Shadow @Final private TameableEntity tameable;

  @Shadow private LivingEntity owner;

  @Shadow @Final private float maxDistance;

  @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
  public void onCanStart(CallbackInfoReturnable<Boolean> cir) {
    var owner = this.tameable.getOwner();
    var logic = ((PetLogic) this.tameable).somft$getMovementLogic();
    if (owner != null && (logic == PetLogic.Movement.STAY || logic == PetLogic.Movement.WANDER)) {
      cir.setReturnValue(false);
    }
  }

  @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
  public void onShouldContinue(CallbackInfoReturnable<Boolean> cir) {
    var owner = this.tameable.getOwner();
    var logic = ((PetLogic) this.tameable).somft$getMovementLogic();
    if (owner != null) {
      if (logic == PetLogic.Movement.FOLLOW) {
        cir.setReturnValue(true);
      } else if (logic == PetLogic.Movement.FOLLOW_DISTANCED) {
        cir.setReturnValue(
            this.tameable.squaredDistanceTo(this.owner)
                >= (double) (this.maxDistance / 2 * this.maxDistance / 2));
      } else if (logic == PetLogic.Movement.FOLLOW_IN_RANGE) {
        cir.setReturnValue(
            !(this.tameable.squaredDistanceTo(this.owner)
                <= (double) (this.maxDistance * this.maxDistance)));
      } else {
        cir.setReturnValue(false);
      }
    }
  }
}
