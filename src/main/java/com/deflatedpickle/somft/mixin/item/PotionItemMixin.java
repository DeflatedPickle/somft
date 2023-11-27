/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(PotionItem.class)
public abstract class PotionItemMixin {
  @Inject(method = "appendTooltip", at = @At("RETURN"))
  public void onAppendTooltip(
      ItemStack stack,
      @Nullable World world,
      List<Text> tooltip,
      TooltipContext context,
      CallbackInfo ci) {
    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(Text.translatable("item.minecraft.bucket.desc1").formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(Text.translatable("item.minecraft.bucket.desc2").formatted(Formatting.BLUE)));
  }
}
