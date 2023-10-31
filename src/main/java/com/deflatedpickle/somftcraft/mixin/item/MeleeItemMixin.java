/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.item;

import com.deflatedpickle.somftcraft.enchantment.DegradationCurseEnchantment;
import java.util.function.Consumer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin({MiningToolItem.class, SwordItem.class, TridentItem.class})
public abstract class MeleeItemMixin {
  @Redirect(
      method = "postHit",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
  public void onPostHit(
      ItemStack instance, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
    if (EnchantmentHelper.getLevel(DegradationCurseEnchantment.INSTANCE, instance) > 0) {
      amount = amount * 3;
    }

    instance.damage(amount, entity, breakCallback);
  }

  @Redirect(
      method = "postMine",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
  public void onPostMine(
      ItemStack instance, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
    if (EnchantmentHelper.getLevel(DegradationCurseEnchantment.INSTANCE, instance) > 0) {
      amount = amount * 3;
    }

    instance.damage(amount, entity, breakCallback);
  }
}
