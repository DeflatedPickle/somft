/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity;

import java.util.function.Consumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
  @Inject(
      method =
          "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
      at = @At(value = "TAIL"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onCreate(
      ServerWorld world,
      @Nullable NbtCompound nbt,
      @Nullable Consumer<Entity> spawnConfig,
      BlockPos pos,
      SpawnReason reason,
      boolean alignPosition,
      boolean invertY,
      CallbackInfoReturnable<@Nullable Entity> cir,
      Entity entity) {
    if (nbt != null && nbt.contains("data")) {
      var compound = nbt.getCompound("data");
      var uuid = compound.getUuid("owner");
      var tamed = compound.getBoolean("tamed");

      if (entity instanceof TameableEntity tameableEntity) {
        tameableEntity.setOwnerUuid(uuid);
        tameableEntity.setTamed(tamed);
      } else if (entity instanceof FoxEntity foxEntity) {
        foxEntity.addTrustedUuid(uuid);
      }
    }
  }
}
