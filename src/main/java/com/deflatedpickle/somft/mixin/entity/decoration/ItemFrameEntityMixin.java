/* Copyright (c) 2023-2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.decoration;

import com.deflatedpickle.somft.api.Waxable;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin", "WrongEntityDataParameterClass"})
@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity implements Waxable {

  @Unique
  private static final TrackedData<Boolean> WAXED =
      DataTracker.registerData(ItemFrameEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

  protected ItemFrameEntityMixin(
      EntityType<? extends AbstractDecorationEntity> entityType, World world) {
    super(entityType, world);
  }

  @Shadow
  public abstract ItemStack getHeldItemStack();

  @Inject(method = "initDataTracker", at = @At("TAIL"))
  public void somft$initDataTracker(CallbackInfo ci) {
    this.getDataTracker().startTracking(WAXED, false);
  }

  @ModifyExpressionValue(
      method = "damage",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"))
  public boolean somft$damage$isWaxed(boolean original) {
    return original && !this.somft$getWaxed();
  }

  @ModifyReturnValue(method = "damage", at = @At("RETURN"))
  public boolean somft$damage$return(boolean original) {
    return !this.somft$getWaxed();
  }

  @Redirect(
      method = "interact",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setRotation(I)V"))
  public void somft$interact$setRoation(ItemFrameEntity instance, int value, PlayerEntity player) {
    if (!this.somft$getWaxed()) {
      if (player.isSneaking()) {
        instance.setRotation(0);
      } else {
        instance.setRotation(value);
      }
    }
  }

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void somft$interact$setWaxed(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    var stack = player.getStackInHand(hand);

    if (!this.getWorld().isClient) {
      if (!this.getHeldItemStack().isEmpty() && stack.isOf(Items.HONEYCOMB)) {
        if (this.somft$setWaxed(true, true)) {
          if (!player.isCreative()) {
            stack.decrement(1);
          }

          this.emitGameEvent(GameEvent.BLOCK_CHANGE, player);
          player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
          this.getWorld().syncWorldEvent(null, WorldEvents.BLOCK_WAXED, this.getBlockPos(), 0);
          cir.setReturnValue(ActionResult.SUCCESS);
        } else {
          cir.setReturnValue(ActionResult.PASS);
        }
      }
    }
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  public void somft$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.putBoolean("Waxed", this.somft$getWaxed());
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  public void somft$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    this.somft$setWaxed(nbt.getBoolean("Waxed"), true);
  }

  @Override
  public boolean somft$getWaxed() {
    return this.getDataTracker().get(WAXED);
  }

  @Override
  public boolean somft$setWaxed(boolean waxed, boolean updateComparators) {
    if (this.somft$getWaxed() != waxed) {
      this.getDataTracker().set(WAXED, waxed);
      if (updateComparators && this.attachmentPos != null) {
        this.getWorld().updateComparators(this.attachmentPos, Blocks.AIR);
      }
      return true;
    } else {
      return false;
    }
  }
}
