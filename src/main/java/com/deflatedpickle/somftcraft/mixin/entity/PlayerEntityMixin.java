/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.entity;

import com.deflatedpickle.somftcraft.api.HasPets;
import com.deflatedpickle.somftcraft.enchantment.DegradationCurseEnchantment;
import com.deflatedpickle.somftcraft.enchantment.MalnutritionCurseEnchantment;
import com.deflatedpickle.somftcraft.item.QuiverItem;
import java.util.function.Consumer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"WrongEntityDataParameterClass", "UnusedMixin"})
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HasPets {
  @Shadow
  public abstract Iterable<ItemStack> getArmorItems();

  @Unique
  private static final TrackedData<NbtCompound> PETS =
      DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

  protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  // TODO: make weapons weaker as they get less sharp

  @Redirect(
      method = "damageShield",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
  public void onDamageShield(
      ItemStack instance, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
    if (EnchantmentHelper.getLevel(DegradationCurseEnchantment.INSTANCE, instance) > 0) {
      amount = amount * 3;
    }

    instance.damage(amount, entity, breakCallback);
  }

  @Redirect(
      method = "eatFood",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/player/HungerManager;eat(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;)V"))
  public void onEatFood(HungerManager instance, Item item, ItemStack stack) {
    if (!item.isFood()) return;
    var foodComponent = item.getFoodComponent();
    if (foodComponent == null) return;

    var hunger = foodComponent.getHunger();
    var saturation = foodComponent.getSaturationModifier();

    for (var i : this.getArmorItems()) {
      if (EnchantmentHelper.getLevel(MalnutritionCurseEnchantment.INSTANCE, i) > 0) {
        hunger /= 1.2;
        saturation /= 1.2;
      }
    }

    instance.add(hunger, saturation);
  }

  @Inject(method = "initDataTracker", at = @At("RETURN"))
  public void onInitDataTracker(CallbackInfo ci) {
    var compound = new NbtCompound();
    compound.put("pets", new NbtList());
    this.dataTracker.startTracking(PETS, compound);
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
  public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.put("pets", somft$getPets());
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    if (nbt.contains("pets", NbtElement.COMPOUND_TYPE)) {
      this.somft$setPets(nbt.getCompound("pets"));
    }
  }

  @Inject(method = "getArrowType", at = @At("HEAD"), cancellable = true)
  public void getArrowType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
    if (stack.getItem() instanceof RangedWeaponItem) {
      var optional = QuiverItem.INSTANCE.getQuiver(this);
      if (optional.isPresent()) {
        var quiver = optional.get();
        var arrow = QuiverItem.INSTANCE.currentArrowStack(quiver);

        if (arrow.isPresent()) {
          cir.setReturnValue(arrow.get());
        } else {
          cir.setReturnValue(ItemStack.EMPTY);
        }
      }
    } else {
      cir.setReturnValue(ItemStack.EMPTY);
    }
  }

  @NotNull
  @Override
  public NbtCompound somft$getPets() {
    return this.dataTracker.get(PETS);
  }

  @Override
  public void somft$setPets(@NotNull NbtCompound compound) {
    this.dataTracker.set(PETS, compound);
  }

  @Override
  public void somft$addPet(@NotNull Entity entity) {
    var compound = new NbtCompound();
    compound.putUuid("id", entity.getUuid());
    ((NbtList) this.dataTracker.get(PETS).get("pets")).add(compound);
  }
}
