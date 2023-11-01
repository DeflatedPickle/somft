/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.client.item.FoodTooltipData;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(Item.class)
public abstract class ItemMixin {
  @Inject(method = "use", at = @At("HEAD"), cancellable = true)
  public void use(
      World world,
      PlayerEntity user,
      Hand hand,
      CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
    var stack = user.getStackInHand(hand);
    if (stack.isEmpty() && user.isSneaking()) {
      if (Impl.INSTANCE.removeArrow(user, user)) {
        cir.setReturnValue(TypedActionResult.success(stack));
      }
    }
  }

  @Inject(method = "getTooltipData", at = @At("HEAD"), cancellable = true)
  public void getTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
    var foodComponent = stack.getItem().getFoodComponent();
    if (foodComponent != null) {
      cir.setReturnValue(Optional.of(new FoodTooltipData(foodComponent)));
    }
  }

  @Inject(method = "appendTooltip", at = @At("HEAD"))
  public void appendTooltip(
      ItemStack stack,
      @Nullable World world,
      List<Text> tooltip,
      TooltipContext context,
      CallbackInfo ci) {
    var foodComponent = stack.getItem().getFoodComponent();
    if (foodComponent != null) {
      tooltip.add(CommonTexts.EMPTY);
      tooltip.add(Text.translatable("item.modifiers.consumed").formatted(Formatting.GRAY));
      tooltip.add(
          Text.translatable(
                  "attribute.modifier.take.0",
                  MODIFIER_FORMAT.format(foodComponent.getHunger() / 2f),
                  Text.translatable("attribute.name.generic.hunger"))
              .formatted(Formatting.BLUE));
      tooltip.add(
          Text.translatable(
                  "attribute.modifier.plus.0",
                  MODIFIER_FORMAT.format(foodComponent.getSaturationModifier()),
                  Text.translatable("attribute.name.generic.saturation"))
              .formatted(Formatting.BLUE));
    }
  }
}
