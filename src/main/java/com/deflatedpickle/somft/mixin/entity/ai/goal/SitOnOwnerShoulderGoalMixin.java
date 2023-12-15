/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.ai.goal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.ai.goal.SitOnOwnerShoulderGoal;
import net.minecraft.entity.passive.TameableShoulderEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(SitOnOwnerShoulderGoal.class)
public abstract class SitOnOwnerShoulderGoalMixin {
  @Shadow @Final private TameableShoulderEntity tameable;

  @ModifyReturnValue(method = "canStart", at = @At("RETURN"))
  public boolean somft$canStart(boolean original) {
    return original && !this.tameable.isInLove();
  }
}
