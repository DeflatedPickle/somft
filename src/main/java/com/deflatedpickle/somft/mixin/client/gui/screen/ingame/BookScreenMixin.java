/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somft.api.BookScreenExtra;
import com.deflatedpickle.somft.client.gui.widget.SoundIconWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(BookScreen.class)
public abstract class BookScreenMixin extends Screen implements BookScreenExtra {
  @Shadow private int pageIndex;

  @Shadow
  protected abstract void updatePageButtons();

  @Shadow
  public abstract int getPageCount();

  @Unique private SoundIconWidget lastPageButton;
  @Unique private SoundIconWidget firstPageButton;

  protected BookScreenMixin(Text title) {
    super(title);
  }

  @Inject(
      method = "addPageButtons",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;updatePageButtons()V",
              shift = At.Shift.BEFORE),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$addPageButtons(CallbackInfo ci, int i, int j) {
    var icons = new Identifier("somft", "textures/gui/book.png");
    this.lastPageButton =
        this.addDrawableChild(
            new SoundIconWidget(
                i + 116 - 10,
                161,
                10,
                9,
                11,
                0,
                12,
                icons,
                21,
                21,
                SoundEvents.ITEM_BOOK_PAGE_TURN,
                buttonWidget -> this.somft$openLastPage()));
    this.firstPageButton =
        this.addDrawableChild(
            new SoundIconWidget(
                i + 43 + 23,
                161,
                10,
                9,
                0,
                0,
                12,
                icons,
                21,
                21,
                SoundEvents.ITEM_BOOK_PAGE_TURN,
                buttonWidget -> this.somft$openFirstPage()));
  }

  @Unique
  public void somft$openFirstPage() {
    this.pageIndex = 0;

    this.updatePageButtons();
  }

  @Unique
  public void somft$openLastPage() {
    this.pageIndex = this.getPageCount() - 1;

    this.updatePageButtons();
  }

  @Inject(method = "updatePageButtons", at = @At("TAIL"))
  public void somft$updatePageButtons(CallbackInfo ci) {
    this.firstPageButton.visible = this.pageIndex > 0;
    this.lastPageButton.visible = this.pageIndex < this.getPageCount() - 1;
  }
}
