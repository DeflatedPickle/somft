/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

interface Timer {
    fun `somft$getTimer`(): Int
    fun `somft$setTimer`(time: Int)
}
