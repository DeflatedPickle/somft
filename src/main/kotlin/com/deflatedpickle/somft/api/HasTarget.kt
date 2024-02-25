/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

@file:Suppress("PropertyName", "SpellCheckingInspection")

package com.deflatedpickle.somft.api

interface HasTarget {
    val `somft$canSwarm`: Boolean
    var `somft$target`: Targetable?
}
