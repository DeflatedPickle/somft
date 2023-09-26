/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft.api

import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction

interface DirectionalFireSpreader : FireSpreader {
    fun `somftcraft$getDirection`(state: BlockState): Direction
}
