/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.gui.hud;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Inject(
      id = "move",
      method = "renderHeldItemTooltip",
      at = {@At(value = "HEAD", id = "head"), @At(value = "RETURN", id = "return")})
  public void moveItemName(GuiGraphics graphics, CallbackInfo ci) {
    Impl.INSTANCE.moveItemText(graphics.getMatrices(), ci);
  }

  @Inject(
      method = "renderHeldItemTooltip",
      at =
          @At(
              value = "INVOKE",
              shift = At.Shift.AFTER,
              target =
                  "Lnet/minecraft/client/gui/GuiGraphics;drawShadowedText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void drawExtraText(
      GuiGraphics graphics, CallbackInfo ci, MutableText mutableText, int i, int j, int k, int l) {
    Impl.INSTANCE.drawItemEnchantments(graphics, i, j, k, l);
  }
}
