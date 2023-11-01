/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import com.deflatedpickle.somft.api.FireSpreader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MagmaBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "deprecation"})
@Mixin(MagmaBlock.class)
public abstract class MagmaBlockMixin extends Block implements FireSpreader {
  public MagmaBlockMixin(Settings settings) {
    super(settings);
  }

  @Inject(method = "scheduledTick", at = @At("HEAD"))
  public void onScheduledTick(
      BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
    super.scheduledTick(state, world, pos, random);
  }

  @Override
  public int somft$getChance(@NotNull BlockState state, @NotNull Direction direction) {
    return 1;
  }
}
