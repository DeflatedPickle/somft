/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.item;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.client.item.FoodTooltipData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

    var tame = new ArrayList<String>();
    var breed = new ArrayList<String>();
    var breedTamed = new ArrayList<String>();

    if (CamelEntity.TEMPT_INGREDIENT.test(stack)) {
      breed.add("entity.minecraft.camel");
    }

    if (stack.isOf(Items.CRIMSON_FUNGUS)) {
      breed.add("entity.minecraft.hoglin");
    }

    if (RabbitEntity.isTempting(stack)) {
      breed.add("entity.minecraft.rabbit");
    }

    if (stack.isIn(ItemTags.FLOWERS)) {
      breed.add("entity.minecraft.bee");
    }

    if (TurtleEntity.BREEDING_ITEM.test(stack)) {
      breed.add("entity.minecraft.turtle");
    }

    if (StriderEntity.BREEDING_INGREDIENT.test(stack)) {
      breed.add("entity.minecraft.strider");
    }

    if (stack.isOf(Blocks.BAMBOO.asItem())) {
      breed.add("entity.minecraft.panda");
    }

    if (stack.isIn(ItemTags.AXOLOTL_TEMPTING)) {
      breed.add("entity.minecraft.axolotl");
    }

    if (PigEntity.BREEDING_INGREDIENT.test(stack)) {
      breed.add("entity.minecraft.pig");
    }

    var item = stack.getItem();
    if (item.isFood() && item.getFoodComponent().isMeat()) {
      breedTamed.add("entity.minecraft.wolf");
    }

    if (CatEntity.TAMING_INGREDIENT.test(stack)) {
      tame.add("entity.minecraft.cat");
      breedTamed.add("entity.minecraft.cat");
    }

    if (OcelotEntity.TAMING_INGREDIENT.test(stack)) {
      tame.add("entity.minecraft.ocelot");
    }

    if (FrogEntity.SLIME_INGREDIENT.test(stack)) {
      breed.add("entity.minecraft.frog");
    }

    if (stack.isIn(ItemTags.FOX_FOOD)) {
      tame.add("entity.minecraft.fox");
      breed.add("entity.minecraft.fox");
    }

    if (HorseBaseEntity.BREEDING_INGREDIENT.test(stack)) {
      tame.addAll(
          List.of("entity.minecraft.horse", "entity.minecraft.donkey", "entity.minecraft.mule"));
    }

    if (stack.isOf(Items.GOLDEN_CARROT)
        || stack.isOf(Items.GOLDEN_APPLE)
        || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
      breedTamed.addAll(List.of("entity.minecraft.horse", "entity.minecraft.donkey"));
    }

    if (stack.isIn(ItemTags.SNIFFER_FOOD)) {
      breed.add("entity.minecraft.sniffer");
    }

    if (ChickenEntity.BREEDING_INGREDIENT.test(stack)) {
      breed.add("entity.minecraft.chicken");
    }

    if (stack.isOf(Items.WHEAT)) {
      breed.addAll(
          List.of(
              "entity.minecraft.cow",
              "entity.minecraft.goat",
              "entity.minecraft.mooshroom",
              "entity.minecraft.sheep"));
    }

    if (LlamaEntity.TAMING_INGREDIENT.test(stack)) {
      tame.add("entity.minecraft.llama");
      breedTamed.add("entity.minecraft.llama");
    }

    if (ParrotEntity.TAMING_INGREDIENTS.contains(stack.getItem())) {
      tame.add("entity.minecraft.parrot");
    }

    if (stack.isOf(Items.BONE)) {
      tame.add("entity.minecraft.wolf");
    }

    if (!tame.isEmpty() || !breed.isEmpty() || !breedTamed.isEmpty()) {
      tooltip.add(CommonTexts.EMPTY);
      tooltip.add(Text.translatable("interaction.entity").formatted(Formatting.GRAY));

      if (!tame.isEmpty()) {
        addTooltipLine(tooltip, "interaction.entity.tame", tame);
      }

      if (!breed.isEmpty()) {
        addTooltipLine(tooltip, "interaction.entity.breed", breed);
      }

      if (!breedTamed.isEmpty()) {
        addTooltipLine(tooltip, "interaction.entity.breed.tamed", breedTamed);
      }
    }
  }

  @Unique
  private void addTooltipLine(List<Text> tooltip, String key, List<String> mob) {
    tooltip.add(
        CommonTexts.space()
            .append(
                Text.translatable(
                        key,
                        mob.stream()
                            .map(I18n::translate)
                            .collect(
                                Collectors.joining(I18n.translate("interaction.entity.delimiter"))))
                    .formatted(Formatting.BLUE)));
  }
}
