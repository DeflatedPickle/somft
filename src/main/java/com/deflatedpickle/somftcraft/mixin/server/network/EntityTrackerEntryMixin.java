/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.server.network;

import com.deflatedpickle.somftcraft.api.Leashable;
import java.util.function.Consumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityAttachmentS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin {
  @Shadow @Final private Entity entity;

  @Inject(method = "sendPackets", at = @At("RETURN"))
  public void sendPackets(
      ServerPlayerEntity player,
      Consumer<Packet<ClientPlayPacketListener>> sender,
      CallbackInfo ci) {
    if (this.entity instanceof ArrowEntity arrow
        && arrow instanceof Leashable leashable
        && leashable.somft$isLeashed()) {
      sender.accept(new EntityAttachmentS2CPacket(arrow, leashable.somft$getHoldingEntity()));
    }
  }
}
