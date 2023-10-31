/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.item;

import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {
  public MilkBucketItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(Text.translatable("item.modifiers.consumed").formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(
                Text.translatable("item.minecraft.milk_bucket.desc1").formatted(Formatting.BLUE)));
  }
}
