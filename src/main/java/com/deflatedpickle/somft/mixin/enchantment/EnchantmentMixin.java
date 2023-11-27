/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.enchantment;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.api.IncompatibleEnchantments;
import com.deflatedpickle.somft.api.SlotTypesGetter;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("UnusedMixin")
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements SlotTypesGetter, IncompatibleEnchantments {
  @Shadow @Final public EquipmentSlot[] slotTypes;

  @NotNull
  @Override
  public EquipmentSlot @NotNull [] somft$getSlotTypes() {
    return this.slotTypes;
  }

  @NotNull
  @Override
  public List<Enchantment> somft$getIncompatible() {
    return Impl.INSTANCE.getIncompatible((Enchantment) (Object) this);
  }
}
