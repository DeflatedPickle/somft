/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

import net.minecraft.entity.EquipmentSlot

interface SlotTypesGetter {
    fun `somft$getSlotTypes`(): Array<EquipmentSlot>
}
