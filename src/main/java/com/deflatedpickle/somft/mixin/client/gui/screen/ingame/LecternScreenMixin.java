/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somft.api.BookScreenExtra;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"UnusedMixin"})
@Mixin(LecternScreen.class)
public abstract class LecternScreenMixin extends BookScreen implements BookScreenExtra {
  @Shadow
  protected abstract void sendButtonPressPacket(int id);

  @Override
  public void somft$openFirstPage() {
    this.sendButtonPressPacket(4);
  }

  @Override
  public void somft$openLastPage() {
    this.sendButtonPressPacket(5);
  }
}
