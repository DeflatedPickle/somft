/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.client.item.HealthTooltipData;
import java.util.Optional;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends Item {
  public MiningToolItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public Optional<TooltipData> getTooltipData(ItemStack stack) {
    var item = stack.getItem();
    if (item instanceof MiningToolItem miningToolItem) {
      return Optional.of(new HealthTooltipData(miningToolItem.getAttackDamage() + 1));
    }
    return super.getTooltipData(stack);
  }
}
