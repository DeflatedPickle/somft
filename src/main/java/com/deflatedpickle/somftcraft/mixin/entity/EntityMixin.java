/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft.mixin.entity;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(Entity.class)
public abstract class EntityMixin {
  @Inject(method = "tick", at = @At("RETURN"))
  public void onTick(CallbackInfo info) {
    Impl.INSTANCE.entityFireSpread((Entity) (Object) this);
  }
}
