/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.block.dispenser

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.CarvedPumpkinBlock
import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.block.dispenser.ItemDispenserBehavior
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPointer

object TorchDispenserBehavior : ItemDispenserBehavior() {
    override fun dispenseSilently(pointer: BlockPointer, stack: ItemStack): ItemStack {
        val world = pointer.world
        val pos = pointer.pos.offset(pointer.blockState.get(DispenserBlock.FACING))
        val state = world.getBlockState(pos)
        val block = state.block

        return if (block == Blocks.CARVED_PUMPKIN) {
            val facing = state.get(CarvedPumpkinBlock.FACING)

            world.playSound(null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.setBlockState(
                pos,
                Blocks.JACK_O_LANTERN.defaultState.with(CarvedPumpkinBlock.FACING, facing),
                Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD
            )

            stack
        } else {
            super.dispenseSilently(pointer, stack)
        }
    }
}
