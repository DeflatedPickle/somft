/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.item.QuiverItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("UnusedMixin")
@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem {
  public BowItemMixin(Settings settings) {
    super(settings);
  }

  @ModifyVariable(method = "onStoppedUsing", at = @At("STORE"), ordinal = 1)
  public ItemStack onStoppedUsing(
      ItemStack value, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    var optional = QuiverItem.INSTANCE.getQuiver(user);
    if (optional.isPresent()) {
      var decrement = QuiverItem.INSTANCE.removeFromFirstStack(optional.get());

      if (decrement.isPresent()) {
        return decrement.get();
      }
    }
    return Items.AIR.getDefaultStack();
  }
}
