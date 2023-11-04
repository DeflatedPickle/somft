/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.item

import net.minecraft.client.item.TooltipData

data class HealthTooltipData(
    val damage: Float,
) : TooltipData
