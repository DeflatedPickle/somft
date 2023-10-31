/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

  @Shadow public int backgroundHeight;

  @Shadow public int backgroundWidth;

  protected HandledScreenMixin(Text title) {
    super(title);
  }

  /**
   * @author
   * @reason
   */
  @Overwrite
  public boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
    return (mouseX < (double) left
            || mouseY < (double) top
            || mouseX >= (double) (left + this.backgroundWidth)
            || mouseY >= (double) (top + this.backgroundHeight))
        && Impl.INSTANCE.outsideExtraSlotBounds(mouseX, mouseY, left, top, button);
  }

  @Inject(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawBackground(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
              shift = At.Shift.AFTER),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onDrawBackground(
      GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci, int i, int j) {
    if (!((Object) this instanceof AbstractInventoryScreen<?>)) {
      Impl.INSTANCE.drawBackground(this, graphics, HandledScreen.BACKGROUND_TEXTURE, i, j);
    }
  }
}
