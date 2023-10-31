/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends LivingEntity {
  protected HorseEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
  public void onInteractMob(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    if (Impl.INSTANCE.removeArrow(this, player)) {
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }
}
