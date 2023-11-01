/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
  @Redirect(
      method = "doItemUse",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
  public boolean isEmpty(ItemStack instance) {
    return false;
  }
}
