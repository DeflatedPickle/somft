/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.client.item.ArmorTooltipData;
import java.util.Optional;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin extends Item {
  public ArmorItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public Optional<TooltipData> getTooltipData(ItemStack stack) {
    var armor = stack.getItem();
    if (armor instanceof ArmorItem armorItem) {
      return Optional.of(new ArmorTooltipData(armorItem.getArmorSlot(), armorItem.getMaterial()));
    }
    return super.getTooltipData(stack);
  }
}
