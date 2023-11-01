/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.SomftClient;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin({
  GenericMessageScreen.class,
  WorldLoadingScreen.class,
  DownloadingTerrainScreen.class,
})
public abstract class LoadingScreenTipsMixin extends Screen {
  @Unique private String tip;

  protected LoadingScreenTipsMixin(Text title) {
    super(title);
  }

  @Override
  public void init() {
    super.init();

    tip =
        SomftClient.INSTANCE
            .getTooltips()
            .get(ThreadLocalRandom.current().nextInt(SomftClient.INSTANCE.getTooltips().size()));
  }

  @Inject(method = "render", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    Impl.INSTANCE.drawTip(
        graphics,
        this.textRenderer,
        mouseX,
        mouseY,
        delta,
        ci,
        Util.getMeasuringTimeMs(),
        this.width / 2,
        this.height / 2,
        this.tip);
  }
}
