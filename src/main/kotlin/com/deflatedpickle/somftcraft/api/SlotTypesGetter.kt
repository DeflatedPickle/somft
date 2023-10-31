/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somftcraft.api

import net.minecraft.entity.EquipmentSlot

interface SlotTypesGetter {
    fun `somft$getSlotTypes`(): Array<EquipmentSlot>
}
