/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.enchantment;

import com.deflatedpickle.somft.enchantment.SomftEnchantmentTarget;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
  @Redirect(
      method = "getPossibleEntries",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
  private static boolean getPossibleEntries(EnchantmentTarget instance, Item item) {
    return instance.isAcceptableItem(item)
        || (instance == EnchantmentTarget.ARMOR
            && SomftEnchantmentTarget.HORSE.isAcceptableItem(item));
  }
}
