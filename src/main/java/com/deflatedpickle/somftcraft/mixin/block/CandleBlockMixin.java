/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.api.FireSpreader;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(CandleBlock.class)
public abstract class CandleBlockMixin implements FireSpreader {
  @Override
  public int somftcraft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if (state.get(CandleBlock.LIT)) {
      return 800 / state.get(CandleBlock.CANDLES);
    } else {
      return 0;
    }
  }
}
