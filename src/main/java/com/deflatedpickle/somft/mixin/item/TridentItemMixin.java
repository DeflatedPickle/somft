/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.client.item.HealthTooltipData;
import java.util.Optional;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item {
  public TridentItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public Optional<TooltipData> getTooltipData(ItemStack stack) {
    var item = stack.getItem();
    if (item instanceof TridentItem tridentItem) {
      var damage = TridentItem.BASE_DAMAGE;
      var optional =
          tridentItem
              .getAttributeModifiers(stack, EquipmentSlot.MAINHAND)
              .get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
              .stream()
              .findFirst();
      if (optional.isPresent()) {
        damage = (float) optional.get().getValue();
      }
      return Optional.of(new HealthTooltipData(damage + 1));
    }
    return super.getTooltipData(stack);
  }
}
