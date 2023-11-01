/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import com.deflatedpickle.somft.api.FireSpreader;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends Block implements FireSpreader {

  public CampfireBlockMixin(Settings settings) {
    super(settings);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(Text.translatable("block.minecraft.campfire.desc1").formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(
                Text.translatable("block.minecraft.campfire.desc2").formatted(Formatting.BLUE)));
  }

  @Override
  public int somft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if (direction == Direction.UP) {
      return 1;
    } else {
      return 450;
    }
  }
}
