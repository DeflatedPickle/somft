/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.gui.screen;

import net.minecraft.client.gui.screen.WorldLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@SuppressWarnings("UnusedMixin")
@Mixin(WorldLoadingScreen.class)
public abstract class WorldLoadingScreenMixin {
  @ModifyConstant(method = "render", constant = @Constant(intValue = 30, ordinal = 1))
  private int moveChunkMap(int constant) {
    return -30;
  }

  @ModifyConstant(method = "render", constant = @Constant(intValue = 30, ordinal = 2))
  private int movePercentage(int constant) {
    return -30;
  }
}
