/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.vehicle;

import com.deflatedpickle.somftcraft.Impl;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(HopperMinecartEntity.class)
public abstract class HopperMinecartEntityMixin extends StorageMinecartEntity {
  protected HopperMinecartEntityMixin(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  @Override
  public ActionResult interact(PlayerEntity player, Hand hand) {
    return Impl.INSTANCE.pickupVehicle(Either.right(this), player, null);
  }
}
