/* Copyright (c) 2023-2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import com.deflatedpickle.somft.api.Milkable;
import com.deflatedpickle.somft.api.Timer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends AnimalEntity implements Milkable, Timer {
  protected CowEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
    super(entityType, world);
  }

  @Redirect(
      method = "interactMob",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CowEntity;isBaby()Z"))
  public boolean somft$interactMob$INVOKE$isBaby(CowEntity instance) {
    return !this.somft$isMilkable();
  }

  @Inject(method = "interactMob", at = @At(value = "RETURN", ordinal = 0))
  public void somft$interatMob$RETURN$0(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    this.somft$setMilked(true);
  }

  @Override
  public void tickMovement() {
    if (this.getWorld().isClient) {
      this.somft$setTimer(Math.max(0, this.somft$getTimer() - 1));
    }

    super.tickMovement();
  }
}
