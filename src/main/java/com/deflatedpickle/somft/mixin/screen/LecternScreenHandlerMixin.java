/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.LecternScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(LecternScreenHandler.class)
public abstract class LecternScreenHandlerMixin {
  @Shadow
  public abstract void setProperty(int id, int value);

  @Inject(method = "onButtonClick", at = @At("HEAD"), cancellable = true)
  public void somft$onButtonClick(
      PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
    switch (id) {
      case 4:
        {
          this.setProperty(0, 0);
          cir.setReturnValue(true);
          break;
        }
      case 5:
        {
          this.setProperty(0, 100);
          cir.setReturnValue(true);
          break;
        }
    }
  }
}
