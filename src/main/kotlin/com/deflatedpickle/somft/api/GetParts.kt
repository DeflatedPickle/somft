/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.api

import net.minecraft.client.model.ModelPart

interface GetParts {
    fun `somft$getHeadParts`(): Iterable<ModelPart>
    fun `somft$getBodyParts`(): Iterable<ModelPart>
}
