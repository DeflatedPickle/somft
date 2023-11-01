/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
  @Shadow @Final MinecraftClient client;

  @Shadow
  public abstract void setRenderHand(boolean renderHand);

  @Redirect(
      method = "renderWorld",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"))
  public void renderWorld(
      Camera instance,
      BlockView area,
      Entity focusedEntity,
      boolean thirdPerson,
      boolean inverseView,
      float tickDelta) {
    if (focusedEntity instanceof PlayerEntity playerEntity) {
      if (playerEntity.isDead()) {
        // FIXME: the player entity stops getting rendered. is it deleted? can we stop that?
        this.setRenderHand(false);
        var camera = this.client.gameRenderer.getCamera();
        camera.area = area;
        camera.focusedEntity = focusedEntity;
        camera.thirdPerson = true;
        camera.setRotation(focusedEntity.getYaw(tickDelta), 35);
        camera.setPos(
            MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()),
            MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY())
                + (double) MathHelper.lerp(tickDelta, camera.lastCameraY, camera.cameraY),
            MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
        camera.moveBy(-camera.clipToSpace(4.0), 4.0, 0.0);
      } else {
        instance.update(area, focusedEntity, thirdPerson, inverseView, tickDelta);
      }
    }
  }
}
