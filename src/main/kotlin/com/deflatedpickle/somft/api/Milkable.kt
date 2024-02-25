/* Copyright (c) 2023-2024 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")
package com.deflatedpickle.somft.api

interface Milkable {
    fun `somft$isMilked`(): Boolean
    fun `somft$setMilked`(milked: Boolean)
    fun `somft$isMilkable`(): Boolean
}
