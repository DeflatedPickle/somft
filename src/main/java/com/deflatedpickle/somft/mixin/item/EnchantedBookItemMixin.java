/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
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
@Mixin(EnchantedBookItem.class)
public abstract class EnchantedBookItemMixin {
  @Inject(method = "appendTooltip", at = @At("RETURN"))
  public void onAppendTooltip(
      ItemStack stack,
      @Nullable World world,
      List<Text> tooltip,
      TooltipContext context,
      CallbackInfo ci) {
    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(
        Text.translatable("item.minecraft.smithing_template.applies_to")
            .formatted(Formatting.GRAY));

    // TODO: account for multiple enchantments
    for (var i : EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(stack)).entrySet()) {
      tooltip.add(
          CommonTexts.space()
              .append(Text.translatable("enchantment." + i.getKey().type.name().toLowerCase()))
              .formatted(Formatting.BLUE));
    }

    tooltip.add(
        Text.translatable("item.minecraft.smithing_template.ingredients")
            .formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(Text.translatable("enchanting.ingredients"))
            .formatted(Formatting.BLUE));

    // TODO: incompatible with
  }
}
