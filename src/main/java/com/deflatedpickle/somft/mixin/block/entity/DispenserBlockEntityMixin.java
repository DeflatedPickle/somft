/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block.entity;

import com.deflatedpickle.somft.api.DispenserExt;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin implements DispenserExt {
  @Unique private ItemStack bow = null;

  @Inject(method = "readNbt", at = @At("TAIL"))
  public void somft$readNbt(NbtCompound nbt, CallbackInfo ci) {
    if (nbt.contains("Bow")) {
      this.bow = ItemStack.fromNbt(nbt.getCompound("Bow"));
    }
  }

  @Inject(method = "writeNbt", at = @At("TAIL"))
  public void somft$writeNbt(NbtCompound nbt, CallbackInfo ci) {
    if (this.bow != null) {
      var compound = new NbtCompound();
      bow.writeNbt(compound);
      nbt.put("Bow", compound);
    }
  }

  @Nullable
  @Override
  public ItemStack getSomft$bowStack() {
    return this.bow;
  }

  @Override
  public void setSomft$bowStack(@Nullable ItemStack itemStack) {
    this.bow = itemStack;
  }
}
