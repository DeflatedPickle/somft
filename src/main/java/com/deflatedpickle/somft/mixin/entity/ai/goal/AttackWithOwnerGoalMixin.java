/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.ai.goal;

import com.deflatedpickle.somft.api.PetLogic;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.passive.TameableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(AttackWithOwnerGoal.class)
public abstract class AttackWithOwnerGoalMixin {
  @Shadow @Final private TameableEntity tameable;

  @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
  public void onCanStart(CallbackInfoReturnable<Boolean> cir) {
    var owner = this.tameable.getOwner();
    var logic = ((PetLogic) this.tameable).somft$getAttackLogic();
    if (owner != null && logic != PetLogic.Attack.GUARD) {
      cir.setReturnValue(false);
    }
  }
}
