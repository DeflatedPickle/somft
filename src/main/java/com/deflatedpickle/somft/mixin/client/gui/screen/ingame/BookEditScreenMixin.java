/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somft.client.gui.widget.SoundIconWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
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
@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {

  @Shadow private int currentPage;

  @Shadow
  protected abstract void updateButtons();

  @Shadow
  protected abstract void changePage();

  @Shadow
  protected abstract int countPages();

  @Shadow private boolean signing;
  @Unique private SoundIconWidget lastPageButton;
  @Unique private SoundIconWidget firstPageButton;

  protected BookEditScreenMixin(Text title) {
    super(title);
  }

  @Inject(
      method = "init",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/screen/ingame/BookEditScreen;updateButtons()V",
              shift = At.Shift.BEFORE),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$init(CallbackInfo ci, int i, int j) {
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
                buttonWidget -> this.openLastPage()));
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
                buttonWidget -> this.openFirstPage()));
  }

  @Inject(method = "updateButtons", at = @At("TAIL"))
  public void somft$updateButtons(CallbackInfo ci) {
    this.firstPageButton.visible = !this.signing && this.currentPage > 0;
    this.lastPageButton.visible = !this.signing && this.currentPage < this.countPages() - 1;
  }

  @Unique
  private void openFirstPage() {
    this.currentPage = 0;

    this.updateButtons();
    this.changePage();
  }

  @Unique
  private void openLastPage() {
    this.currentPage = this.countPages() - 1;

    this.updateButtons();
    this.changePage();
  }
}
