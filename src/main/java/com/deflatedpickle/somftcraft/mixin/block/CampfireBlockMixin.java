/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.api.FireSpreader;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("UnusedMixin")
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin implements FireSpreader {
  @Override
  public int somftcraft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if (direction == Direction.UP) {
      return 1;
    } else {
      return 450;
    }
  }
}
