/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")

package com.deflatedpickle.somft.api

import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.server.world.ServerWorld

interface Breedable {
    fun `somft$isAdult`(): Boolean
    fun `somft$isInLove`(): Boolean
    fun `somft$canBreedWith`(other: WaterCreatureEntity): Boolean
    fun `somft$breed`(world: ServerWorld, other: SquidEntity?)
}
