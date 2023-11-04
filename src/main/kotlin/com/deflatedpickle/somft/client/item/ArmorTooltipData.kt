/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.item

import net.minecraft.client.item.TooltipData
import net.minecraft.item.ArmorItem.ArmorSlot
import net.minecraft.item.ArmorMaterial

data class ArmorTooltipData(
    val slot: ArmorSlot,
    val material: ArmorMaterial,
) : TooltipData
