/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somft.client.gui.ingame.PetButtonWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"UnusedMixin", "rawtypes", "unchecked"})
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen {
  @Unique PetButtonWidget petButtonWidget = new PetButtonWidget(-1, -1);

  public InventoryScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
  }

  @Inject(
      method = "init",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onInit(CallbackInfo ci) {
    petButtonWidget.setPosition(this.x + 26, this.y + 78 - petButtonWidget.getHeight());
    this.addDrawableChild(petButtonWidget);
  }

  @Inject(method = "render", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onDrawBackground(
      GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x + 76, y + 43, 76, 61, 18, 18);
  }
}
