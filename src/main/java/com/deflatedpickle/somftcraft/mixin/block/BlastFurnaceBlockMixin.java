/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.api.DirectionalFireSpreader;
import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(BlastFurnaceBlock.class)
public abstract class BlastFurnaceBlockMixin implements DirectionalFireSpreader {
  @Override
  public int somftcraft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if (state.get(BlastFurnaceBlock.LIT)) {
      if (direction == state.get(BlastFurnaceBlock.FACING)) {
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
    return state.get(BlastFurnaceBlock.FACING);
  }
}
