/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import com.deflatedpickle.somft.api.Milkable;
import com.deflatedpickle.somft.api.Timer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntity implements Milkable, Timer {
  protected ChickenEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
    super(entityType, world);
  }

  /*@Redirect(
      method = "tickMovement",
      at =
          @At(
              value = "FIELD",
              target = "Lnet/minecraft/entity/passive/ChickenEntity;eggLayTime:I",
              opcode = Opcodes.GETFIELD,
              ordinal = 0))
  public int somft$tickMovement$eggLayTime$GETFIELD(ChickenEntity instance) {
    return 0;
  }*/

  @Redirect(
      method = "tickMovement",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/ChickenEntity;isAlive()Z"))
  public boolean somft$tickMovement$isAlive(ChickenEntity instance) {
    return this.somft$isMilkable();
  }

  @Inject(
      method = "tickMovement",
      at =
          @At(
              value = "FIELD",
              target = "Lnet/minecraft/entity/passive/ChickenEntity;eggLayTime:I",
              opcode = Opcodes.PUTFIELD,
              ordinal = 1))
  public void somft$tickMovement$eggLayTime$PUTFIELD(CallbackInfo ci) {
    this.somft$setMilked(true);
  }

  @Inject(method = "tickMovement", at = @At("HEAD"))
  public void somft$tickMovement(CallbackInfo ci) {
    if (this.getWorld().isClient) {
      this.somft$setTimer(Math.max(0, this.somft$getTimer() - 1));
    }
  }
}
