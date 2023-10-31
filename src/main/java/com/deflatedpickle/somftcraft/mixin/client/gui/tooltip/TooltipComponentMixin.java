/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.gui.tooltip;

import com.deflatedpickle.somftcraft.client.gui.tooltip.FoodTooltipComponent;
import com.deflatedpickle.somftcraft.client.item.FoodTooltipData;
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
      at = @At("HEAD"),
      cancellable = true)
  private static void of(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
    if (data instanceof FoodTooltipData foodTooltipData) {
      cir.setReturnValue(new FoodTooltipComponent(foodTooltipData));
    }
  }
}
