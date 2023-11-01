/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")

package com.deflatedpickle.somft.api

import net.minecraft.client.model.ModelPart

interface HasPieces {
    fun `somft$getPieces`(): List<ModelPart>
}
