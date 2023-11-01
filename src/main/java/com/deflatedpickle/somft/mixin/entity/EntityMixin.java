/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity;

import com.deflatedpickle.somft.Impl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(Entity.class)
public abstract class EntityMixin {
  @Inject(method = "tick", at = @At("RETURN"))
  public void onTick(CallbackInfo info) {
    Impl.INSTANCE.entityFireSpread((Entity) (Object) this);
  }

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    if ((Object) this instanceof LivingEntity
        && Impl.INSTANCE.removeArrow((LivingEntity) (Object) this, player)) {
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }
}
