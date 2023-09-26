/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity;

import java.util.Random;
import java.util.stream.IntStream;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  @Unique private static final String NBT_TAG = "ArrowCount";

  public LivingEntityMixin(EntityType<?> variant, World world) {
    super(variant, world);
  }

  @Shadow
  public abstract Box getBoundingBox(EntityPose pose);

  @Shadow
  public abstract int getStuckArrowCount();

  @Shadow
  public abstract void setStuckArrowCount(int stuckArrowCount);

  @Shadow
  public abstract boolean damage(DamageSource source, float amount);

  @ModifyVariable(method = "takeKnockback", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
  public double onTakeKnockback(double strength) {
    var box = getBoundingBox(((Entity) (Object) this).getPose());
    var volume = box.getXLength() * box.getYLength() * box.getZLength();
    return Math.pow(strength, volume / new Random().nextDouble(1.6, 1.8));
  }

  @Inject(method = "drop", at = @At("TAIL"))
  public void dropArrows(DamageSource source, CallbackInfo ci) {
    if (!getWorld().isClient && getStuckArrowCount() > 0) {
      IntStream.rangeClosed(0, random.nextInt(getStuckArrowCount() + 1))
          .forEach(
              i ->
                  getWorld()
                      .spawnEntity(
                          new ItemEntity(
                              getWorld(), getX(), getY(), getZ(), new ItemStack(Items.ARROW))));
    }
  }

  @Redirect(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"))
  public void tickSetStuckArrowCount(LivingEntity instance, int stuckArrowCount) {}

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    setStuckArrowCount(nbt.getInt(NBT_TAG));
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  public void writeCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.putInt(NBT_TAG, getStuckArrowCount());
  }
}
