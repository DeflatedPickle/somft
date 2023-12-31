/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.hud;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.client.gui.hud.*;
import kotlin.Unit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Shadow
  public abstract void renderHotbarItem(
      GuiGraphics graphics,
      int x,
      int y,
      float tickDelta,
      PlayerEntity player,
      ItemStack stack,
      int seed);

  @Shadow
  public abstract void render(GuiGraphics graphics, float tickDelta);

  @Unique private ControlsHud contextHud;
  @Unique private MiniMeHud miniMeHud;
  @Unique private ToolsHud toolsHud;

  @Inject(
      method = "<init>",
      at =
          @At(
              value = "FIELD",
              target =
                  "Lnet/minecraft/client/gui/hud/InGameHud;bossBarHud:Lnet/minecraft/client/gui/hud/BossBarHud;"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$init(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci) {
    this.contextHud = new ControlsHud(client);
    this.miniMeHud = new MiniMeHud(client);
    this.toolsHud = new ToolsHud(client);
  }

  @Inject(
      id = "move",
      method = "renderHeldItemTooltip",
      at = {@At(value = "HEAD", id = "head"), @At(value = "RETURN", id = "return")})
  public void somft$moveItemName(GuiGraphics graphics, CallbackInfo ci) {
    Impl.INSTANCE.moveItemText(graphics.getMatrices(), ci);
  }

  @Inject(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/gui/GuiGraphics;)V"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$render(GuiGraphics graphics, float tickDelta, CallbackInfo ci) {
    this.contextHud.render(graphics);
    this.miniMeHud.render(graphics);
    this.toolsHud.render(graphics);
  }

  @Redirect(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/GuiGraphics;)V"))
  public void somft$renderHotbar(InGameHud instance, float tickDelta, GuiGraphics graphics) {
    Impl.INSTANCE.renderHotbar(instance, tickDelta, graphics, this::renderHotbarItemUnit);
  }

  @Unique
  public Unit renderHotbarItemUnit(
      GuiGraphics graphics,
      int x,
      int y,
      float tickDelta,
      PlayerEntity player,
      ItemStack stack,
      int seed) {
    this.renderHotbarItem(graphics, x, y, tickDelta, player, stack, seed);
    return Unit.INSTANCE;
  }

  @Inject(
      method = "renderHeldItemTooltip",
      at =
          @At(
              value = "INVOKE",
              shift = At.Shift.AFTER,
              target =
                  "Lnet/minecraft/client/gui/GuiGraphics;drawShadowedText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$drawExtraText(
      GuiGraphics graphics, CallbackInfo ci, MutableText mutableText, int i, int j, int k, int l) {
    Impl.INSTANCE.drawItemEnchantments(graphics, i, j, k, l);
  }
}
