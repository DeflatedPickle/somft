/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.deflatedpickle.somftcraft.api

interface PetLogic {
    enum class Movement {
        // move around aimlessly
        // TODO: implement
        WANDER,
        // follow owner
        FOLLOW,
        // follow owner until they get too far away
        FOLLOW_IN_RANGE,
        // follow owner but at a distance
        FOLLOW_DISTANCED,
        // never move
        STAY,
    }

    enum class Attack {
        // run away from hostiles
        // TODO: implement
        AVOID,
        // attack things targeting their owner
        GUARD,
        // preemptively attack hostiles
        ATTACK,
    }

    enum class Hurt {
        // fight back when hurt
        FIGHT,
        // flee when hurt
        FLEE,
    }

    fun `somft$setMovementLogic`(logic: Movement)
    fun `somft$getMovementLogic`(): Movement

    fun `somft$setAttackLogic`(logic: Attack)
    fun `somft$getAttackLogic`(): Attack

    fun `somft$setHurtLogic`(logic: Hurt)
    fun `somft$getHurtLogic`(): Hurt
}
