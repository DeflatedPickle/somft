/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.screen;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "rawtypes"})
@Mixin(AbstractFurnaceScreenHandler.class)
public abstract class AbstractFurnaceScreenHandlerMixin extends ScreenHandler {
  protected AbstractFurnaceScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
    super(type, syncId);
  }

  @Inject(
      method =
          "<init>(Lnet/minecraft/screen/ScreenHandlerType;Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/recipe/book/RecipeBookCategory;ILnet/minecraft/entity/player/PlayerInventory;)V",
      at = @At("RETURN"))
  public void onInit(
      ScreenHandlerType type,
      RecipeType recipeType,
      RecipeBookCategory category,
      int syncId,
      PlayerInventory playerInventory,
      CallbackInfo ci) {
    Impl.INSTANCE.addExtraSlots(this, playerInventory);
  }
}
