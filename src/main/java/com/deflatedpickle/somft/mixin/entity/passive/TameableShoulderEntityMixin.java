/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(TameableShoulderEntity.class)
public abstract class TameableShoulderEntityMixin extends TameableEntity {

  protected TameableShoulderEntityMixin(
      EntityType<? extends TameableEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(
      method = "mountOnto",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/passive/TameableShoulderEntity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;",
              shift = At.Shift.BEFORE),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$mountOnto(
      ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir, NbtCompound nbtCompound) {
    nbtCompound.putBoolean("child", this.dataTracker.get(CHILD));
  }
}
