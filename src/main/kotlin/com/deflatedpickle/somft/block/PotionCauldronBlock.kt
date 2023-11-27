/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("OVERRIDE_DEPRECATION")

package com.deflatedpickle.somft.block

import com.deflatedpickle.somft.Somft
import com.deflatedpickle.somft.potion.PotionWrapper
import com.deflatedpickle.somft.state.property.PotionProperty
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.block.cauldron.LeveledCauldronBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

class PotionCauldronBlock : LeveledCauldronBlock(
    Settings.copy(Blocks.CAULDRON),
    { false },
    Somft.POTION_CAULDRON_BEHAVIOR,
) {
    companion object {
        val INCREASE_POTION =
            CauldronBehavior {
                blockState: BlockState,
                world: World,
                blockPos: BlockPos,
                playerEntity: PlayerEntity,
                hand: Hand,
                itemStack: ItemStack ->
                val potion = PotionUtil.getPotion(itemStack)
                if (blockState.get(LEVEL) != 3 && blockState.get(POTION).potion == potion) {
                    if (!world.isClient) {
                        playerEntity.setStackInHand(hand, ItemUsage.exchangeStack(itemStack, playerEntity, ItemStack(Items.GLASS_BOTTLE)))
                        playerEntity.incrementStat(Stats.USE_CAULDRON)
                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.item))
                        world.setBlockState(blockPos, blockState.cycle(LEVEL))
                        world.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f)
                        world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos)
                    }
                    ActionResult.success(world.isClient)
                } else {
                    ActionResult.PASS
                }
            }

        val DECREASE_POTION =
            CauldronBehavior {
                blockState: BlockState,
                world: World,
                blockPos: BlockPos,
                playerEntity: PlayerEntity,
                hand: Hand,
                itemStack: ItemStack ->
                if (!world.isClient) {
                    val potion = blockState.get(POTION).potion
                    playerEntity.setStackInHand(
                        hand,
                        ItemUsage.exchangeStack(itemStack, playerEntity, PotionUtil.setPotion(ItemStack(Items.POTION), potion))
                    )
                    playerEntity.incrementStat(Stats.USE_CAULDRON)
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.item))
                    decrementFluidLevel(blockState, world, blockPos)
                    world.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos)
                }

                ActionResult.success(world.isClient)
            }

        val POTION = PotionProperty("potion")
    }

    init {
        this.defaultState = this.stateManager.defaultState.with(POTION, PotionWrapper(Potions.EMPTY))
    }

    override fun appendProperties(
        builder: StateManager.Builder<Block, BlockState>
    ) {
        super.appendProperties(builder)
        builder.add(POTION)
    }

    override fun onEntityCollision(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: Entity
    ) {
        if (!world.isClient && isEntityTouchingFluid(state, pos, entity)) {
            val potion = state.get(POTION).potion

            if (entity is LivingEntity && potion.effects.map { entity.hasStatusEffect(it.effectType) }.any { !it }) {
                for (i in potion.effects) {
                    if (i.effectType.isInstant) {
                        i.effectType.applyInstantEffect(
                            entity,
                            entity,
                            entity,
                            i.amplifier,
                            1.0
                        )
                    } else {
                        entity.addStatusEffect(i)
                    }
                }

                potion.hasInstantEffect()

                decrementFluidLevel(state, world, pos)
            }
        }
    }
}
