/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("UnusedMixin")
@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
  @Shadow
  public abstract Block getBlock();

  @Unique
  private static final FoodComponent flowerFood =
      (new FoodComponent.Builder())
          .hunger(1)
          .saturationModifier(0.2F)
          .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 150, 2), 0.5F)
          .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 1), 0.2f)
          .snack()
          .alwaysEdible()
          .build();

  public BlockItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public boolean isFood() {
    return getFoodComponent() != null;
  }

  @Nullable
  @Override
  public FoodComponent getFoodComponent() {
    if (getBlock() instanceof PlantBlock && !(getBlock() instanceof CropBlock)) {
      return flowerFood;
    }

    return super.getFoodComponent();
  }
}
