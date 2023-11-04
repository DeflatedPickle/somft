/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.tooltip;

import com.deflatedpickle.somft.client.gui.tooltip.ArmorTooltipComponent;
import com.deflatedpickle.somft.client.gui.tooltip.FoodTooltipComponent;
import com.deflatedpickle.somft.client.gui.tooltip.HealthTooltipComponent;
import com.deflatedpickle.somft.client.item.ArmorTooltipData;
import com.deflatedpickle.somft.client.item.FoodTooltipData;
import com.deflatedpickle.somft.client.item.HealthTooltipData;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(TooltipComponent.class)
interface TooltipComponentMixin {
  @Inject(
      method =
          "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;",
      at = @At(value = "NEW", target = "(Ljava/lang/String;)Ljava/lang/IllegalArgumentException;"),
      cancellable = true)
  private static void of(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
    if (data instanceof FoodTooltipData foodTooltipData) {
      cir.setReturnValue(new FoodTooltipComponent(foodTooltipData));
    } else if (data instanceof ArmorTooltipData armorTooltipData) {
      cir.setReturnValue(new ArmorTooltipComponent(armorTooltipData));
    } else if (data instanceof HealthTooltipData healthTooltipData) {
      cir.setReturnValue(new HealthTooltipComponent(healthTooltipData));
    }
  }
}
