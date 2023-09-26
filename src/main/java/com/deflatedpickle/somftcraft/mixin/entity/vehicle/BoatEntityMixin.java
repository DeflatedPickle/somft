/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.vehicle;

import com.deflatedpickle.somftcraft.Impl;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin({BoatEntity.class, ChestBoatEntity.class})
public abstract class BoatEntityMixin {

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    Impl.INSTANCE.pickupVehicle(Either.left(((BoatEntity) (Object) this)), player, cir);
  }
}
