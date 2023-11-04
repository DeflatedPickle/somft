/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;
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

  @Override
  public Optional<TooltipData> getTooltipData(ItemStack stack) {
    var nbt = BlockItem.getBlockEntityNbtFromStack(stack);
    if (nbt != null) {
      if (nbt.contains("Items", NbtElement.LIST_TYPE)) {
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
        return Optional.of(new BundleTooltipData(inventory, 27));
      }
    }
    return super.getTooltipData(stack);
  }
}
