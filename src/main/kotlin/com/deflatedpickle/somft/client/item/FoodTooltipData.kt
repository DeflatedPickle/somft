/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.item

import net.minecraft.client.item.TooltipData
import net.minecraft.item.FoodComponent

data class FoodTooltipData(
    val foodComponent: FoodComponent,
) : TooltipData
