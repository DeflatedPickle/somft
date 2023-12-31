/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import com.deflatedpickle.somft.api.DirectionalFireSpreader;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin implements DirectionalFireSpreader {
  @Override
  public int somft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if (state.get(FurnaceBlock.LIT)) {
      if (direction == state.get(FurnaceBlock.FACING)) {
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
  public Direction somft$getDirection(@NotNull BlockState state) {
    return state.get(FurnaceBlock.FACING);
  }
}
