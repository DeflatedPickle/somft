/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection")

package com.deflatedpickle.somftcraft.enchantment

import net.minecraft.item.HorseArmorItem
import net.minecraft.item.Item

enum class SomftEnchantmentTarget {
    HORSE {
        override fun isAcceptableItem(item: Item) = item is HorseArmorItem
    }
    ;
    abstract fun isAcceptableItem(item: Item): Boolean
}
