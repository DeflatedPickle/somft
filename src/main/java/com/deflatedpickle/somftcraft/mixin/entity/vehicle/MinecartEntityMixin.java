/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft.mixin.entity.vehicle;

import com.deflatedpickle.somftcraft.Impl;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin({
  MinecartEntity.class,
  CommandBlockMinecartEntity.class,
  ChestMinecartEntity.class,
  FurnaceMinecartEntity.class,
})
public abstract class MinecartEntityMixin {
  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    Impl.INSTANCE.pickupVehicle(
        Either.right(((AbstractMinecartEntity) (Object) this)), player, cir);
  }
}
