/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somft.api.BookScreenExtra;
import com.deflatedpickle.somft.client.gui.widget.SoundIconWidget;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen implements BookScreenExtra {

  @Shadow private int currentPage;

  @Shadow
  protected abstract void updateButtons();

  @Shadow
  protected abstract void changePage();

  @Shadow
  protected abstract int countPages();

  @Shadow private boolean signing;
  @Shadow @Final private List<String> pages;

  @Shadow
  protected abstract void setPageContent(String newContent);

  @Shadow private boolean dirty;
  @Unique private SoundIconWidget lastPageButton;
  @Unique private SoundIconWidget firstPageButton;
  @Unique private SoundIconWidget removePageButton;
  @Unique private SoundIconWidget clearPageButton;

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
    icons = new Identifier("textures/gui/spectator_widgets.png");
    this.removePageButton =
        this.addDrawableChild(
            new SoundIconWidget(
                i + 36,
                16,
                14,
                14,
                129,
                1,
                0,
                icons,
                256,
                256,
                SoundEvents.ITEM_BOOK_PAGE_TURN,
                buttonWidget -> this.somft$removeCurrentPage()));
    icons = new Identifier("textures/gui/container/beacon.png");
    this.clearPageButton =
        this.addDrawableChild(
            new SoundIconWidget(
                i + 33 + 13 + 6,
                16,
                13,
                13,
                114,
                223,
                0,
                icons,
                256,
                256,
                SoundEvents.ITEM_BOOK_PAGE_TURN,
                buttonWidget -> this.somft$clearCurrentPage()));
  }

  @Inject(method = "updateButtons", at = @At("TAIL"))
  public void somft$updateButtons(CallbackInfo ci) {
    this.firstPageButton.visible = !this.signing && this.currentPage > 0;
    this.lastPageButton.visible = !this.signing && this.currentPage < this.countPages() - 1;
    this.removePageButton.visible = !this.signing && this.countPages() - 1 > 0;
    // TODO: update buttons when pages become non-empty
    this.clearPageButton.visible =
        !this.signing /*&& !Objects.equals(this.pages.get(this.currentPage), "")*/;
  }

  @Unique
  public void somft$openFirstPage() {
    this.currentPage = 0;

    this.updateButtons();
    this.changePage();
  }

  @Unique
  public void somft$openLastPage() {
    this.currentPage = this.countPages() - 1;

    this.updateButtons();
    this.changePage();
  }

  @Unique
  private void somft$removeCurrentPage() {
    if (this.countPages() - 1 > 0) {
      this.pages.remove(this.currentPage);
      this.currentPage = Math.min(this.currentPage, this.countPages() - 1);
      this.dirty = true;

      this.updateButtons();
      this.changePage();
    }
  }

  @Unique
  private void somft$clearCurrentPage() {
    this.setPageContent("");
    this.changePage();
  }
}
