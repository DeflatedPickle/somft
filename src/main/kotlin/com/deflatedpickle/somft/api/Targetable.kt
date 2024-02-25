/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

@file:Suppress("PropertyName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

interface Targetable {
    val `somft$canBeSwarmed`: Boolean
    var `somft$isTargeted`: Boolean
}
