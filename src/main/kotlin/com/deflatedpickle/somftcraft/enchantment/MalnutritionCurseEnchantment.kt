/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.enchantment.Enchantments

object MalnutritionCurseEnchantment : Enchantment(
    Rarity.VERY_RARE,
    EnchantmentTarget.ARMOR,
    Enchantments.ALL_ARMOR
) {
    override fun getMinPower(level: Int) = 25
    override fun getMaxPower(level: Int) = 50
    override fun isTreasure() = true
    override fun isCursed() = true
}
