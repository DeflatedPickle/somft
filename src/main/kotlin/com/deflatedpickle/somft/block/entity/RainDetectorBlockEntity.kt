/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.block.entity

import com.deflatedpickle.somft.Somft
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class RainDetectorBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(
    Somft.RAIN_DETECTOR_BLOCK_ENTITY,
    pos, state
)
