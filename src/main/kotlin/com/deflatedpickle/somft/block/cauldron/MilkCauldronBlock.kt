/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("OVERRIDE_DEPRECATION")

package com.deflatedpickle.somft.block.cauldron

import com.deflatedpickle.somft.Somft
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.cauldron.AbstractCauldronBlock
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object MilkCauldronBlock : AbstractCauldronBlock(
    Settings.copy(Blocks.CAULDRON),
    Somft.MILK_CAULDRON_BEHAVIOR,
) {
    val FILL_WITH_MILK =
        CauldronBehavior {
            state: BlockState,
            world: World,
            pos: BlockPos,
            player: PlayerEntity,
            hand: Hand,
            stack: ItemStack ->
            CauldronBehavior.fillCauldron(
                world,
                pos,
                player,
                hand,
                stack,
                Blocks.LAVA_CAULDRON.defaultState,
                SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW
            )
        }
    override fun getFluidHeight(state: BlockState) = 0.9
    override fun isFull(state: BlockState) = true
    override fun getComparatorOutput(
        state: BlockState,
        world: World,
        pos: BlockPos
    ) = 3

    override fun onEntityCollision(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: Entity
    ) {
        if (isEntityTouchingFluid(state, pos, entity)) {
            if (entity is LivingEntity) {
                entity.clearStatusEffects()
            }
        }
    }
}
