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

@SuppressWarnings("UnusedMixin")
@Mixin({
  CraftingScreenHandler.class,
  BrewingStandScreenHandler.class,
  EnchantmentScreenHandler.class,
  StonecutterScreenHandler.class,
  CartographyTableScreenHandler.class
})
public abstract class CraftingScreenHandlerMixin extends ScreenHandler {
  protected CraftingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
    super(type, syncId);
  }

  @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V", at = @At("RETURN"))
  public void onInit(int syncId, PlayerInventory playerInventory, CallbackInfo ci) {
    Impl.INSTANCE.addExtraSlots(this, playerInventory);
  }
}
