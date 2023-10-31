/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.screen;

import com.deflatedpickle.somftcraft.screen.TotemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {
  protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
    super(type, syncId);
  }

  @Inject(method = "<init>", at = @At("RETURN"))
  public void onInit(
      PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
    this.addSlot(new TotemSlot(inventory, owner));
  }
}
