/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.api.FireSpreader;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"UnusedMixin", "ConstantValue"})
@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin implements FireSpreader {
  @Override
  public int somftcraft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    if ((TorchBlock) (Object) this instanceof RedstoneTorchBlock) {
      return 0;
    } else {
      return 800;
    }
  }
}
