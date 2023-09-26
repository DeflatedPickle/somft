/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")

package com.deflatedpickle.somftcraft.api

import net.minecraft.client.model.ModelPart

interface HasPieces {
    fun `somftcraft$getPieces`(): List<ModelPart>
}
