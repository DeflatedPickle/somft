/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")

package com.deflatedpickle.somft.api

import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction

interface DirectionalFireSpreader : FireSpreader {
    fun `somft$getDirection`(state: BlockState): Direction
}
