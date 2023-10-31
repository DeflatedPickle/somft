/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.decoration;

import com.deflatedpickle.somftcraft.SomftCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.RideableOpenableInventory;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends Entity implements RideableOpenableInventory {
  public ArmorStandEntityMixin(EntityType<?> variant, World world) {
    super(variant, world);
  }

  @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
  public void onInteractAt(
      PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    if (player.shouldCancelInteraction()) {
      this.openInventory(player);
      cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
    }
  }

  @Override
  public void openInventory(PlayerEntity player) {
    if (!this.getWorld().isClient) {
      var buf = PacketByteBufs.create();
      buf.writeVarInt(player.currentScreenHandler.syncId);
      buf.writeVarInt(this.getId());
      ServerPlayNetworking.send(
          (ServerPlayerEntity) player, SomftCraft.INSTANCE.getARMOR_STAND_GUI_PACKET_ID(), buf);
    }
  }
}
