/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity.projectile;

import com.deflatedpickle.somftcraft.api.Leashable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.EntityAttachmentS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntity implements Leashable {
  @Unique @Nullable private Entity holdingEntity;
  @Unique private int holdingEntityId;
  @Unique @Nullable private NbtCompound leashNbt;

  protected ArrowEntityMixin(
      EntityType<? extends PersistentProjectileEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "onHit", at = @At("RETURN"))
  public void initFromStack(LivingEntity target, CallbackInfo ci) {
    if ((ArrowEntity) (Object) this instanceof Leashable leashable
        && target instanceof MobEntity mobEntity) {
      mobEntity.attachLeash(holdingEntity, true);
    }
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    if (this.holdingEntity != null) {
      var nbtCompound2 = new NbtCompound();
      if (this.holdingEntity instanceof LivingEntity) {
        var uUID = this.holdingEntity.getUuid();
        nbtCompound2.putUuid("UUID", uUID);
      } else if (this.holdingEntity instanceof AbstractDecorationEntity) {
        var blockPos = ((AbstractDecorationEntity) this.holdingEntity).getDecorationBlockPos();
        nbtCompound2.putInt("X", blockPos.getX());
        nbtCompound2.putInt("Y", blockPos.getY());
        nbtCompound2.putInt("Z", blockPos.getZ());
      }

      nbt.put("Leash", nbtCompound2);
    } else if (this.leashNbt != null) {
      nbt.put("Leash", this.leashNbt.copy());
    }
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    if (nbt.contains("Leash", NbtElement.COMPOUND_TYPE)) {
      this.leashNbt = nbt.getCompound("Leash");
    }
  }

  public void somft$updateLeash() {
    if (this.leashNbt != null) {
      this.somft$readLeashNbt();
    }

    if (this.holdingEntity != null) {
      if (!this.isAlive() || !this.holdingEntity.isAlive()) {
        this.somft$detachLeash(true, true);
      }
    }
  }

  public void somft$detachLeash(boolean sendPacket, boolean dropItem) {
    if (this.holdingEntity != null) {
      this.holdingEntity = null;
      this.leashNbt = null;
      if (!this.getWorld().isClient && dropItem) {
        this.dropItem(Items.LEAD);
      }

      if (!this.getWorld().isClient && sendPacket && this.getWorld() instanceof ServerWorld) {
        ((ServerWorld) this.getWorld())
            .getChunkManager()
            .sendToOtherNearbyPlayers(this, new EntityAttachmentS2CPacket(this, null));
      }
    }
  }

  public boolean somft$canBeLeashedBy(@NotNull PlayerEntity player) {
    return !this.somft$isLeashed();
  }

  public boolean somft$isLeashed() {
    return this.holdingEntity != null;
  }

  @Nullable
  public Entity somft$getHoldingEntity() {
    if (this.holdingEntity == null && this.holdingEntityId != 0 && this.getWorld().isClient) {
      this.holdingEntity = this.getWorld().getEntityById(this.holdingEntityId);
    }

    return this.holdingEntity;
  }

  public void somft$attachLeash(@NotNull Entity entity, boolean sendPacket) {
    this.holdingEntity = entity;
    this.leashNbt = null;
    if (!this.getWorld().isClient && sendPacket && this.getWorld() instanceof ServerWorld) {
      ((ServerWorld) this.getWorld())
          .getChunkManager()
          .sendToOtherNearbyPlayers(this, new EntityAttachmentS2CPacket(this, this.holdingEntity));
    }
  }

  public void somft$setHoldingEntityId(int id) {
    this.holdingEntityId = id;
    this.somft$detachLeash(false, false);
  }

  public void somft$readLeashNbt() {
    if (this.leashNbt != null && this.getWorld() instanceof ServerWorld) {
      if (this.leashNbt.containsUuid("UUID")) {
        var uUID = this.leashNbt.getUuid("UUID");
        var entity = ((ServerWorld) this.getWorld()).getEntity(uUID);
        if (entity != null) {
          this.somft$attachLeash(entity, true);
        }
      } else if (this.leashNbt.contains("X", NbtElement.NUMBER_TYPE)
          && this.leashNbt.contains("Y", NbtElement.NUMBER_TYPE)
          && this.leashNbt.contains("Z", NbtElement.NUMBER_TYPE)) {
        var blockPos = NbtHelper.toBlockPos(this.leashNbt);
        this.somft$attachLeash(LeashKnotEntity.getOrCreate(this.getWorld(), blockPos), true);
      }
    }
  }
}
