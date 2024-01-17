/* Copyright (c) 2023-2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import com.deflatedpickle.somft.api.EatAngles;
import com.deflatedpickle.somft.api.Milkable;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({
  "WrongEntityDataParameterClass",
  "UnusedMixin",
  "unused",
  "SpellCheckingInspection"
})
@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends AnimalEntity implements Milkable, EatAngles {
  @Unique
  private static final TrackedData<Boolean> MILKED =
      DataTracker.registerData(CowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

  @Unique private int eatGrassTimer;
  @Unique private EatGrassGoal eatGrassGoal;

  protected CowEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "initGoals", at = @At("HEAD"))
  public void somft$initGoals$HEAD(CallbackInfo ci) {
    this.eatGrassGoal = new EatGrassGoal(this);
    this.goalSelector.add(5, this.eatGrassGoal);
  }

  @ModifyArg(
      method = "initGoals",
      index = 0,
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"),
      slice =
          @Slice(
              from =
                  @At(
                      value = "INVOKE",
                      target =
                          "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                      ordinal = 5),
              to =
                  @At(
                      value = "INVOKE",
                      target =
                          "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                      ordinal = 7)))
  public int somft$initGoals(int priority) {
    return priority + 1;
  }

  @Override
  protected void mobTick() {
    this.eatGrassTimer = this.eatGrassGoal.getTimer();
    super.mobTick();
  }

  @Override
  public void tickMovement() {
    if (this.getWorld().isClient) {
      this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
    }

    super.tickMovement();
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(MILKED, false);
  }

  @Override
  public void handleStatus(byte status) {
    if (status == EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART) {
      this.eatGrassTimer = 40;
    } else {
      super.handleStatus(status);
    }
  }

  @Override
  public float somft$getNeckAngle(float delta) {
    if (this.eatGrassTimer <= 0) {
      return 0.0F;
    } else if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
      return 1.0F;
    } else {
      return this.eatGrassTimer < 4
          ? ((float) this.eatGrassTimer - delta) / 4.0F
          : -((float) (this.eatGrassTimer - 40) - delta) / 4.0F;
    }
  }

  @Override
  public float somft$getHeadAngle(float delta) {
    if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
      float f = ((float) (this.eatGrassTimer - 4) - delta) / 32.0F;
      return ((float) (Math.PI / 5)) + 0.21991149F * MathHelper.sin(f * 28.7F);
    } else {
      return this.eatGrassTimer > 0
          ? (float) (Math.PI / 5)
          : this.getPitch() * (float) (Math.PI / 180.0);
    }
  }

  @Redirect(
      method = "interactMob",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CowEntity;isBaby()Z"))
  public boolean somft$interactMob$INVOKE$isBaby(CowEntity instance) {
    return !somft$isMilkable();
  }

  @Inject(method = "interactMob", at = @At(value = "RETURN", ordinal = 0))
  public void somft$interatMob$RETURN$0(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    this.somft$milked();
  }

  @Override
  public void somft$milked() {
    this.somft$setMilked(true);
  }

  @Override
  public boolean somft$isMilkable() {
    return this.isAlive() && !this.somft$isMilked() && !this.isBaby();
  }

  @Override
  public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
    nbt.putBoolean("Milked", this.somft$isMilked());
  }

  @Override
  public void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
    this.somft$setMilked(nbt.getBoolean("Milked"));
  }

  @Override
  public boolean somft$isMilked() {
    return this.dataTracker.get(MILKED);
  }

  @Override
  public void somft$setMilked(boolean milked) {
    this.dataTracker.set(MILKED, milked);
  }

  @Override
  public void onEatingGrass() {
    super.onEatingGrass();
    this.somft$setMilked(false);
    if (this.isBaby()) {
      this.growUp(60);
    }
  }
}
