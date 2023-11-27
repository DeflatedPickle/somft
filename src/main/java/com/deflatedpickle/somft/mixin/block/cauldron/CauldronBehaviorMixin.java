/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block.cauldron;

import com.deflatedpickle.somft.Somft;
import com.deflatedpickle.somft.block.PotionCauldronBlock;
import com.deflatedpickle.somft.block.cauldron.MilkCauldronBlock;
import com.deflatedpickle.somft.potion.PotionWrapper;
import java.util.List;
import java.util.Map;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
  @Shadow
  static void registerBucketBehavior(Map<Item, CauldronBehavior> behavior) {}

  @ModifyArg(
      method = "registerBehavior",
      at =
          @At(
              value = "INVOKE",
              target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
              ordinal = 0),
      index = 1)
  private static Object somft$registerPotionBehavior(Object key) {
    return (CauldronBehavior)
        (blockState, world, blockPos, playerEntity, hand, itemStack) -> {
          if (PotionUtil.getPotion(itemStack) != Potions.WATER) {
            if (!world.isClient) {
              var item = itemStack.getItem();
              playerEntity.setStackInHand(
                  hand,
                  ItemUsage.exchangeStack(
                      itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));
              playerEntity.incrementStat(Stats.USE_CAULDRON);
              playerEntity.incrementStat(Stats.USED.getOrCreateStat(item));
              world.setBlockState(
                  blockPos,
                  Somft.INSTANCE
                      .getPOTION_CAULDRON()
                      .getDefaultState()
                      .with(
                          PotionCauldronBlock.Companion.getPOTION(),
                          new PotionWrapper(PotionUtil.getPotion(itemStack))));
              world.playSound(
                  null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
              world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
            }

            return ActionResult.success(world.isClient);
          }

          return ((CauldronBehavior) key)
              .interact(blockState, world, blockPos, playerEntity, hand, itemStack);
        };
  }

  @Inject(method = "registerBehavior", at = @At("TAIL"))
  private static void somft$registerBehavior(CallbackInfo ci) {
    Somft.INSTANCE
        .getMILK_CAULDRON_BEHAVIOR()
        .put(Items.BUCKET, MilkCauldronBlock.INSTANCE.getEMPTY_MILK());
    registerBucketBehavior(Somft.INSTANCE.getMILK_CAULDRON_BEHAVIOR());
    Somft.INSTANCE
        .getPOTION_CAULDRON_BEHAVIOR()
        .put(Items.GLASS_BOTTLE, PotionCauldronBlock.Companion.getDECREASE_POTION());
    Somft.INSTANCE
        .getPOTION_CAULDRON_BEHAVIOR()
        .put(Items.POTION, PotionCauldronBlock.Companion.getINCREASE_POTION());
    registerBucketBehavior(Somft.INSTANCE.getPOTION_CAULDRON_BEHAVIOR());
  }

  @Inject(method = "registerBucketBehavior", at = @At("TAIL"))
  private static void somft$registerBucketBehaviour(
      Map<Item, CauldronBehavior> behavior, CallbackInfo ci) {
    behavior.put(Items.MILK_BUCKET, MilkCauldronBlock.INSTANCE.getFILL_WITH_MILK());

    for (var i :
        List.of(
            Items.PUFFERFISH_BUCKET,
            Items.SALMON_BUCKET,
            Items.COD_BUCKET,
            Items.TROPICAL_FISH_BUCKET,
            Items.AXOLOTL_BUCKET,
            Items.TADPOLE_BUCKET)) {
      behavior.put(i, Somft.INSTANCE.getFILL_WITH_BUCKET_FISH());
    }
  }
}
