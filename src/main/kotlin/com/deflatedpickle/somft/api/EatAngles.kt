/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

interface EatAngles {
    fun `somft$getNeckAngle`(delta: Float): Float
    fun `somft$getHeadAngle`(delta: Float): Float
}
