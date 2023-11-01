/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.gui.screen.ingame;

import com.deflatedpickle.somft.screen.TotemSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeScreenMixin
    extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
  @Shadow public static ItemGroup selectedTab;

  public CreativeScreenMixin(
      CreativeInventoryScreen.CreativeScreenHandler screenHandler,
      PlayerInventory playerInventory,
      Text text) {
    super(screenHandler, playerInventory, text);
  }

  @Inject(
      method = "setSelectedTab",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z",
              ordinal = 3))
  public void onInit(ItemGroup group, CallbackInfo ci) {
    this.handler.slots.add(
        new CreativeInventoryScreen.CreativeSlot(
            new TotemSlot(CreativeInventoryScreen.INVENTORY, MinecraftClient.getInstance().player),
            41,
            112 + 15,
            21));
  }

  @Inject(method = "render", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onDrawBackground(
      GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    if (selectedTab == Registries.ITEM_GROUP.getOrThrow(ItemGroups.SURVIVAL_INVENTORY)) {
      graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x + 112 + 14, y + 20, 76, 61, 18, 18);
    }
  }
}
