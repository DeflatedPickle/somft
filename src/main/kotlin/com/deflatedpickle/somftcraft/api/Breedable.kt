/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection", "FunctionName")

package com.deflatedpickle.somftcraft.api

import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.server.world.ServerWorld

interface Breedable {
    fun `somftcraft$isAdult`(): Boolean
    fun `somftcraft$isInLove`(): Boolean
    fun `somftcraft$canBreedWith`(other: WaterCreatureEntity): Boolean
    fun `somftcraft$breed`(world: ServerWorld, other: SquidEntity?)
}
