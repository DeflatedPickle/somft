/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.gui.screen;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
  protected TitleScreenMixin(Text title) {
    super(title);
  }

  @Inject(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              shift = At.Shift.BEFORE,
              target =
                  "Lnet/minecraft/client/gui/SplashTextRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/client/font/TextRenderer;I)V"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onRender(
      GuiGraphics graphics,
      int mouseX,
      int mouseY,
      float delta,
      CallbackInfo ci,
      float f,
      float g,
      int i) {
    Impl.INSTANCE.drawExtraTitleComponents(
        (TitleScreen) (Object) this, graphics, mouseX, mouseY, textRenderer, width, height, i);
  }
}
