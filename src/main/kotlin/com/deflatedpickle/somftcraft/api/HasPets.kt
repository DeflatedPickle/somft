/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somftcraft.api

import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound

interface HasPets {
    fun `somft$getPets`(): NbtCompound
    fun `somft$setPets`(compound: NbtCompound)
    fun `somft$addPet`(entity: Entity)
}
