/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.screen;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(Generic3x3ContainerScreenHandler.class)
public abstract class Generic3x3ContainerScreenHandlerMixin extends ScreenHandler {
  protected Generic3x3ContainerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
    super(type, syncId);
  }

  @Inject(
      method =
          "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
      at = @At("RETURN"))
  public void onInit(
      int syncId, PlayerInventory playerInventory, Inventory inventory, CallbackInfo ci) {
    Impl.INSTANCE.addExtraSlots(this, playerInventory);
  }
}
