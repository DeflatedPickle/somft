/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.client.item.HealthTooltipData;
import java.util.Optional;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends Item {
  public SwordItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public Optional<TooltipData> getTooltipData(ItemStack stack) {
    var item = stack.getItem();
    if (item instanceof SwordItem swordItem) {
      return Optional.of(new HealthTooltipData(swordItem.getAttackDamage() + 1));
    }
    return super.getTooltipData(stack);
  }
}
