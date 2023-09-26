/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.api.DirectionalFireSpreader;
import net.minecraft.block.BlockState;
import net.minecraft.block.SmokerBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(SmokerBlock.class)
public abstract class SmokerBlockMixin implements DirectionalFireSpreader {
  @Override
  public int somftcraft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if (state.get(SmokerBlock.LIT)) {
      if (direction == state.get(SmokerBlock.FACING)) {
        return 1;
      } else {
        return 600;
      }
    } else {
      return 0;
    }
  }

  @NotNull
  @Override
  public Direction somftcraft$getDirection(@NotNull BlockState state) {
    return state.get(SmokerBlock.FACING);
  }
}
