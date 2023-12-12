/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION", "UNUSED_PARAMETER")

package com.deflatedpickle.somft.block

import com.deflatedpickle.somft.Somft
import com.deflatedpickle.somft.block.entity.RainDetectorBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.DaylightDetectorBlock
import net.minecraft.block.MapColor
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import kotlin.math.roundToInt

class RainDetectorBlock : BlockWithEntity(
    Settings.create()
        .mapColor(MapColor.STONE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresTool()
        .strength(0.4f)
        .sounds(BlockSoundGroup.STONE)
) {
    companion object {
        val POWER: IntProperty = Properties.POWER
        val INVERTED: BooleanProperty = Properties.INVERTED

        private fun updateState(state: BlockState, world: World, pos: BlockPos) {
            if (!world.getBiome(pos).value().hasPrecipitation()) return

            var power = when {
                world.getThunderGradient(1f) > 0.2f -> {
                    8 + world.getThunderGradient(1f) * 7
                }
                world.getRainGradient(1f) > 0.2f ->
                    world.getRainGradient(1f) * 8
                else -> 0f
            }.roundToInt()

            if (state.get(INVERTED)) {
                power = 15 - power
            }

            if (state.get(POWER) != power) {
                world.setBlockState(pos, state.with(DaylightDetectorBlock.POWER, power), NOTIFY_ALL)
            }
        }

        private fun tick(
            world: World,
            pos: BlockPos,
            state: BlockState,
            blockEntity: RainDetectorBlockEntity,
        ) {
            if (world.time % 20L == 0L) {
                updateState(state, world, pos)
            }
        }
    }

    init {
        this.defaultState = this.stateManager.defaultState
            .with(POWER, 0)
            .with(INVERTED, false)
    }

    override fun createBlockEntity(
        pos: BlockPos,
        state: BlockState
    ) = RainDetectorBlockEntity(pos, state)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = DaylightDetectorBlock.SHAPE

    override fun hasSidedTransparency(state: BlockState) = true

    override fun getWeakRedstonePower(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        direction: Direction
    ): Int = state.get(POWER)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult = if (player.canModifyBlocks()) {
        if (world.isClient) {
            ActionResult.SUCCESS
        } else {
            val blockState = state.cycle(DaylightDetectorBlock.INVERTED)
            world.setBlockState(pos, blockState, NO_REDRAW)
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(player, blockState))
            updateState(blockState, world, pos)
            ActionResult.CONSUME
        }
    } else {
        super.onUse(state, world, pos, player, hand, hit)
    }

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
    override fun isRedstonePowerSource(state: BlockState) = true
    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ) = if (!world.isClient) checkType(
        type, Somft.RAIN_DETECTOR_BLOCK_ENTITY, ::tick
    ) else null

    override fun appendProperties(
        builder: StateManager.Builder<Block, BlockState>
    ) {
        builder.add(
            POWER,
            INVERTED
        )
    }
}
