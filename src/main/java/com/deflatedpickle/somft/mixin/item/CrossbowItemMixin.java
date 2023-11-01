/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.item.QuiverItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("UnusedMixin")
@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
  @ModifyVariable(method = "loadProjectiles", at = @At("STORE"), ordinal = 2)
  private static ItemStack onStoppedUsing(
      ItemStack value, LivingEntity shooter, ItemStack projectile) {
    var optional = QuiverItem.INSTANCE.getQuiver(shooter);
    if (optional.isPresent()) {
      var decrement = QuiverItem.INSTANCE.removeFromFirstStack(optional.get());

      if (decrement.isPresent()) {
        return decrement.get();
      }
    }
    return Items.AIR.getDefaultStack();
  }
}
