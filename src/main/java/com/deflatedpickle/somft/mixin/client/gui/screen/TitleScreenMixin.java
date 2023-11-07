/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen;

import com.deflatedpickle.somft.Impl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.GenericMessageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
  @Unique
  private static final Identifier ARROWS = new Identifier("somft", "textures/gui/arrows.png");

  protected TitleScreenMixin(Text title) {
    super(title);
  }

  @Inject(
      method = "initWidgetsNormal",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  private void initWidgetsNormal(
      int y, int spacingY, CallbackInfo ci, Text text, boolean bl, Tooltip tooltip) {
    var loadLastWorld =
        new TexturedButtonWidget(
            this.width / 2 + 80 + spacingY,
            y - spacingY,
            20,
            20,
            0,
            0,
            20,
            ARROWS,
            20,
            40,
            buttonWidget -> {
              var storage = this.client.getWorldStorage();
              var saves = storage.getWorldSaveList().worldSaves();
              var world = saves.get(saves.size() - 1);
              var name = world.getName();

              this.client
                  .getSoundManager()
                  .play(PositionedSoundInstance.create(SoundEvents.UI_BUTTON_CLICK, 1.0F));
              if (this.client.getWorldStorage().worldSaveExists(name)) {
                this.client.forceSetScreen(
                    new GenericMessageScreen(Text.translatable("selectWorld.data_read")));
                this.client.createIntegratedServerLoader().start(this, name);
              }
            });
    loadLastWorld.setTooltip(Tooltip.create(Text.translatable("title.continue")));
    this.addDrawableChild(loadLastWorld);
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
