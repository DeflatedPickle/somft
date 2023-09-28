/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block.dispenser;

import com.deflatedpickle.somftcraft.Impl;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(ShearsDispenserBehavior.class)
public abstract class ShearsDispenserBehaviorMixin extends FallibleItemDispenserBehavior {
  @Inject(
      method = "dispenseSilently",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/block/dispenser/ShearsDispenserBehavior;setSuccess(Z)V"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION,
      cancellable = true)
  public void onDispenseSilently(
      BlockPointer pointer,
      ItemStack stack,
      CallbackInfoReturnable<ItemStack> cir,
      ServerWorld serverWorld) {
    setSuccess(Impl.INSTANCE.dispenseShears(pointer, serverWorld));
    if (this.isSuccess() && stack.damage(1, serverWorld.getRandom(), null)) {
      stack.setCount(0);
      cir.setReturnValue(stack);
    }
  }
}
