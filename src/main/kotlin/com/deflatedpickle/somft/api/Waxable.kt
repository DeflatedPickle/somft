/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

interface Waxable {
    fun `somft$getWaxed`(): Boolean
    fun `somft$setWaxed`(waxed: Boolean, updateComparators: Boolean): Boolean
}
