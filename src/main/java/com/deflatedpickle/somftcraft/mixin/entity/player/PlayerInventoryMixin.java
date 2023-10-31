/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.player;

import com.deflatedpickle.somftcraft.enchantment.DegradationCurseEnchantment;
import com.deflatedpickle.somftcraft.item.QuiverItem;
import java.util.function.Consumer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

  @Shadow @Final public PlayerEntity player;

  @Inject(
      method = "insertStack(ILnet/minecraft/item/ItemStack;)Z",
      at = @At("HEAD"),
      cancellable = true)
  public void onInsertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
    if (!stack.isEmpty() && stack.isIn(ItemTags.ARROWS)) {
      var optional = QuiverItem.INSTANCE.getQuiver(player);
      if (optional.isPresent()) {
        QuiverItem.INSTANCE.addToQuiver(optional.get(), stack);
        cir.setReturnValue(true);
      }
    }
  }

  @Redirect(
      method = "damageArmor",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
  public void onDamageArmor(
      ItemStack instance, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
    if (EnchantmentHelper.getLevel(DegradationCurseEnchantment.INSTANCE, instance) > 0) {
      amount = amount * 3;
    }

    instance.damage(amount, entity, breakCallback);
  }
}
