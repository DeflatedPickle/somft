/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")

package com.deflatedpickle.somft.api

interface DoesAge {
    fun `somft$isBaby`(): Boolean
    fun `somft$isAdult`(): Boolean
    fun `somft$onGrowUp`()
}
