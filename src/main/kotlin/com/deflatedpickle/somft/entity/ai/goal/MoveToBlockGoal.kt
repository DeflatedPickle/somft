/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.entity.ai.goal

import net.minecraft.block.BlockState
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.MobEntity
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import java.util.EnumSet
import java.util.Optional
import java.util.function.Predicate

class MoveToBlockGoal @JvmOverloads constructor(
    val entity: MobEntity,
    val searchDistance: Double = 5.0,
    val attentionDistance: Double = 32.0,
    val predicate: Predicate<BlockState> = ediblePlantsPredicate
) : Goal() {
    var block: BlockPos? = null

    companion object {
        val ediblePlantsPredicate =
            Predicate { state: BlockState ->
                if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
                    false
                } else if (state.isIn(BlockTags.SMALL_FLOWERS) || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.CROPS)) {
                    true
                } else {
                    false
                }
            }

        val drinkableFluidPredicate =
            Predicate { state: BlockState ->
                if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
                    return@Predicate true
                } else false
            }
    }

    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    override fun canStart(): Boolean {
        val block = this.findBlock(this.predicate, this.searchDistance)

        if (block.isPresent) {
            this.block = block.get()
            return true
        }
        return false
    }

    override fun tick() {
        this.block?.let { block ->
            if (!this.entity.navigation.isFollowingPath) {
                if (!block.isWithinDistance(this.entity.blockPos, this.attentionDistance) || this.entity.world.getBlockState(block).isAir) {
                    this.block = null
                } else {
                    this.entity.navigation.startMovingTo(
                        block.x.toDouble(),
                        block.y.toDouble(),
                        block.z.toDouble(),
                        1.0
                    )
                }
            }
        }
    }

    // modified from: BeeEntity#findFlower
    private fun findBlock(
        predicate: Predicate<BlockState>,
        searchDistance: Double
    ): Optional<BlockPos> {
        val blockPos: BlockPos = this.entity.blockPos
        val mutable = BlockPos.Mutable()

        var i = 0
        while (i.toDouble() <= searchDistance) {
            var j = 0
            while (j.toDouble() < searchDistance) {
                var k = 0
                while (k <= j) {
                    var l = if (k < j && k > -j) j else 0
                    while (l <= j) {
                        mutable[blockPos, k, i - 1] = l
                        if (blockPos.isWithinDistance(
                                mutable,
                                searchDistance
                            ) && predicate.test(this.entity.world.getBlockState(mutable))
                        ) {
                            return Optional.of(mutable)
                        }
                        l = if (l > 0) -l else 1 - l
                    }
                    k = if (k > 0) -k else 1 - k
                }
                ++j
            }
            i = if (i > 0) -i else 1 - i
        }

        return Optional.empty()
    }
}
