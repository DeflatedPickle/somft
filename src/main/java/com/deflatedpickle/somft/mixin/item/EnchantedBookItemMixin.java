/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import com.deflatedpickle.somft.api.IncompatibleEnchantments;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
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
    var slots = new ArrayList<String>();
    var incompatibilities = new ArrayList<Enchantment>();

    for (var i : EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(stack)).entrySet()) {
      slots.add("enchantment." + i.getKey().type.name().toLowerCase());
      incompatibilities.addAll(((IncompatibleEnchantments) i.getKey()).somft$getIncompatible());
    }

    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(
        Text.translatable("item.minecraft.smithing_template.applies_to")
            .formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(
                slots.stream()
                    .map(I18n::translate)
                    .collect(Collectors.joining(I18n.translate("interaction.entity.delimiter"))))
            .formatted(Formatting.BLUE));

    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(
        Text.translatable("item.minecraft.smithing_template.ingredients")
            .formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(Text.translatable("enchanting.ingredients"))
            .formatted(Formatting.BLUE));

    if (!incompatibilities.isEmpty()) {
      tooltip.add(CommonTexts.EMPTY);
      tooltip.add(
          Text.translatable("item.minecraft.enchantment_book.incompatible_with")
              .formatted(Formatting.GRAY));
      tooltip.add(
          CommonTexts.space()
              .append(
                  incompatibilities.stream()
                      .map(Enchantment::getTranslationKey)
                      .map(I18n::translate)
                      .collect(Collectors.joining(I18n.translate("interaction.entity.delimiter"))))
              .formatted(Formatting.BLUE));
    }
  }
}
