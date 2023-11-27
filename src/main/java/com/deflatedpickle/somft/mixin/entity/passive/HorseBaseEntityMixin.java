/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import com.deflatedpickle.somft.api.HasPets;
import com.deflatedpickle.somft.api.PetLogic;
import java.util.UUID;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin extends AnimalEntity implements PetLogic {
  @Shadow
  public abstract void setTame(boolean tame);

  @Shadow
  public abstract void setOwnerUuid(@Nullable UUID uuid);

  @Shadow private @Nullable UUID uuid;

  @Unique
  private static final TrackedData<Integer> MOVEMENT_LOGIC =
      DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.INTEGER);

  @Unique
  private static final TrackedData<Integer> ATTACK_LOGIC =
      DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.INTEGER);

  @Unique
  private static final TrackedData<Integer> HURT_LOGIC =
      DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.INTEGER);

  protected HorseBaseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "initDataTracker", at = @At("RETURN"))
  public void onInitDataTracker(CallbackInfo ci) {
    this.dataTracker.startTracking(MOVEMENT_LOGIC, PetLogic.Movement.FOLLOW.ordinal());
    this.dataTracker.startTracking(ATTACK_LOGIC, PetLogic.Attack.GUARD.ordinal());
    this.dataTracker.startTracking(HURT_LOGIC, PetLogic.Hurt.FIGHT.ordinal());
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
  public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.putInt("movement", somft$getMovementLogic().ordinal());
    nbt.putInt("attack", somft$getAttackLogic().ordinal());
    nbt.putInt("hurt", somft$getHurtLogic().ordinal());
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    somft$setMovementLogic(PetLogic.Movement.getEntries().get(nbt.getInt("movement")));
    somft$setAttackLogic(PetLogic.Attack.getEntries().get(nbt.getInt("attack")));
    somft$setHurtLogic(PetLogic.Hurt.getEntries().get(nbt.getInt("hurt")));
  }

  @Inject(method = "setOwnerUuid", at = @At("HEAD"), cancellable = true)
  public void onSetOwnerUUID(UUID uuid, CallbackInfo ci) {
    if (uuid == null) {
      setTame(false);
      setOwnerUuid(null);
      ci.cancel();
    }
  }

  @Inject(method = "setOwnerUuid", at = @At("RETURN"))
  public void onSetOwner(UUID uuid, CallbackInfo ci) {
    if (uuid != null) {
      var player = this.getWorld().getPlayerByUuid(uuid);

      if (player != null) {
        ((HasPets) player).somft$addPet(this);
      }
    }
  }

  @NotNull
  @Override
  public Movement somft$getMovementLogic() {
    return Movement.getEntries().get(this.dataTracker.get(MOVEMENT_LOGIC));
  }

  @Override
  public void somft$setMovementLogic(@NotNull PetLogic.Movement logic) {
    this.dataTracker.set(MOVEMENT_LOGIC, logic.ordinal());
  }

  @NotNull
  @Override
  public Attack somft$getAttackLogic() {
    return Attack.getEntries().get(this.dataTracker.get(ATTACK_LOGIC));
  }

  @Override
  public void somft$setAttackLogic(@NotNull PetLogic.Attack logic) {
    this.dataTracker.set(ATTACK_LOGIC, logic.ordinal());
  }

  @NotNull
  @Override
  public Hurt somft$getHurtLogic() {
    return Hurt.getEntries().get(this.dataTracker.get(HURT_LOGIC));
  }

  @Override
  public void somft$setHurtLogic(@NotNull PetLogic.Hurt logic) {
    this.dataTracker.set(HURT_LOGIC, logic.ordinal());
  }
}
