/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.ai.goal;

import com.deflatedpickle.somft.api.PetLogic;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin {
  @Shadow @Final protected PathAwareEntity mob;

  @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
  public void onCanStart(CallbackInfoReturnable<Boolean> cir) {
    if (mob instanceof TameableEntity tameable) {
      var owner = tameable.getOwner();
      var logic = ((PetLogic) tameable).somft$getAttackLogic();
      if (owner != null) {
        if (logic != PetLogic.Attack.ATTACK) {
          cir.setReturnValue(false);
        }
      }
    }
  }
}
