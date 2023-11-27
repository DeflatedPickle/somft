/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

import net.minecraft.enchantment.Enchantment

interface IncompatibleEnchantments {
    fun `somft$getIncompatible`(): List<Enchantment>
}
