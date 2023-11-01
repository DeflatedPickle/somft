/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.Impl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin({FlintAndSteelItem.class, FireChargeItem.class})
public abstract class IgniteEntityMixin extends Item {
  public IgniteEntityMixin(Settings settings) {
    super(settings);
  }

  @Override
  public ActionResult useOnEntity(
      ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
    return Impl.INSTANCE.igniteEntity(stack, user, entity, hand);
  }
}
