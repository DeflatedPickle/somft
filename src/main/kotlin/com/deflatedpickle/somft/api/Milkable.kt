/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")
package com.deflatedpickle.somft.api

interface Milkable {
    fun `somft$getMilkTicks`(): Int
    fun `somft$setMilkTicks`(ticks: Int)
}
