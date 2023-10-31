/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.screen;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "rawtypes"})
@Mixin(ForgingScreenHandler.class)
public abstract class ForgingScreenHandlerMixin extends ScreenHandler {
  protected ForgingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
    super(type, syncId);
  }

  @Inject(method = "<init>", at = @At("RETURN"))
  public void onInit(
      ScreenHandlerType type,
      int syncId,
      PlayerInventory playerInventory,
      ScreenHandlerContext context,
      CallbackInfo ci) {
    Impl.INSTANCE.addExtraSlots(this, playerInventory);
  }
}
