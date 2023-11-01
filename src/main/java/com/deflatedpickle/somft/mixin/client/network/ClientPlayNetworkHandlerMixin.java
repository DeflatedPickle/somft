/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.network;

import com.deflatedpickle.somft.api.Leashable;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.packet.s2c.play.EntityAttachmentS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
  @Inject(
      method = "onEntityAttachment",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void onEntityAttachment(EntityAttachmentS2CPacket packet, CallbackInfo ci, Entity entity) {
    if (entity instanceof ArrowEntity) {
      ((Leashable) entity).somft$setHoldingEntityId(packet.getHoldingEntityId());
    }
  }
}
