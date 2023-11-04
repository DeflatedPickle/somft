/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin {
  @Redirect(
      method = "appendTooltip",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/nbt/NbtCompound;contains(Ljava/lang/String;I)Z",
              ordinal = 1))
  public boolean appendTooltip(NbtCompound instance, String key, int type) {
    return false;
  }
}
