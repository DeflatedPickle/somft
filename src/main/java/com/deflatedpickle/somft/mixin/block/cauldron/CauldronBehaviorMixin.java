/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block.cauldron;

import com.deflatedpickle.somft.Somft;
import java.util.Map;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
  @Shadow
  static void registerBucketBehavior(Map<Item, CauldronBehavior> behavior) {}

  @Inject(method = "registerBehavior", at = @At("TAIL"))
  private static void somft$registerBehavior(CallbackInfo ci) {
    Somft.INSTANCE.getMILK_CAULDRON_BEHAVIOR().put(Items.BUCKET, Somft.INSTANCE.getEMPTY_MILK());
    registerBucketBehavior(Somft.INSTANCE.getMILK_CAULDRON_BEHAVIOR());
  }

  @Inject(method = "registerBucketBehavior", at = @At("TAIL"))
  private static void somft$registerBucketBehaviour(
      Map<Item, CauldronBehavior> behavior, CallbackInfo ci) {
    behavior.put(Items.MILK_BUCKET, Somft.INSTANCE.getFILL_WITH_MILK());
  }
}
