/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somftcraft.api

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity

interface Leashable {
    fun `somft$updateLeash`()
    fun `somft$detachLeash`(sendPacket: Boolean, dropItem: Boolean)
    fun `somft$canBeLeashedBy`(playerEntity: PlayerEntity): Boolean
    fun `somft$isLeashed`(): Boolean
    fun `somft$getHoldingEntity`(): Entity?
    fun `somft$attachLeash`(entity: Entity, sendPacket: Boolean)
    fun `somft$setHoldingEntityId`(id: Int)
    fun `somft$readLeashNbt`()
}
