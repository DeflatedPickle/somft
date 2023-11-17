/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.recipe.book;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.recipe.book.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipe.book.RecipeGroupButtonWidget;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(RecipeBookWidget.class)
public abstract class RecipeBookWidgetMixin {
  @Shadow protected MinecraftClient client;

  @Shadow @Final private List<RecipeGroupButtonWidget> tabButtons;

  @ModifyReturnValue(method = "findLeftEdge", at = @At("RETURN"))
  public int findLeftEdge(int original) {
    if (client.player.currentScreenHandler instanceof PlayerScreenHandler) {
      return original;
    }
    return original + 20;
  }

  @Inject(
      method = "drawTooltip",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/screen/recipe/book/RecipeBookResults;drawTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V"))
  public void drawTooltip(
      GuiGraphics graphics, int x, int y, int mouseX, int mouseY, CallbackInfo ci) {
    for (var i : tabButtons) {
      if (i.isMouseOver(mouseX, mouseY)) {
        graphics.drawTooltip(
            this.client.textRenderer,
            Text.translatable("recipe_book.tab." + i.getCategory().name().toLowerCase()),
            mouseX,
            mouseY);
      }
    }
  }
}
