/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.item;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

import com.deflatedpickle.somftcraft.SomftCraft;
import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("UnusedMixin")
@Mixin(HorseArmorItem.class)
public abstract class HorseArmorItemMixin extends Item {
  @Shadow @Final private int bonus;

  public HorseArmorItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public int getEnchantability() {
    System.out.println(this);
    if (this == Items.LEATHER_HORSE_ARMOR) {
      return 15;
    } else if (this == SomftCraft.INSTANCE.getCHAINMAIL_HORSE_ARMOUR()) {
      return 12;
    } else if (this == Items.IRON_HORSE_ARMOR) {
      return 9;
    } else if (this == Items.GOLDEN_HORSE_ARMOR) {
      return 25;
    } else if (this == Items.DIAMOND_HORSE_ARMOR) {
      return 10;
    } else if (this == SomftCraft.INSTANCE.getNETHERITE_HORSE_ARMOUR()) {
      return 15;
    } else {
      return 0;
    }
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(Text.translatable("item.modifiers.horse").formatted(Formatting.GRAY));
    tooltip.add(
        Text.translatable(
                "attribute.modifier.plus." + EntityAttributeModifier.Operation.ADDITION.getId(),
                MODIFIER_FORMAT.format(bonus),
                Text.literal("Armor"))
            .formatted(Formatting.BLUE));
  }
}
