/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.entity.ai.goal

import com.deflatedpickle.somftcraft.api.Breedable
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World
import java.util.EnumSet

class SquidMateGoal(
    private val animal: SquidEntity,
    private val chance: Double,
) : Goal() {
    companion object {
        private val VALID_MATE_PREDICATE =
            TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0).ignoreVisibility()
    }

    private val world: World = animal.world
    private var mate: SquidEntity? = null
    private var timer = 0

    init {
        controls = EnumSet.of(Control.MOVE, Control.LOOK)
    }

    override fun canStart(): Boolean {
        return if (!(animal as Breedable).`somftcraft$isInLove`()) {
            false
        } else {
            mate = findMate()
            mate != null
        }
    }

    override fun shouldContinue(): Boolean {
        return mate!!.isAlive && (mate as Breedable).`somftcraft$isInLove`() && timer < 60
    }

    override fun stop() {
        mate = null
        timer = 0
    }

    override fun tick() {
        animal.lookControl.lookAt(mate, 10.0f, animal.lookPitchSpeed.toFloat())
        animal.navigation.startMovingTo(mate, chance)
        ++timer
        if (timer >= getTickCount(60) && animal.squaredDistanceTo(mate) < 9.0) {
            breed()
        }
    }

    private fun findMate(): SquidEntity? {
        val list = world.getTargets(animal::class.java, VALID_MATE_PREDICATE, animal, animal.boundingBox.expand(8.0))
        var d = Double.MAX_VALUE
        var animalEntity: SquidEntity? = null
        for (animalEntity2 in list) {
            if ((animal as Breedable).`somftcraft$canBreedWith`(animalEntity2) && animal.squaredDistanceTo(animalEntity2) < d) {
                animalEntity = animalEntity2
                d = animal.squaredDistanceTo(animalEntity2)
            }
        }
        return animalEntity
    }

    private fun breed() {
        (animal as Breedable).`somftcraft$breed`(world as ServerWorld, mate)
    }
}
