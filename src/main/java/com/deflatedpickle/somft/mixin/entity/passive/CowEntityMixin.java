/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.api.Milkable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"WrongEntityDataParameterClass", "UnusedMixin"})
@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends AnimalEntity implements Milkable {
  @Shadow
  protected abstract SoundEvent getAmbientSound();

  @Unique
  private static final TrackedData<Integer> MILK_TICKS =
      DataTracker.registerData(CowEntity.class, TrackedDataHandlerRegistry.INTEGER);

  @Unique private static final int MILK_TICKS_MAX = 24000 / 4;
  @Unique private static final String NBT_KEY = "Milk";

  protected CowEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
    super(entityType, world);
  }

  public int somft$getMilkTicks() {
    return this.dataTracker.get(MILK_TICKS);
  }

  public void somft$setMilkTicks(int ticks) {
    this.dataTracker.set(MILK_TICKS, ticks);
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(MILK_TICKS, MILK_TICKS_MAX);
  }

  @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
  public void interactMob(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    if (somft$getMilkTicks() > 0) {
      playAmbientSound();
      Impl.INSTANCE.spawnPlayerReactionParticles(this);
      cir.setReturnValue(super.interactMob(player, hand));
    }
  }

  @Inject(
      method = "interactMob",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/util/ActionResult;success(Z)Lnet/minecraft/util/ActionResult;",
              shift = At.Shift.BEFORE))
  public void interactMobMilkTicks(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    somft$setMilkTicks(MILK_TICKS_MAX);
  }

  @Override
  public void tick() {
    super.tick();
    somft$setMilkTicks(Math.max(0, somft$getMilkTicks() - 1));
  }

  @Override
  public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
    nbt.putInt(NBT_KEY, this.somft$getMilkTicks());
  }

  @Override
  public void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
    this.somft$setMilkTicks(nbt.getInt(NBT_KEY));
  }
}
