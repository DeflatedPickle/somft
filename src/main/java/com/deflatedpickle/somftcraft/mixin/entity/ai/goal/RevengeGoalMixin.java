/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.ai.goal;

import com.deflatedpickle.somftcraft.api.PetLogic;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(RevengeGoal.class)
public abstract class RevengeGoalMixin extends TrackTargetGoal {

  public RevengeGoalMixin(MobEntity mob, boolean checkVisibility, boolean checkNavigable) {
    super(mob, checkVisibility, checkNavigable);
  }

  @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
  public void onCanStart(CallbackInfoReturnable<Boolean> cir) {
    if (this.mob instanceof TameableEntity tameable) {
      var owner = tameable.getOwner();
      var logic = ((PetLogic) tameable).somft$getHurtLogic();
      if (owner != null) {
        if (logic == PetLogic.Hurt.FLEE) {
          cir.setReturnValue(false);
        }
      }
    }
  }
}
