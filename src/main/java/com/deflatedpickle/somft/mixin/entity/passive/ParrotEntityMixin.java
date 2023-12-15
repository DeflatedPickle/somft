/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import static net.minecraft.entity.passive.ParrotEntity.TAMING_INGREDIENTS;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.UUID;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(ParrotEntity.class)
public abstract class ParrotEntityMixin extends TameableShoulderEntity {
  @Shadow
  public abstract boolean isBreedingItem(ItemStack stack);

  protected ParrotEntityMixin(
      EntityType<? extends TameableShoulderEntity> entityType, World world) {
    super(entityType, world);
  }

  @ModifyReturnValue(method = "isBaby", at = @At("RETURN"))
  public boolean somft$isBaby(boolean original) {
    return this.getBreedingAge() < 0;
  }

  @Inject(
      method = "initGoals",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
              ordinal = 4))
  public void somft$initGoals(CallbackInfo ci) {
    this.goalSelector.add(3, new AnimalMateGoal(this, 1.0));
  }

  @ModifyArg(
      method = "initGoals",
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
                      ordinal = 7)),
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
  public int somft$initGoals$add(int priority) {
    if (priority < 3) {
      return 3;
    } else {
      return priority + 1;
    }
  }

  @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
  public void somft$interactMob(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    var itemStack = player.getStackInHand(hand);
    var item = itemStack.getItem();

    if (this.getWorld().isClient) {
      var bl =
          this.isOwner(player)
              || this.isTamed()
              || this.isBreedingItem(itemStack) && !this.isTamed();
      cir.setReturnValue(bl ? ActionResult.CONSUME : ActionResult.PASS);
    } else if (this.isTamed()
        && this.isBreedingItem(itemStack)
        && this.getHealth() < this.getMaxHealth()) {
      if (!player.getAbilities().creativeMode) {
        itemStack.decrement(1);
      }

      var foodComponent = item.getFoodComponent();
      this.heal(foodComponent == null ? 1f : foodComponent.getHunger());
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }

  @ModifyExpressionValue(
      method = "interactMob",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/passive/ParrotEntity;isOwner(Lnet/minecraft/entity/LivingEntity;)Z"))
  public boolean somft$interactMob$isTamed(boolean original, PlayerEntity player, Hand hand) {
    return original && !this.isBreedingItem(player.getStackInHand(hand));
  }

  @ModifyReturnValue(method = "isBreedingItem", at = @At("RETURN"))
  public boolean somft$isBreedingItem(boolean original, ItemStack stack) {
    return TAMING_INGREDIENTS.contains(stack.getItem());
  }

  @ModifyReturnValue(method = "canBreedWith", at = @At("RETURN"))
  public boolean somft$canBreedWith(boolean original, AnimalEntity other) {
    if (other == this) {
      return false;
    } else if (other.getClass() != this.getClass()) {
      return false;
    } else {
      return this.isInLove() && other.isInLove();
    }
  }

  @ModifyReturnValue(method = "createChild", at = @At("RETURN"))
  public PassiveEntity somft$createChild(
      PassiveEntity original, ServerWorld serverWorld, PassiveEntity passiveEntity) {
    var parrotEntity = EntityType.PARROT.create(serverWorld);
    if (parrotEntity != null) {
      UUID uUID = this.getOwnerUuid();
      if (uUID != null) {
        parrotEntity.setOwnerUuid(uUID);
        parrotEntity.setTamed(true);
      }
    }

    return parrotEntity;
  }
}
