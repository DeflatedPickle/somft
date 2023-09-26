/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(SweetBerryBushBlock.class)
public abstract class SweetBerryBushBlockMixin {
  @Redirect(
      method = "onEntityCollision",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
  public boolean onEntityCollision(Entity instance, DamageSource source, float amount) {
    return Impl.INSTANCE.redirectSweetBerryBushDamage(instance, source, amount);
  }
}
